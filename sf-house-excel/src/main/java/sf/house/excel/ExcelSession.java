package sf.house.excel;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.FileUtil;
import sf.house.excel.base.BaseEnum;
import sf.house.excel.base.Constants;
import sf.house.excel.base.ErrorEnum;
import sf.house.excel.excps.ExcelParseException;
import sf.house.excel.excps.ExcelParseExceptionInfo;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Slf4j
public class ExcelSession {

    public enum ExcelType {
        BIG_XLSX, XLSX, XLS
    }

    @Setter
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

    @Getter
    protected Workbook workbook;
    @Getter
    protected Sheet sheet;

    private CellStyle defaultStyle;

    public ExcelSession(@NonNull ExcelType excelType, @NonNull String sheetName) {
        if (ExcelType.XLSX.equals(excelType)) {
            this.workbook = new XSSFWorkbook();
        } else if (ExcelType.XLS.equals(excelType)) {
            this.workbook = new HSSFWorkbook();
        } else if (ExcelType.BIG_XLSX.equals(excelType)) {
            this.workbook = new SXSSFWorkbook(new XSSFWorkbook());
        } else {
            throw UnifiedException.gen(Constants.MODULE, "没有此类型的excel");
        }
        this.sheet = createSheet(sheetName);
        this.defaultStyle = createDefaultCellStyle();
    }

