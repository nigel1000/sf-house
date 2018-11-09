package sf.house.mybatis.generator.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import sf.house.bean.excps.UnifiedException;

import java.io.IOException;
import java.util.Properties;

public class PropertiesLoad {

    public final static ThreadLocal<Properties> propertiesThreadLocal = new ThreadLocal<>();

    public static void init(String propertiesPath) {
        try {
            propertiesThreadLocal.set(PropertiesLoaderUtils.loadAllProperties(propertiesPath));
        } catch (IOException e) {
            String message = "PropertiesLoad load " + propertiesPath + " failed.";
            throw UnifiedException.gen(message, e);
        }
    }

    public static void init(Properties properties) {
        propertiesThreadLocal.set(properties);
        Constants.init();
    }

    public static void clear() {
        propertiesThreadLocal.remove();
    }

    public static String getByKey(String key, Boolean isValidNull) {
        if (propertiesThreadLocal.get() == null) {
            String message = "properties is null. please execute init first.";
            throw UnifiedException.gen(message);
        }
        String ret = propertiesThreadLocal.get().getProperty(key);
        if (isValidNull) {
            if (StringUtils.isEmpty(ret)) {
                String message = key + " is null in properties. ";
                throw UnifiedException.gen(message);
            }
        } else {
            if (ret == null) {
                ret = "";
            }
        }
        return ret;
    }

}
