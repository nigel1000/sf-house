package sf.house.bean;


import lombok.extern.slf4j.Slf4j;
import sf.house.bean.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
public class DateUtilTest {

    public static void main(String[] args) {
        log.info("getCurrentDate:" + DateUtil.getCurrentDate());
        log.info("toLocalDate:" + DateUtil.toLocalDate(DateUtil.getCurrentDate()));
        log.info("toLocalDateTime:" + DateUtil.toLocalDateTime(DateUtil.getCurrentDate()));
        log.info("getStartOfDay:" + DateUtil.getStartOfDay(DateUtil.getCurrentDate()));
        log.info("getEndOfDay:" + DateUtil.getEndOfDay(DateUtil.getCurrentDate()));
        log.info("toDate:" + DateUtil.toDate(LocalDate.now()));
        log.info("toDate:" + DateUtil.toDate(LocalDateTime.now()));
        log.info("formatByDateTimeFormatter:" + DateUtil.formatByDateTimeFormatter(DateUtil.getCurrentDate()));
        log.info("formatByDateFormatter:" + DateUtil.formatByDateFormatter(DateUtil.getCurrentDate()));
        log.info("formatByChinaDateFormatter:" + DateUtil.formatByChinaDateFormatter(DateUtil.getCurrentDate()));
        log.info("formatByChinaSimpleDateFormatter:" + DateUtil.formatByChinaSimpleDateFormatter(DateUtil.getCurrentDate()));
        log.info("getDayOfWeek:" + DateUtil.getDayOfWeek(LocalDate.now()));


    }

}
