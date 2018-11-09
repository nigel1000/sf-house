package sf.house.aop.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

public class NumUtil {

    public static long parse10kToFen(String moneyString) {

        if (StringUtils.isEmpty(moneyString)) {
            return 0;
        }
        BigDecimal bigDecimal = new BigDecimal(moneyString);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000000));
        return multiply.longValue();
    }

    public static String parseFenTo10k(Long money) {

        if (money == null) {
            return "0";
        }
        BigDecimal bigDecimal = new BigDecimal(money);
        BigDecimal divide = bigDecimal.divide(new BigDecimal(1000000));
        return divide.toString();
    }

    /**
     *
     * @param string
     * @return 默认值null
     */
    public static Integer parseToInteger(String string) {

        return parseToInteger(string, null);
    }

    public static Integer parseToInteger(String string, Integer value) {

        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    /**
     *
     * @param string
     * @return 默认值null
     */
    public static Long parseToLong(String string) {

        return parseToLong(string, null);
    }

    public static Long parseToLong(String string, Long value) {

        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return value;
        }
    }

}
