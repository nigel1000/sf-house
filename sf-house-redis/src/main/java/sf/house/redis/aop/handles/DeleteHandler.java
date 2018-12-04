package sf.house.redis.aop.handles;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
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
public class DeleteHandler implements RedisCacheHandler {

    @Resource
    private DeployClient deployClient;

    @Override
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {

        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        if (StringUtils.isBlank(clazzMeta.getKeyPrefix())) {
            throw UnifiedException.gen("keyPrefix 不能为空");
        }
        Object result = AspectUtil.proceed(point);
        // 删除缓存
        String key = clazzMeta.getKey();
        deployClient.remove(Lists.newArrayList(RedisKey.createKey(key)));
        Constants.logDel(Lists.newArrayList(key));
        return result;
    }

}
