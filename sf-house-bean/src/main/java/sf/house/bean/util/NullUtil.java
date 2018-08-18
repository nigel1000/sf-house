package sf.house.bean.util;

import sf.house.bean.excps.UnifiedException;

import java.util.function.Supplier;

/**
 * Created by hznijianfeng on 2018/8/17.
 */

public class NullUtil {

    private static final String module = "空校验工具";

    public static <T> T validThrow(Supplier<T> supplier, String message) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw UnifiedException.gen(module, message);
        }
    }

    public static <T> T validDefault(Supplier<T> supplier, T def) {
        try {
            return supplier.get();
        } catch (NullPointerException ex) {
            return def;
        }
    }

    // 校验是否为空 为空抛出异常
    public static <T> T validThrow(T obj, String message) {
        if (obj == null) {
            throw UnifiedException.gen(module, message);
        }
        return obj;
    }

    // 校验是否表达式是否为true 为false抛出异常
    public static void validThrow(Boolean b, String message) {
        if (!b) {
            throw UnifiedException.gen(module, message);
        }
    }

    // 校验是否为空 为空降级处理
    public static <T> T validDefault(T obj, T def) {
        if (obj == null) {
            return def;
        }
        return obj;
    }

}
