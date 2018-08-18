package sf.house.aop.util;

import com.google.common.base.Throwables;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class SerializeUtil {

    private SerializeUtil() {}

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

    // java序列化
    public static byte[] javaSerialize(Object object) {

        ObjectOutputStream oos;
        ByteArrayOutputStream baos;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {

        }
        return null;
    }

    // java反序列化
    public static Object javaDeserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }

}
