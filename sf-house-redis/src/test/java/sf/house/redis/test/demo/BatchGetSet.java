package sf.house.redis.test.demo;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.enums.RedisPair;
import sf.house.redis.base.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/9/3.
 */

@Component
@Slf4j

public class BatchGetSet {

    // key的类型和返回的map类型一样
    @RedisCache(redisPair = RedisPair.BATCH_GET_SET, expireTime = Constants.ONE_SECOND * 5, keyAuto = {0})
    public Map<Integer, Integer> getBatchGetSet(List<Integer> keys, int hour) {
        log.info("enter getBatchGetSet！{}", keys);
        Map<Integer, Integer> ret = Maps.newHashMap();
        for (Integer key : keys) {
            ret.put(key, 1);
        }
        return ret;
    }

    // key的类型和返回的map类型不一样 key.toString 和 Map里的key必须一样才有作用
    @RedisCache(redisPair = RedisPair.BATCH_GET_SET, keyDiy = "getBatchGetSetDiffDiy",
            expireTime = Constants.ONE_SECOND * 5, keyAuto = {0})
    public Map<String, Integer> getBatchGetSetDiff(List<Integer> keys, int hour) {
        log.info("enter getBatchGetSetDiff！ {}", keys);
        Map<String, Integer> ret = Maps.newHashMap();
        for (Integer key : keys) {
            ret.put(key.toString(), 1);
        }
        return ret;
    }

}
