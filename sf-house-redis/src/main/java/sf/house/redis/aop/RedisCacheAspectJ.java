package sf.house.redis.aop;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sf.house.aop.util.AspectUtil;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.enums.RedisPair;
import sf.house.redis.aop.handles.base.RedisCacheHandler;
import sf.house.redis.aop.handles.base.RedisHandlerFactory;
import sf.house.redis.base.Constants;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

@Aspect
@Component
@Order(value = 1)
@Slf4j
public class RedisCacheAspectJ {

    @Pointcut("@within(sf.house.redis.aop.annotation.RedisCache)||" +
            "@within(sf.house.redis.aop.annotation.RedisCaches)")
    public void clazz() {
    }

    @Pointcut("@annotation(sf.house.redis.aop.annotation.RedisCache)||" +
            "@annotation(sf.house.redis.aop.annotation.RedisCaches)")
    public void method() {
    }

    @Resource
    private RedisHandlerFactory redisHandlerFactory;

    @Around("clazz() || method()")
    public Object around(final ProceedingJoinPoint point) throws Throwable {
        try {
            RedisCache[] redisCaches = AspectUtil.getAnnotationsByType(point, RedisCache.class);
            if (!isAvailable(redisCaches)) {
                return AspectUtil.proceed(point);
            }
            for (RedisCache redisCache : redisCaches) {
                RedisCacheHandler handler = redisHandlerFactory.createHandler(redisCache.redisPair());
                Constants.SERIALIZE_ENUM.set(redisCache.serializeEnum());
                handler.handle(point, redisCache);
                Constants.SERIALIZE_ENUM.remove();
            }
            return AspectUtil.getProceedResult();
        } finally {
            AspectUtil.clearProceedResult();
        }
    }

    private static List<RedisPair> unSupportedMulti = Lists.newArrayList(
            RedisPair.BATCH_GET_SET,
            RedisPair.GET_SET_WITH_EXPIRE,
            RedisPair.GET_SET,
            RedisPair.UPDATE);

    private boolean isAvailable(RedisCache[] redisCaches) {
        if (redisCaches == null) {
            return false;
        }
        int length = redisCaches.length;
        if (length <= 0) {
            return false;
        } else if (length == 1) {
            return true;
        } else {
            for (RedisCache redisCache : redisCaches) {
                if (unSupportedMulti.contains(redisCache.redisPair())) {
                    return false;
                }
            }
        }
        return true;
    }

}
