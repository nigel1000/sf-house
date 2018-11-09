package sf.house.aop.constants;

import com.google.common.base.Throwables;
import sf.house.aop.util.JsonUtil;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class LogConstant {

    // [module] class.method
    public static final String LOG_PREFIX = "[{}] {}.{} ";
    public static final String START_LOG_PREFIX = LOG_PREFIX + "start.";
    public static final String FINISH_LOG_PREFIX = LOG_PREFIX + "finish.";
    public static final String EXCP_LOG_PREFIX = LOG_PREFIX + "exception.";

    public static String getObjString(Object obj) {

        try {
            if (obj == null) {
                return "null";
            }
            Class<?> clazz = obj.getClass();
            // 数组||集合类直接返回json解析
            if (clazz.isArray() || obj instanceof Collection || obj instanceof Map) {
                return JsonUtil.bean2json(obj);
            }
            // 支持重写toString()
            Method method = null;
            try {
                method = clazz.getMethod("toString");
            } catch (NoSuchMethodException ignored) {
            }
            if (method == null) {
                return "null";
            }
            Class<?> declaringClass = method.getDeclaringClass();
            if (Object.class != declaringClass) {
                // 重写了toString()
                return String.valueOf(obj);
            }
            return JsonUtil.bean2json(obj);
        } catch (Exception e) {
            return "json 序列化失败. " + Throwables.getStackTraceAsString(e);
        }
    }

}
