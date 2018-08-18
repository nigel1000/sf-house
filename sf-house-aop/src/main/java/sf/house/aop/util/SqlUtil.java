package sf.house.aop.util;

import org.springframework.util.StringUtils;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class SqlUtil {

    private SqlUtil() {}

    public static String outerVague(String column) {

        if (StringUtils.isEmpty(column)) {
            return null;
        }
        return "%" + column.trim() + "%";
    }

    public static String tailVague(String column) {

        if (StringUtils.isEmpty(column)) {
            return null;
        }
        return column.trim() + "%";
    }

    public static String trim(String column) {

        if (StringUtils.isEmpty(column)) {
            return null;
        }
        return column.trim();
    }
}
