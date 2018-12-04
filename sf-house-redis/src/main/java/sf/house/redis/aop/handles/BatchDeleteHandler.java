package sf.house.redis.aop.handles;

import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import sf.house.aop.util.AspectUtil;
import sf.house.bean.excps.UnifiedException;
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
    @SuppressWarnings("unchecked")
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {

        Object result = AspectUtil.proceed(point);
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        if (clazzMeta.getKeySuffixIndex().length != 1 || clazzMeta.getArgs().length < 1 ||
                StringUtils.isBlank(clazzMeta.getKeyPrefix())) {
            throw UnifiedException.gen("keyPrefix 不能为空,kySuffixIndex 的大小必须等于1,入参数量必须大于0");
        }
        if (!TypeUtil.isAssignableFrom(List.class, clazzMeta.getArgs()[clazzMeta.getKeySuffixIndex()[0]].getClass())) {
            throw UnifiedException.gen("入参 args[" + clazzMeta.getKeySuffixIndex()[0] + "] 必须是 List");
        }
        String keyPrefix = clazzMeta.getPrefixKey();
        Set<Object> keys = Sets.newHashSet((List<Object>) (clazzMeta.getArgs()[clazzMeta.getKeySuffixIndex()[0]]));
        if (CollectionUtils.isNotEmpty(keys)) {
            // 批量删除缓存
            List<String> cacheKeys = FunctionUtil.valueList(keys, (t) -> keyPrefix + String.valueOf(t) + "_");
            deployClient.remove(FunctionUtil.valueList(cacheKeys, RedisKey::createKey));
            Constants.logDel(cacheKeys);
        }
        return result;
    }

}
