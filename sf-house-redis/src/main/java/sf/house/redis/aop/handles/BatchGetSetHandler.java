package sf.house.redis.aop.handles;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import sf.house.aop.util.AspectUtil;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.TypeUtil;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.base.RedisCacheMeta;
import sf.house.redis.aop.handles.base.RedisCacheHandler;
import sf.house.redis.base.Constants;
import sf.house.redis.client.base.DeployClient;
import sf.house.redis.domain.RedisKey;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hznijianfeng on 2018/9/3.
 */

@Component
public class BatchGetSetHandler implements RedisCacheHandler {

    @Resource
    private DeployClient deployClient;
    @Resource
    private GetSetWithExpireHandler getSetWithExpireHandler;

    @Override
    @SuppressWarnings("unchecked")
    public Object handle(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {
        RedisCacheMeta clazzMeta = new RedisCacheMeta(point, redisCache);
        if (clazzMeta.getKeySuffixIndex().length != 1 || clazzMeta.getArgs().length < 1) {
            throw UnifiedException.gen("keySuffixIndex 的大小必须等于1,入参数量必须大于0");
        }
        if (!TypeUtil.isAssignableFrom(List.class, clazzMeta.getArgs()[clazzMeta.getKeySuffixIndex()[0]].getClass())) {
            throw UnifiedException.gen("入参 args[" + clazzMeta.getKeySuffixIndex()[0] + "] 必须是 List");
        }
        if (!TypeUtil.isAssignableFrom(Map.class, clazzMeta.getReturnType())) {
            throw UnifiedException.gen("返回类型必须是 Map");
        }
        Set<Object> keys = Sets.newHashSet((List<Object>) (clazzMeta.getArgs()[clazzMeta.getKeySuffixIndex()[0]]));
        // 如果List<key>为空，则走getPutWithExpireHandler
        if (CollectionUtils.isEmpty(keys)) {
            return getSetWithExpireHandler.handle(point, redisCache);
        }
        Map<Object, Object> ret = Maps.newHashMap();
        String keyPrefix = clazzMeta.getPrefixKey();
        List<RedisKey> redisKeys = Lists.newArrayList();
        for (Object key : keys) {
            redisKeys.add(RedisKey.createKey(keyPrefix + String.valueOf(key) + "_"));
        }
        // from cache
        List<String> cacheKeys = Lists.newArrayList();
        Map<RedisKey, Object> fromCache = deployClient.batchGet(redisKeys);
        if (MapUtils.isNotEmpty(fromCache)) {
            for (Object key : keys) {
                RedisKey redisKey = RedisKey.createKey(keyPrefix + String.valueOf(key) + "_");
                Object cacheObj = fromCache.get(redisKey);
                if (cacheObj != null) {
                    ret.putIfAbsent(key, cacheObj);
                    cacheKeys.add(redisKey.getKey());
                }
            }
            Constants.logGet(cacheKeys);
        }
        // from business
        if (keys.size() != fromCache.entrySet().size()) {
            List<Object> noCache = Lists.newArrayList();
            // collect no cache data
            for (Object key : keys) {
                if (!cacheKeys.contains(keyPrefix + String.valueOf(key) + "_")) {
                    noCache.add(key);
                }
            }
            // get no cache data
            if (CollectionUtils.isNotEmpty(noCache)) {
                clazzMeta.getArgs()[clazzMeta.getKeySuffixIndex()[0]] = noCache;
                Map<Object, Object> fromBiz = (Map<Object, Object>) clazzMeta.getPoint().proceed(clazzMeta.getArgs());
                // put cache
                if (MapUtils.isNotEmpty(fromBiz)) {
                    ret.putAll(fromBiz);
                    Map<RedisKey, Serializable> cacheMap = Maps.newHashMap();
                    for (Map.Entry<Object, Object> fromBizEntry : fromBiz.entrySet()) {
                        cacheMap.put(RedisKey.createKey(keyPrefix + String.valueOf(fromBizEntry.getKey()) + "_",
                                clazzMeta.getExpireTime()), (Serializable) fromBizEntry.getValue());
                    }
                    deployClient.batchSetWithExpire(cacheMap);
                }
            }
        }
        return AspectUtil.setProceedResult(() -> ret, point);
    }

}
