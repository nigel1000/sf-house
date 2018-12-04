package sf.house.redis.aop.handles;

import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import sf.house.aop.util.AspectUtil;
import sf.house.bean.util.TypeUtil;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.base.RedisCacheMeta;
import sf.house.redis.aop.handles.base.RedisCacheHandler;
import sf.house.redis.base.Constants;
import sf.house.redis.client.base.DeployClient;
import sf.house.redis.domain.RedisKey;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Component
public class GetSetWithExpireHandler implements RedisCacheHandler {

    @Resource
    private DeployClient deployClient;

    @Override
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        if (!TypeUtil.isAssignableFrom(Serializable.class, clazzMeta.getReturnType())
                && !clazzMeta.getReturnType().isPrimitive()) {
            return AspectUtil.proceed(point);
        }
        RedisKey redisKey = RedisKey.createKey(clazzMeta.getKey(), clazzMeta.getExpireTime());
        Object cache = deployClient.get(redisKey);
        if (cache == null) {
            Object result = AspectUtil.proceed(point);
            deployClient.setWithExpire(redisKey, (Serializable) result);
            return result;
        }
        Constants.logGet(Lists.newArrayList(redisKey.getKey()));
        return AspectUtil.setProceedResult(() -> cache);
    }
}
