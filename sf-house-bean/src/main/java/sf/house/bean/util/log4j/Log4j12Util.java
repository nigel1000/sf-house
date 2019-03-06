package sf.house.bean.util.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import sf.house.bean.util.NullUtil;

import java.util.Enumeration;
import java.util.Map;

/**
 * Created by nijianfeng on 2018/11/9.
 */
class Log4j12Util {

    public static void fillLoggerInfo(Map<String, Object> loggerMap, Map<String, String> loggerLevelMap) {
        Enumeration enumeration = org.apache.log4j.LogManager.getCurrentLoggers();
        while (enumeration.hasMoreElements()) {
            Logger logger = (Logger) enumeration.nextElement();
            loggerMap.putIfAbsent(logger.getName(), logger);
            loggerLevelMap.putIfAbsent(logger.getName(),
                    NullUtil.validDefault(() -> logger.getLevel().toString(), null));
        }
        Logger rootLogger = LogManager.getRootLogger();
        loggerMap.putIfAbsent(rootLogger.getName(), rootLogger);
        loggerLevelMap.putIfAbsent(rootLogger.getName(),
                NullUtil.validDefault(() -> rootLogger.getLevel().toString(), null));
    }

    public static void setLoggerLevel(Object logger, String loggerLevel) {
        Logger targetLogger = (Logger) logger;
        Level targetLevel = Level.toLevel(loggerLevel);
        targetLogger.setLevel(targetLevel);
    }

}
