package sf.house.excel.base;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.ConvertUtil;
import sf.house.bean.util.TypeUtil;
import sf.house.excel.annotations.ExcelExportField;
import sf.house.excel.annotations.ExcelParseField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hznijianfeng on 2018/8/28.
 */

@Getter
public class ExcelTargetClazz<C> {

    public enum ClazzType {
        FIELD_PARSE,
        FIELD_EXPORT,;
    }

    private Class<C> target;
    private List<Field> fields = new ArrayList<>();
    private List<String> fieldNames = new ArrayList<>();
    private List<Class> fieldTypes = new ArrayList<>();
    private List<Method> setFieldMethods = new ArrayList<>();
    private List<String> setFieldMethodNames = new ArrayList<>();
    private List<Method> getFieldMethods = new ArrayList<>();
    private List<String> getFieldMethodNames = new ArrayList<>();
    private List<ExcelParseField> excelParseFields = new ArrayList<>();
    private List<ExcelExportField> excelExportFields = new ArrayList<>();

    public ExcelTargetClazz(@NonNull Class<C> target, @NonNull ClazzType clazzType) {
        this.target = target;

        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (ClazzType.FIELD_PARSE.equals(clazzType)) {
                ExcelParseField excelParseField = field.getAnnotation(ExcelParseField.class);
                if (excelParseField == null) {
                    continue;
                }
                validExcelParseField(excelParseField, field.getType());
                this.excelParseFields.add(excelParseField);
            } else if (ClazzType.FIELD_EXPORT.equals(clazzType)) {
                ExcelExportField excelExportField = field.getAnnotation(ExcelExportField.class);
                if (excelExportField == null) {
                    continue;
                }
                this.excelExportFields.add(excelExportField);
            } else {
                continue;
            }
            this.fields.add(field);
            this.fieldNames.add(fieldName);
            this.fieldTypes.add(field.getType());
            String setMethodName = "set" + ConvertUtil.firstUpper(field.getName());
            String getMethodName = "get" + ConvertUtil.firstUpper(field.getName());
            this.setFieldMethodNames.add(setMethodName);
            this.getFieldMethodNames.add(getMethodName);
            try {
                this.setFieldMethods.add(target.getMethod(setMethodName, field.getType()));
                this.getFieldMethods.add(target.getMethod(getMethodName));
            } catch (NoSuchMethodException e) {
                throw UnifiedException.gen(Constants.MODULE, "解析" + this.target.getName() + "出错", e);
            }
        }
    }

    public C newInstance() {
        try {
            return target.newInstance();
        } catch (Exception e) {
            throw UnifiedException.gen(Constants.MODULE, "新建" + this.target.getName() + "出错", e);
        }
    }

    public void setFieldValue(Method method, C target, Object value) {
        try {
            method.invoke(target, value);
        } catch (Exception e) {
            throw UnifiedException.gen(Constants.MODULE, "设值" +
                    this.target.getName() + "-" + method.getName() + "出错", e);
        }
    }

    public Object getFieldValue(Method method, C target) {
        try {
            return method.invoke(target);
        } catch (Exception e) {
            throw UnifiedException.gen(Constants.MODULE, "取值" +
                    this.target.getName() + "-" + method.getName() + "出错", e);
        }
    }

    public List<Integer> getCellIndexes(Integer fieldIndex, Integer rowMaxCellIndex) {
        List<Integer> cellIndexes = Lists.newArrayList();
        ExcelParseField excelField = this.getExcelParseFields().get(fieldIndex);
        if (excelField.cellIndex().length != 0) {
            cellIndexes.addAll(Arrays.stream(excelField.cellIndex()).boxed().collect(Collectors.toList()));
        } else if (excelField.startIndex() != Integer.MIN_VALUE) {
            int startIndex = excelField.startIndex();
            int endIndex;
            if (excelField.endIndex() == Integer.MAX_VALUE) {
                endIndex = rowMaxCellIndex - 1;
            } else {
                endIndex = excelField.endIndex();
            }
            for (int cellIndex = startIndex; cellIndex <= endIndex; cellIndex++) {
                cellIndexes.add(cellIndex);
            }
        }
        return cellIndexes;
    }

    private void validExcelParseField(@NonNull ExcelParseField excelParseField, @NonNull Class fieldType) {
        if (excelParseField.startIndex() == Integer.MIN_VALUE && excelParseField.cellIndex().length == 0) {
            throw UnifiedException.gen(Constants.MODULE, "范围列或者指定列不能同时为空");
        }
        if (excelParseField.startIndex() != Integer.MIN_VALUE && excelParseField.cellIndex().length != 0) {
            throw UnifiedException.gen(Constants.MODULE, "范围列或者指定列不能同时指定");
        }
        List<Class> classes = Arrays.asList(
                String.class, Date.class, Integer.class,
                Long.class, BigDecimal.class, Boolean.class,
                boolean.class, int.class, long.class);
        boolean validList = false;
        if (excelParseField.startIndex() != Integer.MIN_VALUE) {
            if (excelParseField.endIndex() < 0 || excelParseField.startIndex() < 0) {
                throw UnifiedException.gen(Constants.MODULE, "范围列时，endIndex或者endIndex 不能小于0");
            }
            if (excelParseField.startIndex() >= excelParseField.endIndex()) {
                throw UnifiedException.gen(Constants.MODULE, "范围列时，endIndex 不能小于等于 startIndex");
            }
            validList = true;
        }
        if (excelParseField.cellIndex().length != 0) {
            if (excelParseField.cellIndex().length == 1) {
                if (!classes.contains(fieldType) && !TypeUtil.isAssignableFrom(BaseEnum.class, fieldType)) {
                    throw UnifiedException.gen(Constants.MODULE, "指定列时，dataType 属性类型不合法");
                }
            } else {
                validList = true;
            }
        }
        if (validList) {
            if (fieldType != List.class) {
                throw UnifiedException.gen(Constants.MODULE, "指定多列时，属性必须是 list 类型");
            }
            if (!classes.contains(excelParseField.dataType()) && !TypeUtil.isAssignableFrom(BaseEnum.class, excelParseField.dataType())) {
                throw UnifiedException.gen(Constants.MODULE, "指定多列时，dataType 属性类型不合法");
            }
        }
    }

}
