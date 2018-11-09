package sf.house.redis.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sf.house.aop.util.AspectUtil;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.handles.base.RedisCacheHandler;
import sf.house.redis.aop.handles.base.RedisHandlerFactory;
import sf.house.redis.base.Constants;

import javax.annotation.Resource;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

@Aspect
@Component
@Order(value = 1)
@Slf4j
public class RedisCacheAspectJ {

    @Pointcut("@within(sf.house.redis.aop.annotation.RedisCache)")
    public void clazz() {}

    @Pointcut("@annotation(sf.house.redis.aop.annotation.RedisCache)")
    public void method() {}

    @Resource
    private RedisHandlerFactory redisHandlerFactory;

    @Around("clazz() || method()")
    public Object around(final ProceedingJoinPoint point) throws Throwable {
        RedisCache redisCache = AspectUtil.getAnnotation(point, RedisCache.class);
        RedisCacheHandler handler = redisHandlerFactory.createHandler(redisCache.redisPair());
        if (handler == null) {
            return point.proceed();
        } else {
            Constants.SERIALIZE_ENUM.set(redisCache.serializeEnum());
            Object result = handler.handle(point, redisCache);
            Constants.SERIALIZE_ENUM.remove();
            return result;
        }
    }

}
