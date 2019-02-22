package sf.house.bean.util;

import sf.house.bean.constant.Contants;

import java.util.regex.Pattern;

public class ValidUtil {

    // 获取字符串的字符数，中文算两个字符
    public static Integer getCharLength(String str) {
        if (str == null) {
            return 0;
        }
        return str.replaceAll(Contants.CHINESE_REG_EX, "aa").length();
    }

    // 手机号码的正则表达式
    public static boolean isPhoneNo(String str) {
        return match(str, "[1]{1}[3|4|5|7|8|9]{1}[0-9]{9}");
    }

    // 校验邮箱格式
    public static boolean isEmail(String email) {
        return match(email, "^([\\w-\\.]+)@[\\w-.]+(\\.?[a-zA-Z]{2,4}$)");
    }

    // 0.1
    // 111.22
    // 102,112,222
    // 102,112,222.121123
    public static boolean isAmout(String number) {
        return match(number, "^([0]{1})|([1-9]{1}([0-9]*|[0-9]{0,2}(,[0-9]{3})*)([.]{1}[0-9]+)?)$");
    }

    public static boolean match(String str, String regexp) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        return Pattern.matches(regexp, str);
    }

}