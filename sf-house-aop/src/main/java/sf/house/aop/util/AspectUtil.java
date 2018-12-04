package sf.house.aop.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import sf.house.bean.functions.SupplierThrow;
import sf.house.bean.util.TypeUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
public class AspectUtil {

    private AspectUtil() {}

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

    private static ThreadLocal<Object> joinPointResult = new ThreadLocal<>();
    private static ThreadLocal<Integer> hasJoinPointResult = new ThreadLocal<>();

    // 保证 point.proceed() 只执行一次
    public static Object proceed(final ProceedingJoinPoint point) throws Throwable {
        Integer flag = hasJoinPointResult.get();
        if (flag == null) {
            hasJoinPointResult.set(1);
            joinPointResult.set(point.proceed());
        }
        return joinPointResult.get();
    }

    public static <E extends Throwable> Object setProceedResult(SupplierThrow<Object, E> supplier) throws E {
        joinPointResult.set(supplier.get());
        return joinPointResult.get();
    }

    public static Object getProceedResult() {
        return joinPointResult.get();
    }

    public static void clearProceedResult() {
        hasJoinPointResult.remove();
        joinPointResult.remove();
    }

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
