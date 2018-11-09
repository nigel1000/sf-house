package sf.house.excel.base;

import lombok.Getter;
import lombok.NonNull;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.ConvertUtil;
import sf.house.excel.annotations.ExcelExportField;
import sf.house.excel.annotations.ExcelParseField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/28.
 */

@Getter
public class ExcelTargetClazz<C> {

    public enum ClazzType {
        FIELD_PARSE, FIELD_EXPORT,;
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
            throw UnifiedException.gen(Constants.MODULE, "设值" + this.target.getName() + "出错", e);
        }
    }

    public Object getFieldValue(Method method, C target) {
        try {
            return method.invoke(target);
        } catch (Exception e) {
            throw UnifiedException.gen(Constants.MODULE, "取值" + this.target.getName() + "出错", e);
        }
    }

}
