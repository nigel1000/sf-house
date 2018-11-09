package sf.house.bean.util.log4j;

import sf.house.bean.util.NullUtil;

import java.util.Enumeration;
import java.util.Map;

/**
 * Created by nijianfeng on 2018/11/9.
 */
public class Slf4jLog4j12Util {

    public static void fillLoggerInfo(Map<String, Object> loggerMap, Map<String, String> loggerLevelMap) {
        Enumeration enumeration = org.apache.log4j.LogManager.getCurrentLoggers();
        while (enumeration.hasMoreElements()) {
            org.apache.log4j.Logger logger = (org.apache.log4j.Logger) enumeration.nextElement();
            loggerMap.putIfAbsent(logger.getName(), logger);
            loggerLevelMap.putIfAbsent(logger.getName(),
                    NullUtil.validDefault(() -> logger.getLevel().toString(), null));
        }
        org.apache.log4j.Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
        loggerMap.putIfAbsent(rootLogger.getName(), rootLogger);
        loggerLevelMap.putIfAbsent(rootLogger.getName(),
                NullUtil.validDefault(() -> rootLogger.getLevel().toString(), null));
    }

    public static void setLoggerLevel(Object logger, String loggerLevel) {
        org.apache.log4j.Logger targetLogger = (org.apache.log4j.Logger) logger;
        org.apache.log4j.Level targetLevel = org.apache.log4j.Level.toLevel(loggerLevel);
        targetLogger.setLevel(targetLevel);
    }

}
