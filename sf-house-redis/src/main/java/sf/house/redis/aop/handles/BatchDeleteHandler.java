package sf.house.redis.aop.handles;

import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import sf.house.bean.util.FunctionUtil;
import sf.house.bean.util.TypeUtil;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.base.RedisCacheMeta;
import sf.house.redis.aop.handles.base.RedisCacheHandler;
import sf.house.redis.base.Constants;
import sf.house.redis.client.base.DeployClient;
import sf.house.redis.domain.RedisKey;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Component
public class BatchDeleteHandler implements RedisCacheHandler {

    @Resource
    private DeployClient deployClient;

    @Override
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {

        Object result = point.proceed();
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        if (redisCache.keyAuto().length != 1 || clazzMeta.getArgs().length < 1) {
            return result;
        }
        if (!TypeUtil.isAssignableFrom(List.class, clazzMeta.getArgs()[redisCache.keyAuto()[0]].getClass())) {
            return result;
        }
        String keyPrefix = clazzMeta.getKeyDiy();
        Set<Object> keys = Sets.newHashSet((List<Object>) (clazzMeta.getArgs()[redisCache.keyAuto()[0]]));
        if (CollectionUtils.isNotEmpty(keys)) {
            // 批量删除缓存
            List<String> cacheKeys = FunctionUtil.valueList(keys, (t) -> keyPrefix + String.valueOf(t) + "_");
            deployClient.remove(FunctionUtil.valueList(cacheKeys, RedisKey::createKey));
            Constants.logDel(cacheKeys);
        }
        return result;
    }

}
