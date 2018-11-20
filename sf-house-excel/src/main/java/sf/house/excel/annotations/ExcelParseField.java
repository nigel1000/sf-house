package sf.house.excel.annotations;

import sf.house.excel.base.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelParseField {

    // 必填约束
    boolean required() default false;

    String requiredTips() default "定义属性不能为空而录入数据为空";

    // excel的cellIndex
    int[] cellIndex() default {};

    int startIndex() default Integer.MIN_VALUE;

    int endIndex() default Integer.MIN_VALUE;

    Class dataType() default String.class;

    boolean ignoreNull() default true;

    // excel的title
    String title() default "";

    // 单元格的字符串的最大长度或者数值的最大值
    // 作用与类型：String,Integer,Long,BigDecimal
    long max() default Long.MIN_VALUE;

    String maxTips() default "数值必须小于" + Constants.MAX_PLACEHOLDER + "或者字符必须少于" + Constants.MAX_PLACEHOLDER;

    // 作用与类型：BaseEnum类
    String optionListTips() default "录入数据不在指定范围内[" + Constants.OPTION_LIST_PLACEHOLDER + "]";

    // 作用与类型：Date
    String dateParse() default "yyyy-MM-dd HH:mm:ss";

    String dateParseTips() default "日期格式需要满足" + Constants.DATE_PARSE_PLACEHOLDER;

    // 作用与类型：String
    String regex() default "";

    String regexTips() default "正则表达式：" + Constants.REGEX_PLACEHOLDER;

    // excel字段描述
    String desc() default "";
}
