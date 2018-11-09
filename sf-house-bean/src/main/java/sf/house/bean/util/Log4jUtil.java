package sf.house.bean.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.impl.StaticLoggerBinder;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.log4j.LogbackClassicUtil;
import sf.house.bean.util.log4j.Slf4jLog4j12Util;
import sf.house.bean.util.log4j.Slf4jSimpleUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Slf4jLog4j12Util.fillLoggerInfo(loggerMap, loggerLevelMap);
        } else if (SLF4J_SIMPLE.equals(currentLog4jName)) {
            Slf4jSimpleUtil.fillLoggerInfo(loggerMap, loggerLevelMap);
        } else if (LOGBACK_CLASSIC.equals(currentLog4jName)) {
            LogbackClassicUtil.fillLoggerInfo(loggerMap, loggerLevelMap);
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
            Slf4jLog4j12Util.setLoggerLevel(logger, loggerLevel);
        } else if (SLF4J_SIMPLE.equals(currentLog4jName)) {
            Slf4jSimpleUtil.setLoggerLevel(logger, loggerLevel);
        } else if (currentLog4jName.equals(LOGBACK_CLASSIC)) {
            LogbackClassicUtil.setLoggerLevel(logger, loggerLevel);
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

