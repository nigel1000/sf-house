package sf.house.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sf.house.aop.annotation.EasyLog;
import sf.house.aop.constants.LogConstant;
import sf.house.aop.util.AspectUtil;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Aspect
@Component
@Order(value = 2)
@Slf4j
public class EasyLogAspectJ {

    // public EasyLogAspectJ(List<String> packagePaths) {
    // // 静态织入，故而修改了Pointcut的值也不生效。
    // AspectUtil.genExecutionString(Lists.newArrayList("sf.house.aop.util"),
    // EasyLogAspectJ.class, "clazz");
    // AspectUtil.genExecutionString(Lists.newArrayList("sf.house.aop.util"),
    // EasyLogAspectJ.class,
    // "method");
    // }

    @Pointcut("@within(sf.house.aop.annotation.EasyLog)")
    public void clazz() {}

    @Pointcut("@annotation(sf.house.aop.annotation.EasyLog)")
    public void method() {}

    @Before("clazz() || method()")
    public void before(final JoinPoint point) {

        EasyLog easyLog = AspectUtil.getAnnotation(point, EasyLog.class);
        String module = easyLog.module();
        log.info(LogConstant.START_LOG_PREFIX + " args:{}", module, point.getTarget().getClass().getName(),
                point.getSignature().getName(), LogConstant.getObjString(point.getArgs()));
    }

    @AfterReturning(returning = "rtObj", value = "clazz() || method()")
    public void afterReturning(final JoinPoint point, final Object rtObj) {

        EasyLog easyLog = AspectUtil.getAnnotation(point, EasyLog.class);
        String module = easyLog.module();
        log.info(LogConstant.FINISH_LOG_PREFIX + " return:{}", module, point.getTarget().getClass().getName(),
                point.getSignature().getName(), LogConstant.getObjString(rtObj));
    }

}

