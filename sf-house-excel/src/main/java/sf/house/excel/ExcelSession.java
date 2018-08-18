package sf.house.excel;

import com.google.common.collect.Lists;
import sf.house.excel.base.BaseEnum;
import sf.house.excel.excps.ExcelParseException;
import sf.house.excel.excps.ExcelParseExceptionInfo;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ExcelSession {

    public enum ExcelType {
        XLSX, XLS
    }

    private static final Pattern CN_PATTERN = Pattern.compile("[\\u4E00-\\u9FA5]");
    private static float DEFAULT_ROW_HEIGHT = 35;

    @Getter
    private Workbook workbook;
    @Getter
    private Sheet sheet;

    public ExcelSession(@NonNull ExcelType excelType, @NonNull String sheetName) {
        if (ExcelType.XLSX.equals(excelType)) {
            this.workbook = new XSSFWorkbook();
        } else if (ExcelType.XLS.equals(excelType)) {
            this.workbook = new HSSFWorkbook();
        } else {
            throw new RuntimeException("没有此类型的excel");
        }
        this.sheet = createSheet(sheetName);
    }

    public ExcelSession(@NonNull Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    public ExcelSession(@NonNull Workbook workbook) {
        this(workbook, null);
        Sheet sheet;
        try {
            sheet = workbook.getSheetAt(0);
        } catch (IllegalArgumentException e) {
            sheet = workbook.createSheet();
        }
        this.sheet = sheet;
    }

    public ExcelSession(@NonNull InputStream inputStream) {
        try {
            this.workbook = WorkbookFactory.create(inputStream);
            changeSheet(0);
        } catch (Exception e) {
            throw new RuntimeException("excel文件流有误!", e);
        }
    }

    public ExcelSession(@NonNull String filePath) {
        try {
            this.workbook = WorkbookFactory.create(new FileInputStream(filePath));
            changeSheet(0);
        } catch (Exception e) {
            throw new RuntimeException("excel文件流有误!", e);
        }
    }

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

    // 设置单元格的值
    public Cell setCellValue(int rowIndex, int colIndex, Object value) {
        Cell cell = getCell(rowIndex, colIndex, sheet.getDefaultColumnWidth());
        if (value == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(value.toString());
        }
        return cell;
    }

    // 设置单元格的值并指定样式
    public Cell setCellValue(int rowIndex, int colIndex, Object value, CellStyle cellStyle) {
        Cell cell = setCellValue(rowIndex, colIndex, value);
        cell.setCellStyle(copyCellStyle(cellStyle));
        return cell;
    }

    // 获取或者创建单元格(指定列宽)
    public Cell getCell(int rowIndex, int colIndex, Integer colWidth) {
        Cell cell = this.sheet.getRow(rowIndex).getCell(colIndex);
        if (cell == null) {
            cell = this.sheet.getRow(rowIndex).createCell(colIndex);
            hiddenColumn(colIndex, false);
            this.sheet.setColumnWidth(colIndex, colWidth);
        }
        return cell;
    }

    // 拷贝单元格样式
    public void copyCellStyle(int fromRowIndex, int fromColIndex, int toRowIndex, int toColIndex) {
        Cell fromCell = this.sheet.getRow(fromRowIndex).getCell(fromColIndex);
        if (fromCell == null) {
            log.info(this.sheet.getSheetName() + "第" + fromRowIndex + "行,第" + fromColIndex + "列不存在！");
            return;
        }
        CellStyle fromStyle = fromCell.getCellStyle();
        Cell toCell = getCell(toRowIndex, toColIndex, this.sheet.getColumnWidth(fromColIndex));
        // 样式
        toCell.setCellStyle(copyCellStyle(fromStyle));
        // 列宽
        this.sheet.getRow(toRowIndex).setHeight(this.sheet.getRow(fromRowIndex).getHeight());
        hiddenColumn(toColIndex, sheet.isColumnHidden(fromColIndex));
    }

    // 隐藏某一列
    public void hiddenColumn(int colIndex, boolean isHidden) {
        this.sheet.setColumnHidden(colIndex, isHidden);
    }

    // 在某行后插入若干行 带样式
    public void insertRows(int starRow, int rowNum) {
        this.sheet.shiftRows(starRow + 1, this.sheet.getLastRowNum(), rowNum, true, false);
        starRow = starRow - 1;
        for (int i = 0; i < rowNum; i++) {
            Row sourceRow;
            Row targetRow;
            Cell sourceCell;
            Cell targetCell;
            short m;
            starRow = starRow + 1;
            sourceRow = sheet.getRow(starRow);
            targetRow = sheet.createRow(starRow + 1);
            targetRow.setHeight(sourceRow.getHeight());
            for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++) {
                sourceCell = sourceRow.getCell(m);
                targetCell = targetRow.createCell(m);
                copyCell(sheet, sheet, sourceCell, targetCell, false);
            }
        }
    }

    // 冻结窗口 colSplit, rowSplit, leftmostColumn, topRow
    public void freezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
    }

    // 复制sheet
    public void copySheet(String name) {
        Sheet fromSheet = this.sheet;
        Sheet toSheet = createSheet(name);
        // 合并区域处理
        mergerRegion(fromSheet, toSheet);
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
            Row tmpRow = (Row) rowIt.next();
            Row newRow = toSheet.createRow(tmpRow.getRowNum());
            // 行复制
            copyRow(fromSheet, toSheet, tmpRow, newRow, true);
        }
    }

    // 复制行
    public void copyRow(Sheet fromSheet, Sheet toSheet, Row fromRow, Row toRow, boolean isCopyValue) {
        // 设置行高
        toRow.setHeight(fromRow.getHeight());
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
            Cell tmpCell = (Cell) cellIt.next();
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
            copyCell(fromSheet, toSheet, tmpCell, newCell, isCopyValue);
        }
    }

    // 复制列
    public void copyCell(Sheet fromSheet, Sheet toSheet, Cell srcCell, Cell distCell, boolean isCopyValue) {
        // 设置列宽和是否隐藏
        int fromColIndex = srcCell.getColumnIndex();
        int toColIndex = distCell.getColumnIndex();
        toSheet.setColumnWidth(toColIndex, fromSheet.getColumnWidth(fromColIndex));
        toSheet.setColumnHidden(toColIndex, fromSheet.isColumnHidden(fromColIndex));
        // 样式
        distCell.setCellStyle(copyCellStyle(srcCell.getCellStyle()));
        // 评论
        distCell.setCellComment(srcCell.getCellComment());
        // 不同数据类型处理
        int srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (isCopyValue) {
            if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
                if (DateUtil.isCellDateFormatted(srcCell)) {
                    distCell.setCellValue(srcCell.getDateCellValue());
                } else {
                    distCell.setCellValue(srcCell.getNumericCellValue());
                }
            } else if (srcCellType == Cell.CELL_TYPE_STRING) {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            } else if (srcCellType == Cell.CELL_TYPE_BLANK) {
                // nothing21
            } else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
                distCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (srcCellType == Cell.CELL_TYPE_ERROR) {
                distCell.setCellErrorValue(srcCell.getErrorCellValue());
            } else if (srcCellType == Cell.CELL_TYPE_FORMULA) {
                distCell.setCellFormula(srcCell.getCellFormula());
            } else { // nothing29
            }
        }
    }

    // 复制样式
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

    // 创建样式
    public CellStyle createDefaultCellStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        // 指定单元格居中对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    // 合并单元格
    public void mergerRegion(Sheet fromSheet, Sheet toSheet) {
        int sheetMergerCount = fromSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(mergedRegionAt);
        }
    }

    /**
     * 创建row
     */
    public Row getRow(int rownum) {
        Row row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }
        return row;
    }

    /**
     * 获取cell
     */
    public Cell getCell(int rownum, int cellnum) {
        return getCell(rownum, cellnum, Row.CREATE_NULL_AS_BLANK);
    }

    /**
     * 获取某行某列的单元格
     */
    public Cell getCell(int rownum, int cellnum, Row.MissingCellPolicy missingCellPolicy) {
        return getRow(rownum).getCell(cellnum, missingCellPolicy);
    }

    /**
     * 获取近似列宽(像素)
     */
    public float getCellApproximateWidth(int row, int column) {
        Cell cell = sheet.getRow(row).getCell(column);
        int x = getColNum(cell.getRowIndex(), cell.getColumnIndex());
        float totalWidthInPixels = 0;
        for (int i = 0; i < x; i++) {
            totalWidthInPixels += cell.getSheet().getColumnWidth(i + cell.getColumnIndex());
        }
        Font font = cell.getSheet().getWorkbook().getFontAt(cell.getCellStyle().getFontIndex());
        float f = totalWidthInPixels * 1.2F / 256 * (font.getFontHeightInPoints() / 2);
        return f;
    }

    /**
     * 获取单元格字体大小
     */
    public short getCellFontSize(int row, int column) {
        CellStyle cellStyle = getCell(row, column).getCellStyle();
        Font font = workbook.getFontAt(cellStyle.getFontIndex());
        return font.getFontHeightInPoints();
    }

    /**
     * 获取单元格的列数
     */
    public int getColNum(int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        // 判断该单元格是否是合并区域的内容
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow && column >= firstColumn && column <= lastColumn) {
                return lastColumn - firstColumn + 1;
            }
        }
        return 1;
    }

    /**
     * 获取单元格内容的字符宽度
     */
    public int getCellPixLen(String val) {
        if (val == null) {
            return 0;
        }

        int cnNum = 0;
        for (int index = 0; index < val.length(); index++) {
            Matcher matcher = CN_PATTERN.matcher(val.substring(index, index + 1));
            if (matcher.find()) {
                cnNum++;
            }
        }
        return cnNum + val.length();
    }

    /**
     * 获取近似行高
     */
    public float getCellApproximateHeight(int row, int column) {
        short fontSize = getCellFontSize(row, column);
        float cellWidth = getCellApproximateWidth(row, column);
        String content = getCellContent(row, column);
        int contentLen = getCellPixLen(content);

        return cellWidth == 0 ? DEFAULT_ROW_HEIGHT : fontSize * fontSize * contentLen / cellWidth + 1;
    }

    /**
     * 设置合适的行高
     */
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
            if (rowHeightLimit < DEFAULT_ROW_HEIGHT) {
                rowHeightLimit = DEFAULT_ROW_HEIGHT;
            }
            getRow(row).setHeightInPoints(rowHeightLimit);
        }
    }

    /**
     * 获取单元格中的字面内容
     */
    public String getCellContent(int rownum, int colnum) {
        Cell cell = getCell(rownum, colnum);
        return this.getCellContent(cell);
    }

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
    public void save(@NonNull String filePath) {
        try {
            byte[] ret = this.getBytes();
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(ret, 0, ret.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    ///////////////////////////////////////////// 导出数据到excel//////////////////////////////////////////////////////

    /**
     * 导出数据到excel
     * 
     * @param data
     * @param type
     */
    public void export(List data, Class type) {
        // 还未创建sheet返回
        if (this.workbook == null || this.sheet == null) {
            return;
        }
        // 组装数据
        Field[] fields = type.getDeclaredFields();
        int rowOffset = 0;
        Row row = this.getRow(rowOffset++);
        // 设置标题列
        for (Field field : fields) {
            ExcelExportField excelAnnotation = field.getAnnotation(ExcelExportField.class);
            if (excelAnnotation == null)
                continue;
            int cellIndex = excelAnnotation.cellIndex();
            String title = excelAnnotation.title();
            this.setCell(row, cellIndex, title);
        }
        // 遍历list
        exportData(data, type, fields, rowOffset);
        // 自适应行高
        rowHeightAutoFit(new CellRangeAddress(0, data.size(), 0, fields.length));
    }

    /**
     * 从某行开始导出到excel
     * 
     * @param data
     * @param type
     * @param startRowIndex
     */
    public void export(List<?> data, Class type, int startRowIndex) {
        // 还未创建sheet返回
        if (this.workbook == null || this.sheet == null) {
            return;
        }
        // 组装数据
        Field[] fields = type.getDeclaredFields();
        // 遍历list
        exportData(data, type, fields, startRowIndex);
        // 自适应行高
        rowHeightAutoFit(new CellRangeAddress(0, data.size() + startRowIndex - 1, 0, fields.length));
    }

    private void exportData(@NonNull List data, Class<?> type, Field[] fields, int rowOffset) {
        if (data.size() > 0) {
            for (Object obj : data) {
                Row row = this.getRow(rowOffset++);
                for (Field field : fields) {
                    ExcelExportField excelAnnotation = field.getAnnotation(ExcelExportField.class);
                    if (excelAnnotation == null)
                        continue;
                    int cellIndex = excelAnnotation.cellIndex();
                    // 利用反射赋值
                    StringBuilder sb = new StringBuilder();
                    String fieldName = field.getName();
                    sb.append("get");
                    sb.append(fieldName.substring(0, 1).toUpperCase());
                    sb.append(fieldName.substring(1));
                    try {
                        Method method = type.getMethod(sb.toString());
                        Object result = method.invoke(obj);
                        if (result != null) {
                            String val = result.toString();
                            if (val != null && !"".equals(val)) {
                                if (field.getType() == Date.class) {
                                    this.setCell(row, cellIndex,
                                            new SimpleDateFormat(excelAnnotation.dateFormat()).format(result));
                                } else if (field.getType() == Double.class) {
                                    this.setCell(row, cellIndex, (Double) result);
                                } else {
                                    this.setCell(row, cellIndex, String.valueOf(result));
                                }
                                this.sheet.setColumnWidth(cellIndex, excelAnnotation.colWidth());
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("给excel设值失败.", e);
                    }

                }
            }
        }
    }

    // 设置cell编码解决中文高位字节截断
    private static short XLS_ENCODING = HSSFCell.ENCODING_UTF_16;
    // 定制浮点数格式
    private static String NUMBER_FORMAT = " #,##0.00 ";

    public boolean isRowEmpty(@NonNull Row row) {
        Iterator<Cell> cellIter = row.cellIterator();
        boolean isRowEmpty = true;
        while (cellIter.hasNext()) {
            Cell cell = cellIter.next();
            String value = getCellContent(cell);
            if (value != null && !"".equals(value.trim())) {
                isRowEmpty = false;
                break;
            }
        }
        return isRowEmpty;
    }

    public String getCellContent(@NonNull Cell cell) {
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                Instant instant = date.toInstant();
                LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return formatter.format(localDateTime);
            } else {
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getRichStringCellValue().getString();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return String.valueOf(cell.getErrorCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula();
        } else {
        }
        return null;
    }

    // 设置单元格填充值
    public void setCell(@NonNull Row row, @NonNull int colIndex, String value) {
        Cell cell = row.createCell(colIndex);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellType(XLS_ENCODING);
        cell.setCellValue(value);
        CellStyle cellStyle = createDefaultCellStyle();
        cell.setCellStyle(cellStyle);
    }

    public void setCell(@NonNull Row row, @NonNull int colIndex, Double value) {
        Cell cell = row.createCell(colIndex);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        CellStyle cellStyle = createDefaultCellStyle();
        cellStyle.setDataFormat(this.workbook.createDataFormat().getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
        cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
    }

    /////////////////////////////////////////////////// 导入excel数据/////////////////////////////////////////////////
    /**
     * 从startRow开始以行获取excel
     *
     * @param startRow
     * @param targetClass
     * @return
     * @throws ExcelParseException
     */
    public <D> List<D> excelParse(Integer startRow, Class<D> targetClass) throws ExcelParseException {
        if (sheet == null || workbook == null)
            return Lists.newArrayList();
        List<D> retList = Lists.newArrayList();
        boolean isExcelExp = false;
        List<ExcelParseExceptionInfo> infoList = Lists.newArrayList();
        Iterator<Row> iter = sheet.rowIterator();
        for (int i = 0; i < startRow; i++) {
            iter.next();
        }
        while (iter.hasNext()) {
            Row row = iter.next();
            boolean isEmpty = this.isRowEmpty(row);
            if (!isEmpty) {
                try {
                    retList.add(rowParse(row, targetClass));
                } catch (ExcelParseException e) {
                    isExcelExp = true;
                    infoList.addAll(e.getInfoList());
                }
            }
        }
        if (isExcelExp) {
            throw new ExcelParseException(infoList);
        }
        return retList;
    }

    /**
     * 获取excel某一行
     *
     * @param row
     * @param targetClass
     * @return
     * @throws ExcelParseException
     */
    public <D> D rowParse(Row row, Class<D> targetClass) throws ExcelParseException {
        Object target;
        boolean isExcelExp = false;
        List<ExcelParseExceptionInfo> infoList = Lists.newArrayList();
        try {
            if (sheet == null || workbook == null) {
                return targetClass.newInstance();
            }
            target = targetClass.newInstance();
            String sheetName = this.sheet.getSheetName();
            Field[] fields = targetClass.getDeclaredFields();
            int rowNum = row.getRowNum() + 1;
            for (Field field : fields) {
                // 只注入有注解的属性
                ExcelField excelAnnotation = field.getAnnotation(ExcelField.class);
                if (excelAnnotation == null)
                    continue;
                String title = excelAnnotation.title();
                int cellIndex = excelAnnotation.cellIndex();
                Object value = null;
                // 获取数据并转换类型
                if (row.getLastCellNum() >= cellIndex) {
                    Cell cell = row.getCell(cellIndex);
                    try {
                        value = getFieldValue(cell, field.getType());
                    } catch (Exception e) {
                        isExcelExp = true;
                        ExcelParseExceptionInfo expInfo =
                                new ExcelParseExceptionInfo(this.sheet.getSheetName(), rowNum, title, "录入数据和定义属性类型有误");
                        infoList.add(expInfo);
                        continue;
                    }
                }
                // 检验是否必须
                boolean required = excelAnnotation.required();
                if (required && (value == null || (value instanceof String && "".equals(((String) value).trim())))) {
                    isExcelExp = true;
                    ExcelParseExceptionInfo expInfo =
                            new ExcelParseExceptionInfo(sheetName, rowNum, title, "定义属性不能为空而录入数据为空");
                    infoList.add(expInfo);
                    continue;
                }
                // 属性类型是枚举的校验
                if (value instanceof BaseEnum) {
                    int valueTemp = ((BaseEnum) value).getValue();
                    String descTemp = ((BaseEnum) value).getDesc();
                    if ("NULL".equals(descTemp) && valueTemp == -1) {
                        isExcelExp = true;
                        ExcelParseExceptionInfo expInfo =
                                new ExcelParseExceptionInfo(sheetName, rowNum, title, "录入数据不在枚举指定范围内");
                        infoList.add(expInfo);
                        continue;
                    }
                }
                // 校验单元格的字符串的最大长度或者数值的最大值
                if (value != null) {
                    String format = excelAnnotation.dateParse();
                    if (field.getType() == Date.class) {
                        try {
                            value = new SimpleDateFormat(format).parse((String) value);
                        } catch (Exception e) {
                            isExcelExp = true;
                            ExcelParseExceptionInfo expInfo =
                                    new ExcelParseExceptionInfo(sheetName, rowNum, title, "日期格式需要满足" + format);
                            infoList.add(expInfo);
                            continue;
                        }
                    }

                    String max = excelAnnotation.max();
                    if (!"-1".equals(max)) {
                        if (field.getType() == BigDecimal.class) {
                            BigDecimal maxValue = new BigDecimal(max);
                            if (((BigDecimal) value).compareTo(maxValue) > 0) {
                                isExcelExp = true;
                                ExcelParseExceptionInfo expInfo =
                                        new ExcelParseExceptionInfo(sheetName, rowNum, title, "数值必须小于" + max);
                                infoList.add(expInfo);
                                continue;
                            }
                        }
                        if (field.getType() == Long.class) {
                            Long maxValue = Long.valueOf(max);
                            if ((((Long) value) - maxValue) > 0) {
                                isExcelExp = true;
                                ExcelParseExceptionInfo expInfo =
                                        new ExcelParseExceptionInfo(sheetName, rowNum, title, "数值必须小于" + max);
                                infoList.add(expInfo);
                                continue;
                            }
                        }
                        if (field.getType() == Integer.class) {
                            Integer maxValue = Integer.valueOf(max);
                            if ((((Integer) value) - maxValue) > 0) {
                                isExcelExp = true;
                                ExcelParseExceptionInfo expInfo =
                                        new ExcelParseExceptionInfo(sheetName, rowNum, title, "数值必须小于" + max);
                                infoList.add(expInfo);
                                continue;
                            }
                        }
                        if (field.getType() == String.class) {
                            if (((String) value).length() > Integer.valueOf(max)) {
                                isExcelExp = true;
                                ExcelParseExceptionInfo expInfo =
                                        new ExcelParseExceptionInfo(sheetName, rowNum, title, "文字必须在" + max + "字以内");
                                infoList.add(expInfo);
                                continue;
                            }
                        }
                    }
                }
                // 利用反射赋值
                StringBuilder sb = new StringBuilder();
                String fieldName = field.getName();
                sb.append("set");
                sb.append(fieldName.substring(0, 1).toUpperCase());
                sb.append(fieldName.substring(1));
                Method setMethod = targetClass.getMethod(sb.toString(), field.getType());
                setMethod.invoke(target, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (isExcelExp) {
            throw new ExcelParseException(infoList);
        }
        return (D) target;
    }

    private Object getFieldValue(Cell cell, Class fieldTypeClass) {
        if (cell == null)
            return null;
        String strVal = this.getCellContent(cell);
        if (strVal == null || "".equals(strVal.trim())) {
            return null;
        }
        Object value;
        if (BaseEnum.class.isAssignableFrom(fieldTypeClass)) {
            // CASE1: ExcelEnum值
            value = ((BaseEnum) fieldTypeClass.getEnumConstants()[0]).getEnumByDesc(strVal);
        } else if (fieldTypeClass == BigDecimal.class) {
            // CASE2: BigDecimal
            value = new BigDecimal(strVal);
        } else if (fieldTypeClass == Long.class || fieldTypeClass == long.class) {
            // CASE3: LONG
            value = Long.valueOf(strVal);
        } else if (fieldTypeClass == Integer.class || fieldTypeClass == int.class) {
            // CASE4: INT
            value = Integer.valueOf(strVal);
        } else {
            // CASE7: DEFAULT
            value = strVal;
        }
        return value;
    }


}
