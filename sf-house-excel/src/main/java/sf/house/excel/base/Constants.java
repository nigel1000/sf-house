package sf.house.excel.base;

import sf.house.excel.excps.ExcelParseExceptionInfo;

import java.util.regex.Pattern;

/**
 * Created by hznijianfeng on 2018/8/25.
 */

public class Constants {

    public final static String MODULE = "excel操作";

    // base enum 无效校验值
    public final static String ENUM_ILLEGAL_DESC_NULL = "NULL";
    public final static int ENUM_ILLEGAL_VALUE_NULL = -1;

    // tips 占位符
    public final static String MAX_PLACEHOLDER = "#{max_check}";
    public final static String DATE_PARSE_PLACEHOLDER = "#{date_parse_check}";
    public final static String OPTION_LIST_PLACEHOLDER = "#{option_list_check}";
    public final static String REGEX_PLACEHOLDER = "#{regex_check}";
    public final static String VALUE_COMMON_PLACEHOLDER = "#{current_value}";
    public final static String ROW_NUM_COMMON_PLACEHOLDER = "#{row_num}";
    public final static String COL_COMMON_PLACEHOLDER = "#{col_num}";
    public final static String COL_TITLE_PLACEHOLDER = "#{col_title}";
    public final static String SHEET_NAME_PLACEHOLDER = "#{sheet_name}";

    // 定制浮点数格式
    public final static String NUMBER_FORMAT = " #,##0.00 ";
    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // 匹配汉字
    public final static Pattern CN_PATTERN = Pattern.compile("[\\u4E00-\\u9FA5]");
    public final static float DEFAULT_ROW_HEIGHT = 35;
    // String默认返回
    public final static String DEFAULT_RETURN_EMPTY = "";

    public static String fillCommonPlaceholder(String value, ExcelParseExceptionInfo info) {
        return value.replace(VALUE_COMMON_PLACEHOLDER, info.getCurrentValue())
                .replace(ROW_NUM_COMMON_PLACEHOLDER, info.getRowNum() + "")
                .replace(COL_COMMON_PLACEHOLDER, info.getColNum() + "")
                .replace(COL_TITLE_PLACEHOLDER, info.getColumnName())
                .replace(SHEET_NAME_PLACEHOLDER, info.getSheetName());
    }

    public static boolean isEmpty(String value) {
        if (value == null) {
            return true;
        }
        if (value.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

}
