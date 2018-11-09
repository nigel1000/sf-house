package sf.house.excel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExportField {

    // excel的cellIndex
    int cellIndex() default 0;

    // excel的title
    String title() default "";

    // excel的列宽
    int colWidth() default 5 * 1000;

    // excel的date约束
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    // excel字段描述
    String desc() default "";
}
