package sf.house.bean.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class DateUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter chinaDateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private static final DateTimeFormatter chinaSimpleDateFormatter = DateTimeFormatter.ofPattern("MM月dd日");

    private DateUtil() {}

    // Wed Jan 31 15:11:43 CST 2018
    public static Date getCurrentDate() {
        return new Date();
    }

    // Wed Jan 31 00:00:00 CST 2018
    public static Date toDate(LocalDate localDate) {
        if (localDate == null)
            return null;
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    // Wed Jan 31 15:11:43 CST 2018
    public static Date toDate(LocalDateTime localDateTime) {

        if (localDateTime == null)
            return null;
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date parseDateTime(String dateTime) {
        return toDate(LocalDateTime.parse(dateTime, dateTimeFormatter));
    }

    public static Date parseDate(String date) {
        return toDate(LocalDate.parse(date, dateFormatter));
    }


    // 2018-01-31
    public static LocalDate toLocalDate(Date date) {
        if (date == null)
            return null;
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // 2018-01-31T15:11:43.592
    public static LocalDateTime toLocalDateTime(Date date) {

        if (date == null)
            return null;
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // 2018-01-31T00:00
    public static LocalDateTime getStartOfDay(Date date) {

        if (date == null)
            return null;
        LocalDate localDate = toLocalDate(date);
        return localDate.atStartOfDay();
    }

    // 2018-01-31T23:59:59.999999999
    public static LocalDateTime getEndOfDay(Date date) {

        if (date == null)
            return null;
        LocalDate localDate = toLocalDate(date);
        return LocalDateTime.of(localDate, LocalTime.MAX);
    }

    // 2018-01-31 15:11:43
    public static String formatByDateTimeFormatter(Date date) {
        return format(date, dateTimeFormatter);
    }

    // 2018-01-31
    public static String formatByDateFormatter(Date date) {
        return format(date, dateFormatter);
    }

    // 2018年01月31日
    public static String formatByChinaDateFormatter(Date date) {
        return format(date, chinaDateFormatter);
    }

    // 01月31日
    public static String formatByChinaSimpleDateFormatter(Date date) {
        return format(date, chinaSimpleDateFormatter);
    }

    public static String getDayOfWeek(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY:
                return "一";
            case TUESDAY:
                return "二";
            case WEDNESDAY:
                return "三";
            case THURSDAY:
                return "四";
            case FRIDAY:
                return "五";
            case SATURDAY:
                return "六";
            case SUNDAY:
                return "日";
        }
        return "";
    }

    private static String format(Date date, DateTimeFormatter formatter) {
        if (date == null)
            return "";
        LocalDateTime localDateTime = toLocalDateTime(date);
        return formatter.format(localDateTime);
    }

}
