package sf.house.bean.util.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sf.house.bean.util.NullUtil;

import java.util.Map;

/**
 * Created by nijianfeng on 2018/11/9.
 */
public class LogbackClassicUtil {

    public static void fillLoggerInfo(Map<String, Object> loggerMap, Map<String, String> loggerLevelMap) {
        ch.qos.logback.classic.LoggerContext loggerContext =
                (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : loggerContext.getLoggerList()) {
            loggerMap.putIfAbsent(logger.getName(), logger);
            loggerLevelMap.putIfAbsent(logger.getName(),
                    NullUtil.validDefault(() -> logger.getLevel().toString(), null));
        }
        ch.qos.logback.classic.Logger rootLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        loggerMap.putIfAbsent(rootLogger.getName(), rootLogger);
        loggerLevelMap.putIfAbsent(rootLogger.getName(),
                NullUtil.validDefault(() -> rootLogger.getLevel().toString(), null));
    }

    public static void setLoggerLevel(Object logger, String loggerLevel) {
        ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) logger;
        ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(loggerLevel);
        targetLogger.setLevel(targetLevel);
    }

}
