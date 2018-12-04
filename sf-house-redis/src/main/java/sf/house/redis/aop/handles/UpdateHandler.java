package sf.house.redis.aop.handles;

import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import sf.house.aop.util.AspectUtil;
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
public class UpdateHandler implements RedisCacheHandler {

    @Resource
    private DeployClient deployClient;

    @Override
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {

        Object result = AspectUtil.proceed(point);
        // 更新缓存
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        String key = clazzMeta.getKey();
        deployClient.setWithExpire(RedisKey.createKey(key, clazzMeta.getExpireTime()), (Serializable) result);
        Constants.logUpdate(Lists.newArrayList(key));
        return result;
    }

}
