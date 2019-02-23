package sf.house.aop.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import sf.house.bean.functions.SupplierThrow;
import sf.house.bean.util.TypeUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
public class AspectUtil {

    private AspectUtil() {
    }

    /**
     * 递归查找目标类的所有子类的注解
     * 方法上优先，类上次之，找到即返回
     */
    public static <T extends Annotation> T recursionGetAnnotation(final JoinPoint point, Class<T> annotationClass) {

        if (point == null) {
            return null;
        }

        List<Class> classes = TypeUtil.getSuperclasses(point.getTarget().getClass());
        T result = null;
        T candidate = null;
        for (Class<?> clazz : classes) {
            Method method = getMethod(point);
            if (method != null) {
                result = method.getAnnotation(annotationClass);
                if (result == null) {
                    result = clazz.getAnnotation(annotationClass);
                }
                if (result != null) {
                    break;
                }
            } else {
                candidate = clazz.getAnnotation(annotationClass);
            }
        }
        return Optional.ofNullable(result).orElse(candidate);
    }

    /**
     * 方法上优先 类上次之
     */
    public static <T extends Annotation> T getAnnotation(final JoinPoint point, Class<T> annotationClass) {
        if (point == null) {
            return null;
        }
        Class<?> clazz = point.getTarget().getClass();
        Method method = getMethod(point);
        if (method == null) {
            return null;
        }
        return Optional.ofNullable(method.getAnnotation(annotationClass)).orElse(clazz.getAnnotation(annotationClass));
    }

    public static <T extends Annotation> T[] getAnnotationsByType(final JoinPoint point, Class<T> annotationClass) {
        if (point == null) {
            return null;
        }
        Class<?> clazz = point.getTarget().getClass();
        Method method = getMethod(point);
        if (method == null) {
            return null;
        }
        return Optional.ofNullable(method.getAnnotationsByType(annotationClass))
                .orElse(clazz.getAnnotationsByType(annotationClass));
    }

    public static Method getMethod(final JoinPoint point) {
        Class<?> clazz = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
        // 必须用这种方式获得method
        // ((MethodSignature) pjp.getSignature()).getMethod() 无法通过接口拿到实现类方法上的注解
        Method method = null;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ignored) {
        }
        return method;
    }

    ///////////////////////////////////////////////
    private static ThreadLocal<Map<String, Object>> joinPointResultMap = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Integer>> hasJoinPointResultMap = new ThreadLocal<>();
    private static final Integer FLAG = 1;

    // 保证 point.proceed() 只执行一次
    public static Object proceed(final ProceedingJoinPoint point) throws Throwable {
        String key = point.toString();
        Map<String, Object> resultMap = Optional.ofNullable(joinPointResultMap.get()).orElse(Maps.newHashMap());
        Map<String, Integer> hasResultMap = Optional.ofNullable(hasJoinPointResultMap.get()).orElse(Maps.newHashMap());
        if (MapUtils.isEmpty(hasResultMap) || hasResultMap.get(key) == null) {
            resultMap.putIfAbsent(key, point.proceed());
            hasResultMap.putIfAbsent(key, FLAG);
            hasJoinPointResultMap.set(hasResultMap);
            joinPointResultMap.set(resultMap);
        }
        return resultMap.get(key);
    }

    public static <E extends Throwable> Object setProceedResult(SupplierThrow<Object, E> supplier,
                                                                final ProceedingJoinPoint point) throws E {
        String key = point.toString();
        Object result = supplier.get();
        Map<String, Object> resultMap = Optional.ofNullable(joinPointResultMap.get()).orElse(Maps.newHashMap());
        resultMap.put(key, result);
        joinPointResultMap.set(resultMap);
        return result;
    }

    public static Object getProceedResult(final ProceedingJoinPoint point) {
        return Optional.ofNullable(joinPointResultMap.get()).orElse(Maps.newHashMap()).get(point.toString());
    }

    public static void clearProceedResult(final ProceedingJoinPoint point) {
        String key = point.toString();
        Map<String, Object> resultMap = Optional.ofNullable(joinPointResultMap.get()).orElse(Maps.newHashMap());
        Map<String, Integer> hasResultMap = Optional.ofNullable(hasJoinPointResultMap.get()).orElse(Maps.newHashMap());
        resultMap.remove(key);
        hasResultMap.remove(key);
        if (MapUtils.isEmpty(resultMap)) {
            joinPointResultMap.remove();
        }
        if (MapUtils.isEmpty(hasResultMap)) {
            hasJoinPointResultMap.remove();
        }
    }
    ///////////////////////////////////////////////

    /**
     * 替换注解的值
     */
//    public static void genExecutionString(List<String> packagePaths, Class<?> clazz, String methodName) {
//        StringBuilder sb = new StringBuilder();
//
//        String replace = "{prefix_package}";
//        String template = "execution(* " + replace + "..*.*(..))";
//        if (packagePaths != null && packagePaths.size() != 0) {
//            sb.append("&&(");
//            int length = packagePaths.size();
//            for (int i = 0; i < length; i++) {
//                String packagePath = packagePaths.get(i);
//                if (!StringUtils.isEmpty(packagePath)) {
//                    sb.append(template.replace(replace, packagePath));
//                    if (i != (length - 1)) {
//                        sb.append("||");
//                    }
//                }
//            }
//            sb.append(")");
//
//            Pointcut methodPointcut;
//            try {
//                methodPointcut = clazz.getMethod(methodName).getAnnotation(Pointcut.class);
//            } catch (Exception e) {
//                log.info("class:{},没有 method:{}", clazz.getName(), methodName);
//                return;
//            }
//            if (methodPointcut == null) {
//                log.info("class:{}, method:{} 没有 Pointcut 注解!", clazz.getName(), methodName);
//                return;
//            }
//            String newValue = methodPointcut.value() + sb.toString();
//            // 修改值
//            InvocationHandler handler = Proxy.getInvocationHandler(methodPointcut);
//            Field f;
//            try {
//                f = handler.getClass().getDeclaredField("memberValues");
//                f.setAccessible(true);
//                Map<String, Object> memberValues = (Map<String, Object>) f.get(handler);
//                memberValues.put("value", newValue);
//            } catch (Exception e) {
//                throw new RuntimeException("[AuthAcyAop]  修改注解值错误!", e);
//            }
//            log.info("{} 的 point cut:{}", methodName, newValue);
//        }
//    }

}
