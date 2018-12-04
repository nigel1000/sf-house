package sf.house.redis.aop.handles;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import sf.house.aop.util.AspectUtil;
import sf.house.bean.excps.UnifiedException;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.base.RedisCacheMeta;
import sf.house.redis.aop.handles.base.RedisCacheHandler;
import sf.house.redis.base.Constants;
import sf.house.redis.client.base.DeployClient;
import sf.house.redis.domain.RedisKey;

import javax.annotation.Resource;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Component
@Slf4j
public class LockReleaseHandler implements RedisCacheHandler {

    @Resource
    private DeployClient deployClient;

    @Override
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        RedisKey redisKey = RedisKey.createKey(clazzMeta.getKey(), clazzMeta.getExpireTime());

        Object object;
        if (deployClient.lock(redisKey)) {
            Constants.logLock(Lists.newArrayList(redisKey.getKey()));
            try {
                object = AspectUtil.proceed(point);
            } finally {
                deployClient.release(redisKey);
                Constants.logRelease(Lists.newArrayList(redisKey.getKey()));
            }
        } else {
            throw UnifiedException.gen("分布式缓存锁", redisCache.failTips());
        }
        return object;

    }

}
