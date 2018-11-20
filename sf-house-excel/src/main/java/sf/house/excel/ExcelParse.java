package sf.house.excel;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.TypeUtil;
import sf.house.excel.annotations.ExcelParseField;
import sf.house.excel.base.*;
import sf.house.excel.excps.ExcelParseException;
import sf.house.excel.excps.ExcelParseExceptionInfo;
import sf.house.excel.strategy.MaxCheckStrategy;
import sf.house.excel.strategy.OptionListCheckStrategy;
import sf.house.excel.strategy.RegexCheckStrategy;
import sf.house.excel.strategy.RequireCheckStrategy;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nijianfeng on 2018/8/26.
 */
@Slf4j
public class ExcelParse extends ExcelSession {

    private static final List<CheckStrategy> checkList = new ArrayList<>();

    static {
        checkList.add(new RequireCheckStrategy());
        checkList.add(new OptionListCheckStrategy());
        checkList.add(new MaxCheckStrategy());
        checkList.add(new RegexCheckStrategy());
    }

    @Setter
    @Getter
    private Integer importLimit;

    @Setter
    @Getter
    private boolean collectAllTips = false;

    public ExcelParse(@NonNull InputStream inputStream) {
        super(inputStream);
    }

    public ExcelParse(@NonNull String filePath) {
        super(filePath);
    }

