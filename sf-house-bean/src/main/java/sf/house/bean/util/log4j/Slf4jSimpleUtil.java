package sf.house.bean.util.log4j;

import org.slf4j.Logger;
import org.slf4j.impl.SimpleLogger;
import org.slf4j.impl.SimpleLoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.slf4j.spi.LocationAwareLogger;
import sf.house.bean.excps.UnifiedException;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by nijianfeng on 2018/11/9.
 */
public class Slf4jSimpleUtil {

    public static void fillLoggerInfo(Map<String, Object> loggerMap, Map<String, String> loggerLevelMap) {
        try {
            SimpleLoggerFactory simpleLoggerFactory =
                    (SimpleLoggerFactory) StaticLoggerBinder.getSingleton().getLoggerFactory();
            Field field = SimpleLoggerFactory.class.getDeclaredField("loggerMap");
            field.setAccessible(true);
            ConcurrentMap<String, Logger> simpleLoggerMap = (ConcurrentMap) field.get(simpleLoggerFactory);
            for (Map.Entry<String, Logger> entry : simpleLoggerMap.entrySet()) {
                String loggerName = entry.getKey();
                SimpleLogger logger = (SimpleLogger) entry.getValue();
                loggerMap.putIfAbsent(loggerName, logger);
                if (logger.isErrorEnabled()) {
                    loggerLevelMap.putIfAbsent(loggerName, LocationAwareLogger.ERROR_INT + "");
                    continue;
                }
                if (logger.isWarnEnabled()) {
                    loggerLevelMap.putIfAbsent(loggerName, LocationAwareLogger.WARN_INT + "");
                    continue;
                }
                if (logger.isInfoEnabled()) {
                    loggerLevelMap.putIfAbsent(loggerName, LocationAwareLogger.INFO_INT + "");
                    continue;
                }
                if (logger.isDebugEnabled()) {
                    loggerLevelMap.putIfAbsent(loggerName, LocationAwareLogger.DEBUG_INT + "");
                    continue;
                }
                if (logger.isTraceEnabled()) {
                    loggerLevelMap.putIfAbsent(loggerName, LocationAwareLogger.TRACE_INT + "");
                    continue;
                }
            }
        } catch (Exception ex) {
            throw UnifiedException.gen("解析 SLF4J_SIMPLE SimpleLoggerFactory loggerMap 失败! ", ex);
        }
    }

    public static void setLoggerLevel(Object logger, String loggerLevel) {
        try {
            SimpleLogger simpleLogger = (SimpleLogger) logger;
            if ("error".equalsIgnoreCase(loggerLevel)) {
                loggerLevel = LocationAwareLogger.ERROR_INT + "";
            }
            if ("warn".equalsIgnoreCase(loggerLevel)) {
                loggerLevel = LocationAwareLogger.WARN_INT + "";
            }
            if ("info".equalsIgnoreCase(loggerLevel)) {
                loggerLevel = LocationAwareLogger.INFO_INT + "";
            }
            if ("debug".equalsIgnoreCase(loggerLevel)) {
                loggerLevel = LocationAwareLogger.DEBUG_INT + "";
            }
            if ("trace".equalsIgnoreCase(loggerLevel)) {
                loggerLevel = LocationAwareLogger.TRACE_INT + "";
            }
            Field field = SimpleLogger.class.getDeclaredField("currentLogLevel");
            field.setAccessible(true);
            field.set(simpleLogger, Integer.valueOf(loggerLevel));
        } catch (Exception ex) {
            throw UnifiedException.gen("赋值 SimpleLogger currentLogLevel 失败! ", ex);
        }
    }

}
