package sf.house.excel;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import sf.house.excel.annotations.ExcelExportField;
import sf.house.excel.base.ExcelTargetClazz;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nijianfeng on 2018/8/26.
 */
public class ExcelExport extends ExcelSession {

    @Setter
    @Getter
    private Integer exportLimit;

    @Setter
    @Getter
    private Boolean isCalRowHeight;

    public ExcelExport(@NonNull ExcelType excelType, @NonNull String sheetName) {
        super(excelType, sheetName);
    }

    public ExcelExport(@NonNull InputStream inputStream) {
        super(inputStream);
    }

    public ExcelExport(@NonNull String filePath) {
        super(filePath);
    }

    public ExcelExport(@NonNull Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    public ExcelExport(@NonNull Workbook workbook) {
        super(workbook);
    }

    /**
     * 导出title到excel
     */
    public <T> void export(Class<T> type) {
        // 还未创建sheet返回
        if (this.workbook == null || this.sheet == null) {
            return;
        }
        // 组装数据
        ExcelTargetClazz<T> targetClazz = new ExcelTargetClazz<>(type, ExcelTargetClazz.ClazzType.FIELD_EXPORT);
        Row row = this.getRow(0);
        // 设置标题列
        for (int i = 0; i < targetClazz.getFields().size(); i++) {
            ExcelExportField excelExportField = targetClazz.getExcelExportFields().get(i);
            int cellIndex = excelExportField.cellIndex();
            String title = excelExportField.title();
            setCellValue(row.getRowNum(), cellIndex, title);
            // 自适应行高
            rowHeightAutoFit(new CellRangeAddress(0, 0, 0, targetClazz.getFields().size()));
        }
    }

    /**
     * 导出数据到excel
     */
    public <T> void export(@NonNull List<T> data, Class<T> type) {
        // 导出 title
        export(type);
        // 导出数据
        export(data, type, 1);
    }

    /**
     * 从某行开始导出到excel
     */
    public <T> void export(@NonNull List<T> data, Class<T> type, int startRowIndex) {
        // 还未创建sheet返回
        if (this.workbook == null || this.sheet == null) {
            return;
        }
        if (exportLimit != null && data.size() > exportLimit) {
            data = data.subList(0, exportLimit);
            // throw UnifiedException.gen("excel导入", "excel导出限制最多" + exportLimit + "条");
        }
        // 遍历list
        ExcelTargetClazz<T> targetClazz = new ExcelTargetClazz<>(type, ExcelTargetClazz.ClazzType.FIELD_EXPORT);
        exportData(data, targetClazz, startRowIndex);
    }

    private <T> void exportData(@NonNull List<T> data, ExcelTargetClazz<T> targetClazz, int rowOffset) {
        if (data.size() > 0) {
            for (T obj : data) {
                Row row = this.getRow(rowOffset++);
                for (int i = 0; i < targetClazz.getFields().size(); i++) {
                    Field field = targetClazz.getFields().get(i);
                    ExcelExportField excelAnnotation = targetClazz.getExcelExportFields().get(i);
                    int cellIndex = excelAnnotation.cellIndex();
                    // 利用反射赋值
                    Object result = targetClazz.getFieldValue(targetClazz.getGetFieldMethods().get(i), obj);
                    if (field.getType() == Date.class) {
                        this.setSimpleDateFormat(new SimpleDateFormat(excelAnnotation.dateFormat()));
                    }
                    setCellValue(row.getRowNum(), cellIndex, result);
                    setCellWidth(cellIndex, excelAnnotation.colWidth());
                }
                // 自适应行高
                if (isCalRowHeight == null) {
                    row.setHeightInPoints(-1);
                } else if (isCalRowHeight) {
                    rowHeightAutoFit(
                            new CellRangeAddress(rowOffset - 1, rowOffset - 1, 0, targetClazz.getFields().size()));
                }
            }
        }
    }

}
