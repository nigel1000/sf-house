package sf.house.aop.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
public class AspectUtil {

    private AspectUtil() {}

    /**
     * 方法上优先 类上次之
     * 
     * @param point
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotation(final JoinPoint point, Class<T> annotationClass) {

        if (point == null) {
            return null;
        }
        String methodName = point.getSignature().getName();
        Class<?> clazz = point.getTarget().getClass();
        Class[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
        // 必须用这种方式获得method
        // ((MethodSignature) pjp.getSignature()).getMethod() 无法通过接口拿到实现类方法上的注解
        Method method = null;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ignored) {
        }
        if (method == null) {
            return null;
        }
        return Optional.ofNullable(method.getAnnotation(annotationClass)).orElse(clazz.getAnnotation(annotationClass));
    }

    /**
     * 替换注解的值
     * 
     * @param packagePaths
     * @param methodName
     * @return
     */
    public static void genExecutionString(List<String> packagePaths, Class<?> clazz, String methodName) {
        StringBuilder sb = new StringBuilder();

        String replace = "{prefix_package}";
        String template = "execution(* " + replace + "..*.*(..))";
        if (packagePaths != null && packagePaths.size() != 0) {
            sb.append("&&(");
            int length = packagePaths.size();
            for (int i = 0; i < length; i++) {
                String packagePath = packagePaths.get(i);
                if (!StringUtils.isEmpty(packagePath)) {
                    sb.append(template.replace(replace, packagePath));
                    if (i != (length - 1)) {
                        sb.append("||");
                    }
                }
            }
            sb.append(")");

            Pointcut methodPointcut;
            try {
                methodPointcut = clazz.getMethod(methodName).getAnnotation(Pointcut.class);
            } catch (Exception e) {
                log.info("class:{},没有 method:{}", clazz.getName(), methodName);
                return;
            }
            if (methodPointcut == null) {
                log.info("class:{}, method:{} 没有 Pointcut 注解!", clazz.getName(), methodName);
                return;
            }
            String newValue = methodPointcut.value() + sb.toString();
            // 修改值
            InvocationHandler handler = Proxy.getInvocationHandler(methodPointcut);
            Field f;
            try {
                f = handler.getClass().getDeclaredField("memberValues");
                f.setAccessible(true);
                Map<String, Object> memberValues = (Map<String, Object>) f.get(handler);
                memberValues.put("value", newValue);
            } catch (Exception e) {
                throw new RuntimeException("[AuthAcyAop]  修改注解值错误!", e);
            }
            log.info("{} 的 point cut:{}", methodName, newValue);
        }
    }

}
