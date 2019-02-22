package sf.house.aop.order;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by hznijianfeng on 2019/2/22.
 */
@Aspect
@Order(value = 3)
@Slf4j
@Component
public class Aop3 {

    @Pointcut("execution(public * sf.house.aop.order.demo..*.*(..))")
    public void order() {}

    @Around("order()")
    public Object around(final ProceedingJoinPoint point) throws Throwable {
        log.info(this.getClass().getName()+"  begin");
        try{
            Object proceed = point.proceed();
            log.info(this.getClass().getName()+"  end");
            return proceed;
        }catch (Exception ex){
            log.info(this.getClass().getName()+"  exception");
        }finally {
            log.info(this.getClass().getName()+"  finally");
            return null;
        }
    }


}
