package sf.house.bean.util;

import sf.house.bean.constant.Contants;

public class ValidUtil {

    /**
     * 获取字符串的字符数，中文算两个字符
     */
    public static Integer getCharLength(String str) {
        if (str == null) {
            return 0;
        }
        return str.replaceAll(Contants.CHINESE_REG_EX, "aa").length();
    }

}