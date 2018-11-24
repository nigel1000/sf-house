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

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Component
public class DeleteHandler implements RedisCacheHandler {

    @Resource
    private DeployClient deployClient;

    @Override
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {

        Object result = point.proceed();
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        // keyDiy 不能为空
        if (StringUtils.isBlank(clazzMeta.getKeyDiy())) {
            return result;
        }
        String key = clazzMeta.getKey();
        if (StringUtils.isBlank(key)) {
            return result;
        }
        // 删除缓存
        deployClient.remove(Lists.newArrayList(RedisKey.createKey(key)));
        Constants.logDel(Lists.newArrayList(key));
        return result;
    }

}
