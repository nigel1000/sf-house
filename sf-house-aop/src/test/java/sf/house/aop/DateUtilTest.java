package sf.house.aop;


import sf.house.aop.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class DateUtilTest {

    public static void main(String[] args) {
        System.out.println("getCurrentDate:" + DateUtil.getCurrentDate());
        System.out.println("toLocalDate:" + DateUtil.toLocalDate(DateUtil.getCurrentDate()));
        System.out.println("toLocalDateTime:" + DateUtil.toLocalDateTime(DateUtil.getCurrentDate()));
        System.out.println("getStartOfDay:" + DateUtil.getStartOfDay(DateUtil.getCurrentDate()));
        System.out.println("getEndOfDay:" + DateUtil.getEndOfDay(DateUtil.getCurrentDate()));
        System.out.println("toDate:" + DateUtil.toDate(LocalDate.now()));
        System.out.println("toDate:" + DateUtil.toDate(LocalDateTime.now()));
        System.out
                .println("formatByDateTimeFormatter:" + DateUtil.formatByDateTimeFormatter(DateUtil.getCurrentDate()));
        System.out.println("formatByDateFormatter:" + DateUtil.formatByDateFormatter(DateUtil.getCurrentDate()));
        System.out.println(
                "formatByChinaDateFormatter:" + DateUtil.formatByChinaDateFormatter(DateUtil.getCurrentDate()));
        System.out.println("formatByChinaSimpleDateFormatter:"
                + DateUtil.formatByChinaSimpleDateFormatter(DateUtil.getCurrentDate()));
        System.out.println("getDayOfWeek:" + DateUtil.getDayOfWeek(LocalDate.now()));
    }

}