    public ExcelParse(@NonNull Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    public ExcelParse(@NonNull Workbook workbook) {
        super(workbook);
    }

    /**
     * 从startRow开始以行获取excel
     */
    public <D> List<D> excelParse(Integer startRow, Class<D> targetClass) throws ExcelParseException {
        if (sheet == null || workbook == null) {
            return new ArrayList<>();
        }

        if (importLimit != null && sheet.getLastRowNum() >= (importLimit + startRow)) {
            throw UnifiedException.gen(Constants.MODULE, "导入超过了限制，限制:" + importLimit);
        }

        List<D> retList = new ArrayList<>();
        boolean isExcelExp = false;
        ExcelParseException excelParseException = new ExcelParseException(collectAllTips);
        Iterator<Row> iter = sheet.rowIterator();
        for (int i = 0; i < startRow; i++) {
            iter.next();
        }
        ExcelTargetClazz<D> targetClazz = new ExcelTargetClazz<>(targetClass, ExcelTargetClazz.ClazzType.FIELD_PARSE);
        while (iter.hasNext()) {
            Row row = iter.next();
            boolean isEmpty = this.isRowEmpty(row);
            if (!isEmpty) {
                try {
                    retList.add(rowParse(row, targetClazz));
                } catch (ExcelParseException e) {
                    isExcelExp = true;
                    excelParseException.addInfo(e.getInfoList());
                }
            }
        }
        if (isExcelExp) {
            throw excelParseException;
        }
        return retList;
    }

    /**
     * 获取excel某一行
     */
    private <D> D rowParse(Row row, ExcelTargetClazz<D> targetClazz) throws ExcelParseException {

        D result = targetClazz.newInstance();
        ExcelParseException excelParseException = new ExcelParseException(collectAllTips);
        if (sheet == null || workbook == null) {
            return result;
        }
        String sheetName = this.sheet.getSheetName();
        int rowNum = row.getRowNum() + 1;
        for (int i = 0; i < targetClazz.getFields().size(); i++) {
            ExcelParseField excelField = targetClazz.getExcelParseFields().get(i);
            String title = excelField.title();
            Class dataType;
            List<Integer> cellIndexs = Lists.newArrayList();
            if (excelField.cellIndex().length != 0) {
                cellIndexs.addAll(Arrays.stream(excelField.cellIndex()).boxed().collect(Collectors.toList()));
                if (excelField.cellIndex().length == 1) {
                    dataType = targetClazz.getFieldTypes().get(i);
                } else {
                    dataType = excelField.dataType();
                }
            } else if (excelField.startIndex() != Integer.MIN_VALUE) {
                int startIndex = excelField.startIndex();
                int endIndex;
                if (excelField.endIndex() == Integer.MAX_VALUE) {
                    // row.getLastCellNum() 不是从0开始的
                    endIndex = row.getLastCellNum() - 1;
                } else {
                    endIndex = excelField.endIndex();
                }
                for (int cellIndex = startIndex; cellIndex <= endIndex; cellIndex++) {
                    cellIndexs.add(cellIndex);
                }
                dataType = excelField.dataType();
            } else {
                continue;
            }
            List<Object> values = Lists.newArrayList();
            for (Integer cellIndex : cellIndexs) {
                if (row.getLastCellNum() <= cellIndex) {
                    continue;
                }
                int colNum = cellIndex + 1;
                Object value = null;
                String currentValue;
                // 获取数据并转换类型
                Cell cell = getCell(row.getRowNum(), cellIndex);
                currentValue = getCellValue(cell.getRowIndex(), cellIndex);
                if (Constants.isNotEmpty(currentValue)) {
                    try {
                        ExcelParseExceptionInfo expInfo = ExcelParseExceptionInfo.builder().columnName(title).rowNum(rowNum)
                                .sheetName(sheetName).colNum(colNum).currentValue(currentValue).build();
                        value = getFieldValue(currentValue, dataType, excelField, excelParseException, expInfo);
                    } catch (Exception e) {
                        log.debug(Constants.MODULE, e);
                        continue;
                    }
                }
                // 校验
                for (CheckStrategy checkStrategy : checkList) {
                    ExcelParseExceptionInfo expInfo = ExcelParseExceptionInfo.builder().columnName(title).rowNum(rowNum)
                            .sheetName(sheetName).colNum(colNum).currentValue(currentValue).build();
                    excelParseException.addInfo(checkStrategy.check(value, excelField, expInfo));
                }
                values.add(value);
            }
            if (excelField.ignoreNull()) {
                values.removeAll(Collections.singleton(null));
            }
            // 利用反射赋值
            if (excelParseException.isEmptyInfo()) {
                if (targetClazz.getFieldTypes().get(i) == List.class) {
                    targetClazz.setFieldValue(targetClazz.getSetFieldMethods().get(i), result, values);
                } else {
                    if (values.size() == 1) {
                        targetClazz.setFieldValue(targetClazz.getSetFieldMethods().get(i), result, values.get(0));
                    }
                }

            }
        }
        if (!excelParseException.isEmptyInfo()) {
            throw excelParseException;
        }
        return result;
    }

    private Object getFieldValue(String currentValue, Class fieldTypeClass, ExcelParseField excelAnnotation,
                                 ExcelParseException exp, ExcelParseExceptionInfo expInfo) {
        if (currentValue == null) {
            return null;
        }
        try {
            Object value;
            if (TypeUtil.isAssignableFrom(BaseEnum.class, fieldTypeClass)) {
                value = ((BaseEnum) fieldTypeClass.getEnumConstants()[0]).getEnumByDesc(currentValue);
            } else if (fieldTypeClass == Long.class || fieldTypeClass == long.class) {
                value = Long.valueOf(currentValue);
            } else if (fieldTypeClass == Integer.class || fieldTypeClass == int.class) {
                value = Integer.valueOf(currentValue);
            } else if (fieldTypeClass == BigDecimal.class) {
                value = new BigDecimal(currentValue);
            } else if (fieldTypeClass == Boolean.class || fieldTypeClass == boolean.class) {
                value = Boolean.valueOf(currentValue);
            } else if (fieldTypeClass == Date.class) {
                try {
                    value = new SimpleDateFormat(excelAnnotation.dateParse()).parse(currentValue);
                } catch (Exception e) {
                    String tips = Constants.fillCommonPlaceholder(excelAnnotation.dateParseTips()
                            .replace(Constants.DATE_PARSE_PLACEHOLDER, excelAnnotation.dateParse()), expInfo);
                    expInfo.setErrMsg(tips);
                    expInfo.setErrType(ErrorEnum.DATE_PARSE.getCode());
                    exp.addInfo(expInfo);
                    throw UnifiedException.gen(Constants.MODULE, "解析时间有误", e);
                }
            } else {
                value = currentValue;
            }
            return value;
        } catch (UnifiedException e) {
            throw e;
        } catch (Exception e) {
            String tips = "录入数据和定义属性类型有误";
            expInfo.setErrMsg(tips);
            expInfo.setCurrentValue(currentValue);
            expInfo.setErrType(ErrorEnum.NULL.getCode());
            exp.addInfo(expInfo);
            throw UnifiedException.gen(Constants.MODULE, "录入数据和定义属性类型有误", e);
        }
    }

}
