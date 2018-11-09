package sf.house.bean.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;
import org.slf4j.impl.SimpleLoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.slf4j.spi.LocationAwareLogger;
import sf.house.bean.excps.UnifiedException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by hznijianfeng on 2018/11/9.
 */

@Slf4j
public class Log4jUtil {

    private final static String currentLog4jName = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
    private final static Map<String, Object> loggerMap = new HashMap<>();
    private final static Map<String, String> loggerLevelMap = new HashMap<>();
    private final static String SLF4J_LOG4J12 = "org.slf4j.impl.Log4jLoggerFactory";
    private final static String SLF4J_SIMPLE = "org.slf4j.impl.SimpleLoggerFactory";
    private final static String LOGBACK_CLASSIC = "ch.qos.logback.classic.util.ContextSelectorStaticBinder";
    private final static List<String> levels = Arrays.asList("error", "warn", "info", "debug", "trace");
    static {
        log.info("当前使用的 LoggerFactory :[{}]", currentLog4jName);
        initAndSync();
    }

    @SuppressWarnings("unchecked")
    private static void initAndSync() {
        if (SLF4J_LOG4J12.equals(currentLog4jName)) {
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
        } else if (SLF4J_SIMPLE.equals(currentLog4jName)) {
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
        } else if (LOGBACK_CLASSIC.equals(currentLog4jName)) {
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
        } else {
            throw UnifiedException.gen("Log框架无法识别: type={" + currentLog4jName + "}");
        }
    }

    public static void setLogLevel(String loggerName, String loggerLevel) {
        if (loggerName == null || loggerLevel == null) {
            return;
        }
        if (!levels.contains(loggerLevel)) {
            return;
        }
        Object logger = loggerMap.get(loggerName);
        if (logger == null) {
            initAndSync();
            logger = loggerMap.get(loggerName);
            if (logger == null) {
                return;
            }
        }
        if (currentLog4jName.equals(SLF4J_LOG4J12)) {
            org.apache.log4j.Logger targetLogger = (org.apache.log4j.Logger) logger;
            org.apache.log4j.Level targetLevel = org.apache.log4j.Level.toLevel(loggerLevel);
            targetLogger.setLevel(targetLevel);
        } else if (SLF4J_SIMPLE.equals(currentLog4jName)) {
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
        } else if (currentLog4jName.equals(LOGBACK_CLASSIC)) {
            ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) logger;
            ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(loggerLevel);
            targetLogger.setLevel(targetLevel);
        }
    }

    public static void setLogLevel(String loggerLevel) {
        if (loggerLevel == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : loggerMap.entrySet()) {
            setLogLevel(entry.getKey(), loggerLevel.toLowerCase());
        }
    }

    public static void recovery(String loggerName) {
        if (loggerName == null) {
            return;
        }
        String loggerLevel = loggerLevelMap.get(loggerName);
        if (loggerLevel == null) {
            return;
        }
        setLogLevel(loggerName, loggerLevel.toLowerCase());
    }

    public static void recovery() {
        for (Map.Entry<String, Object> entry : loggerMap.entrySet()) {
            recovery(entry.getKey());
        }
    }

}