    public ExcelSession(@NonNull Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    public ExcelSession(@NonNull Workbook workbook) {
        this(workbook, null);
        try {
            this.sheet = workbook.getSheetAt(0);
        } catch (IllegalArgumentException e) {
            this.sheet = workbook.createSheet();
        }
    }

    public ExcelSession(@NonNull InputStream inputStream) {
        try {
            this.workbook = WorkbookFactory.create(inputStream);
            changeSheet(0);
        } catch (Exception e) {
            throw UnifiedException.gen(Constants.MODULE, "excel文件流有误!", e);
        }
    }

    public ExcelSession(@NonNull String filePath) {
        try {
            this.workbook = WorkbookFactory.create(new FileInputStream(filePath));
            changeSheet(0);
        } catch (Exception e) {
            throw UnifiedException.gen(Constants.MODULE, "excel文件流有误!", e);
        }
    }

    ///////////////////////////////// sheet////////////////////////////////
    // 创建sheet
    public Sheet createSheet(@NonNull String name) {
        return this.workbook.createSheet(name);
    }

    // 根据名称获取或者创建sheet
    public void changeSheet(@NonNull String name) {
        Sheet tmp = this.workbook.getSheet(name);
        if (tmp == null) {
            this.sheet = this.workbook.createSheet(name);
        } else {
            this.sheet = tmp;
        }
    }

    // 根据下标获取或者创建sheet
    public void changeSheet(int index) {
        Sheet tmp = this.workbook.getSheetAt(index);
        if (tmp == null) {
            this.sheet = this.workbook.createSheet("工作表" + (index + 1));
        } else {
            this.sheet = tmp;
        }
    }

    // 根据下标修改sheet名称
    public void setSheetName(int index, String name) {
        this.workbook.setSheetName(index, name);
    }
    ///////////////////////////////// sheet////////////////////////////////

    ///////////////////////////////// row&col////////////////////////////////
    // 设置单元格列的宽度
    public void setCellWidth(int colIndex, int colWidth) {
        this.sheet.setColumnWidth(colIndex, colWidth);
    }

    public int getCellWidth(int colIndex) {
        return this.sheet.getColumnWidth(colIndex);
    }

    // 隐藏某一列
    public void setHiddenColumn(int colIndex, boolean isHidden) {
        this.sheet.setColumnHidden(colIndex, isHidden);
    }

    public boolean isHiddenColumn(int colIndex) {
        return this.sheet.isColumnHidden(colIndex);
    }

    // 设置单元格的值
    public Cell setCellValue(int rowIndex, int colIndex, Object value) {
        Cell cell = this.getCell(rowIndex, colIndex);
        CellStyle cellStyle = createDefaultCellStyle();
        if (value == null) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue("");
        } else if (value.getClass() == String.class) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(value.toString());
        } else if (value instanceof BaseEnum) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(((BaseEnum) value).getDesc());
        } else if (value.getClass() == Double.class || value.getClass() == double.class) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Double) value);
            // 设置cell样式为定制的浮点数格式
            cellStyle.setDataFormat(createDefaultDoubleFormat());
        } else if (value.getClass() == Float.class || value.getClass() == float.class) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Float) value);
            // 设置cell样式为定制的浮点数格式
            cellStyle.setDataFormat(createDefaultDoubleFormat());
        } else if (value.getClass() == BigDecimal.class) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(((BigDecimal) value).doubleValue());
            // 设置cell样式为定制的浮点数格式
            cellStyle.setDataFormat(createDefaultDoubleFormat());
        } else if (value.getClass() == Date.class) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(this.simpleDateFormat.format((Date) value));
        } else if (value.getClass() == Integer.class || value.getClass() == int.class) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Integer) value);
        } else if (value.getClass() == Long.class || value.getClass() == long.class) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Long) value);
        } else if (value.getClass() == Boolean.class || value.getClass() == boolean.class) {
            cell.setCellType(CellType.BOOLEAN);
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(value.toString());
        }
        setCellStyle(rowIndex, colIndex, cellStyle);
        return cell;
    }

    public String getCellValue(int rowIndex, int colIndex) {
        String ret;
        Cell cell = this.getCell(rowIndex, colIndex);
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                Instant instant = date.toInstant();
                LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
                ret = formatter.format(localDateTime);
            } else {
                ret = NumberToTextConverter.toText(cell.getNumericCellValue());
            }
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            ret = cell.getRichStringCellValue().getString();
        } else if (cell.getCellTypeEnum() == CellType.BLANK) {
            ret = Constants.DEFAULT_RETURN_EMPTY;
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            ret = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.ERROR) {
            ret = String.valueOf(cell.getErrorCellValue());
        } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
            ret = cell.getCellFormula();
        } else {
            ret = cell.getStringCellValue();
        }
        if (ret == null) {
            ret = Constants.DEFAULT_RETURN_EMPTY;
        }
        return ret.trim();
    }

    // 设置单元格并指定样式
    public Cell setCellStyle(int rowIndex, int colIndex, CellStyle cellStyle) {
        Cell cell = this.getCell(rowIndex, colIndex);
        if (cellStyle == null) {
            return cell;
        }
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public CellStyle getCellStyle(int rowIndex, int colIndex) {
        Cell cell = this.getCell(rowIndex, colIndex);
        return cell.getCellStyle();
    }

    public CellStyle copyCellStyle(CellStyle fromStyle) {
        CellStyle toStyle = workbook.createCellStyle();
        toStyle.cloneStyleFrom(fromStyle);
        // toStyle.setAlignment(fromStyle.getAlignment());
        // // 边框和边框颜色
        // toStyle.setBorderBottom(fromStyle.getBorderBottom());
        // toStyle.setBorderLeft(fromStyle.getBorderLeft());
        // toStyle.setBorderRight(fromStyle.getBorderRight());
        // toStyle.setBorderTop(fromStyle.getBorderTop());
        // toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        // toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        // toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        // toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());
        // // 背景和前景
        // toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        // toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());
        // toStyle.setDataFormat(fromStyle.getDataFormat());
        // toStyle.setFillPattern(fromStyle.getFillPattern());
        // toStyle.setHidden(fromStyle.getHidden());
        // toStyle.setIndention(fromStyle.getIndention());// 首行缩进
        // toStyle.setLocked(fromStyle.getLocked());
        // toStyle.setRotation(fromStyle.getRotation());// 旋转
        // toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
        // toStyle.setWrapText(fromStyle.getWrapText());
        return toStyle;
    }

    public CellStyle createDefaultCellStyle() {
        if (defaultStyle != null) {
            return defaultStyle;
        }
        CellStyle cellStyle = workbook.createCellStyle();
        // 边框和边框颜色
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 指定单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        this.defaultStyle = cellStyle;
        return defaultStyle;
    }

    // 设置单元格描述
    public Cell setCellComment(int rowIndex, int colIndex, Comment comment) {
        Cell cell = this.getCell(rowIndex, colIndex);
        cell.setCellComment(comment);
        return cell;
    }

    public Comment getCellComment(int rowIndex, int colIndex) {
        Cell cell = this.getCell(rowIndex, colIndex);
        return cell.getCellComment();
    }

    // 获取默认的浮点数格式
    public short createDefaultDoubleFormat() {
        return this.workbook.createDataFormat().getFormat(Constants.NUMBER_FORMAT);
    }

    // 获取单元格字体大小
    public short getCellFontSize(int rowIndex, int colIndex) {
        CellStyle cellStyle = getCellStyle(rowIndex, colIndex);
        Font font = workbook.getFontAt(cellStyle.getFontIndex());
        return font.getFontHeightInPoints();
    }

    // 获取cell
    public Cell getCell(int rowIndex, int colIndex) {
        return getRow(rowIndex).getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    // 设置单元格列的宽度
    public void setRowHeight(int rowIndex, short rowHeight) {
        getRow(rowIndex).setHeight(rowHeight);
    }

    public short getRowHeight(int rowIndex) {
        return getRow(rowIndex).getHeight();
    }

    // 创建 row
    public Row getRow(int rowIndex) {
        Row row = this.sheet.getRow(rowIndex);
        if (row == null) {
            row = this.sheet.createRow(rowIndex);
        }
        return row;
    }

    public boolean isRowEmpty(@NonNull Row row) {
        Iterator<Cell> cellIter = row.cellIterator();
        boolean isRowEmpty = true;
        while (cellIter.hasNext()) {
            Cell cell = cellIter.next();
            String value = getCellValue(cell.getRowIndex(), cell.getColumnIndex());
            if (value != null && !"".equals(value.trim())) {
                isRowEmpty = false;
                break;
            }
        }
        return isRowEmpty;
    }

    // 冻结窗口 colSplit, rowSplit, leftmostColumn, topRow
    public void freezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        this.sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
    }

    // 合并单元格
    public void copyMergerRegion(Sheet fromSheet, Sheet toSheet) {
        int sheetMergerCount = fromSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(mergedRegionAt);
        }
    }

    public int getMergeColCount(int rowIndex, int colIndex) {
        int sheetMergeCount = this.sheet.getNumMergedRegions();
        // 判断该单元格是否是合并区域的内容
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress cellRangeAddress = this.sheet.getMergedRegion(i);
            int firstColumn = cellRangeAddress.getFirstColumn();
            int lastColumn = cellRangeAddress.getLastColumn();
            int firstRow = cellRangeAddress.getFirstRow();
            int lastRow = cellRangeAddress.getLastRow();
            // 如果cell在合并单元格中，则返回此合并单元格的列数
            if (rowIndex >= firstRow && rowIndex <= lastRow && colIndex >= firstColumn && colIndex <= lastColumn) {
                return lastColumn - firstColumn + 1;
            }
        }
        return 1;
    }

    public Map<Integer, String> getRowValueMap(@NonNull Row row) {
        Map<Integer, String> ret = Maps.newHashMap();
        // row.getLastCellNum() 不是从0开始的
        int lastCellNum = row.getLastCellNum();
        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
            ret.put(cellIndex, getCellValue(row.getRowNum(), cellIndex));
        }
        return ret;
    }
    ///////////////////////////////// row&col////////////////////////////////


    ///////////////////////////////// compose////////////////////////////////
    // 拷贝单元格全部样式
    public void copyCell(int fromRowIndex, int fromColIndex, int toRowIndex, int toColIndex) {
        Cell toCell = getCell(toRowIndex, toColIndex);
        setCellWidth(toCell.getColumnIndex(), getCellWidth(fromColIndex));
        setCellStyle(toCell.getRowIndex(), toCell.getColumnIndex(), getCellStyle(fromRowIndex, fromColIndex));
        setRowHeight(toCell.getRowIndex(), getRowHeight(fromRowIndex));
        setHiddenColumn(toCell.getColumnIndex(), isHiddenColumn(fromColIndex));
        setCellComment(toCell.getRowIndex(), toCell.getColumnIndex(), getCellComment(fromRowIndex, fromColIndex));
    }

    // 在某行后插入若干行 带样式
    public void insertRows(int starRow, int rowCount) {
        this.sheet.shiftRows(starRow + 1, this.sheet.getLastRowNum(), rowCount, true, false);
        starRow = starRow - 1;
        for (int i = 0; i < rowCount; i++) {
            Row sourceRow;
            Row targetRow;
            Cell sourceCell;
            Cell targetCell;
            short m;
            starRow = starRow + 1;
            sourceRow = getRow(starRow);
            targetRow = getRow(starRow + 1);
            setRowHeight(targetRow.getRowNum(), getRowHeight(sourceRow.getRowNum()));
            for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {
                sourceCell = getCell(sourceRow.getRowNum(), m);
                targetCell = getCell(targetRow.getRowNum(), m);
                copyCell(this.sheet, this.sheet, sourceCell, targetCell, false);
            }
        }
    }

    // 复制列
    public void copyCell(Sheet fromSheet, Sheet toSheet, Cell fromCell, Cell toCell, boolean isCopyValue) {
        // 设置列宽和是否隐藏
        int fromColIndex = fromCell.getColumnIndex();
        int toColIndex = toCell.getColumnIndex();
        toSheet.setColumnWidth(toColIndex, fromSheet.getColumnWidth(fromColIndex));
        toSheet.setColumnHidden(toColIndex, fromSheet.isColumnHidden(fromColIndex));
        // 样式
        toCell.setCellStyle(copyCellStyle(fromCell.getCellStyle()));
        // 评论
        toCell.setCellComment(fromCell.getCellComment());
        // 不同数据类型处理
        CellType srcCellType = fromCell.getCellTypeEnum();
        toCell.setCellType(srcCellType);
        if (isCopyValue) {
            if (srcCellType == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(fromCell)) {
                    toCell.setCellValue(fromCell.getDateCellValue());
                } else {
                    toCell.setCellValue(fromCell.getNumericCellValue());
                }
            } else if (srcCellType == CellType.STRING) {
                toCell.setCellValue(fromCell.getRichStringCellValue());
            } else if (srcCellType == CellType.BLANK) {
                toCell.setCellValue(fromCell.getStringCellValue());
            } else if (srcCellType == CellType.BOOLEAN) {
                toCell.setCellValue(fromCell.getBooleanCellValue());
            } else if (srcCellType == CellType.ERROR) {
                toCell.setCellErrorValue(fromCell.getErrorCellValue());
            } else if (srcCellType == CellType.FORMULA) {
                toCell.setCellFormula(fromCell.getCellFormula());
            } else {
                toCell.setCellValue(fromCell.getRichStringCellValue());
            }
        }
    }

    // 复制sheet
    public void copySheet(String name) {
        Sheet fromSheet = this.sheet;
        Sheet toSheet = createSheet(name);
        // 合并区域处理
        copyMergerRegion(fromSheet, toSheet);
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext(); ) {
            Row fromRow = (Row) rowIt.next();
            Row toRow = toSheet.createRow(fromRow.getRowNum());
            // 行复制
            copyRow(fromSheet, toSheet, fromRow, toRow, true);
        }
    }

    // 复制行
    public void copyRow(Sheet fromSheet, Sheet toSheet, Row fromRow, Row toRow, boolean isCopyValue) {
        // 设置行高
        toRow.setHeight(fromRow.getHeight());
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext(); ) {
            Cell fromCell = (Cell) cellIt.next();
            Cell toCell = toRow.createCell(fromCell.getColumnIndex());
            copyCell(fromSheet, toSheet, fromCell, toCell, isCopyValue);
        }
    }

    // 设置错误行的cellstyle
    public void setErrorCellStyle(ExcelParseException excption, Map<ErrorEnum, CellStyle> maps) {
        List<ExcelParseExceptionInfo> infos = excption.getInfoList();
        for (ExcelParseExceptionInfo info : infos) {
            setCellStyle(info.getRowNum() - 1, info.getColNum() - 1, maps.get(ErrorEnum.parseCode(info.getErrType())));
        }
    }
    ///////////////////////////////// compose////////////////////////////////

    ///////////////////////////////// 智能////////////////////////////////
    // 获取近似列宽(像素)
    public float getCellApproximateWidth(int rowIndex, int colIndex) {
        Cell cell = this.sheet.getRow(rowIndex).getCell(colIndex);
        int x = getMergeColCount(cell.getRowIndex(), cell.getColumnIndex());
        float totalWidthInPixels = 0;
        for (int i = 0; i < x; i++) {
            totalWidthInPixels += cell.getSheet().getColumnWidth(i + cell.getColumnIndex());
        }
        Font font = cell.getSheet().getWorkbook().getFontAt(cell.getCellStyle().getFontIndex());
        float f = totalWidthInPixels * 1.2F / 256 * (font.getFontHeightInPoints() / 2);
        return f;
    }

    // 获取单元格内容的字符宽度
    public int getCellValueLenght(int rowIndex, int colIndex) {
        String value = getCellValue(rowIndex, colIndex);
        int cnNum = 0;
        for (int index = 0; index < value.length(); index++) {
            Matcher matcher = Constants.CN_PATTERN.matcher(value.substring(index, index + 1));
            if (matcher.find()) {
                cnNum++;
            }
        }
        return cnNum + value.length();
    }

    // 获取近似行高
    public float getCellApproximateHeight(int rowIndex, int colIndex) {
        short fontSize = getCellFontSize(rowIndex, colIndex);
        float cellWidth = getCellApproximateWidth(rowIndex, colIndex);
        int contentLen = getCellValueLenght(rowIndex, colIndex);
        return cellWidth == 0 ? Constants.DEFAULT_ROW_HEIGHT : fontSize * fontSize * contentLen / cellWidth + 1;
    }

    // 设置合适的行高
    public void rowHeightAutoFit(CellRangeAddress cellRangeAddress) {
        int firstRow = cellRangeAddress.getFirstRow();
        int firstColumn = cellRangeAddress.getFirstColumn();
        int lastRow = cellRangeAddress.getLastRow();
        int lastColumn = cellRangeAddress.getLastColumn();
        for (int row = firstRow; row <= lastRow; row++) {
            float rowHeightLimit = 0;
            for (int column = firstColumn; column <= lastColumn; column++) {
                float tmpHeight = getCellApproximateHeight(row, column);
                if (tmpHeight > rowHeightLimit) {
                    rowHeightLimit = tmpHeight;
                }
            }
            if (rowHeightLimit < Constants.DEFAULT_ROW_HEIGHT) {
                rowHeightLimit = Constants.DEFAULT_ROW_HEIGHT;
            }
            getRow(row).setHeightInPoints(rowHeightLimit);
        }
    }
    ///////////////////////////////// 智能////////////////////////////////

    ///////////////////////////////// 转换////////////////////////////////
    // 获取byte数组
    public byte[] getBytes() {
        return getOutputStream().toByteArray();
    }

    // workbook转换成InputStream
    public InputStream getInputStream() {
        return new ByteArrayInputStream(getBytes());
    }

    // workbook转换成OutputStream
    public ByteArrayOutputStream getOutputStream() {
        if (workbook == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return os;
    }

    // 获取byte数组
    public File save(@NonNull String filePath) {
        try {
            File temp = new File(filePath);
            FileOutputStream out = new FileOutputStream(temp);
            workbook.write(out);
            return temp;
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw UnifiedException.gen("excel 文件生成", "excel save 失败", ex);
        }
    }

    public File saveTemp(@NonNull String prefix, @NonNull String suffix) {
        try {
            File temp = FileUtil.saveTempFile(prefix, suffix);
            FileOutputStream out = new FileOutputStream(temp);
            workbook.write(out);
            return temp;
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw UnifiedException.gen("excel 文件生成", "excel saveTemp 失败", ex);
        }
    }

    public void closeSource() {
        try {
            workbook.close();
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw UnifiedException.gen("excel 文件生成", "excel saveTemp 失败", ex);
        }
    }
    ///////////////////////////////// 转换////////////////////////////////

}
