package sf.house.redis.aop.handles;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
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

        Object result = point.proceed();
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        String key = clazzMeta.getKey();
        if (StringUtils.isBlank(key)) {
            return result;
        }
        // 更新缓存
        deployClient.setWithExpire(RedisKey.createKey(key, clazzMeta.getExpireTime()), (Serializable) result);
        Constants.logUpdate(Lists.newArrayList(key));
        return result;
    }

}
