package sf.house.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sf.house.aop.annotation.TraceId;
import sf.house.aop.util.AspectUtil;
import sf.house.trace.common.TraceIdUtil;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Aspect
@Component
@Order(value = 1)
@Slf4j
public class TraceIdAspectJ {

    @Pointcut("@within(sf.house.aop.annotation.TraceId)")
    public void clazz() {}

    @Pointcut("@annotation(sf.house.aop.annotation.TraceId)")
    public void method() {}

    @Before("clazz() || method()")
    public void before(final JoinPoint point) {

        TraceIdUtil.initTraceIdIfAbsent();
    }

    @AfterReturning("clazz() || method()")
    public void afterReturning(final JoinPoint point) {

        TraceId traceId = AspectUtil.getAnnotation(point, TraceId.class);
        if (traceId.needClear()) {
            TraceIdUtil.clearTraceId();
        }

    }

}

