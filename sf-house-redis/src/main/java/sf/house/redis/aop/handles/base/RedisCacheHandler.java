package sf.house.redis.aop.handles.base;

import org.aspectj.lang.ProceedingJoinPoint;
import sf.house.redis.aop.annotation.RedisCache;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

public interface RedisCacheHandler {

    Object handle(final ProceedingJoinPoint point, RedisCache redisCache) throws Throwable;

}
