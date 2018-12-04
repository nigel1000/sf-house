package sf.house.redis.test.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.enums.RedisPair;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/9/7.
 */

@Component
@Slf4j
public class BatchDelete {

    @RedisCache(redisPair = RedisPair.BATCH_DELETE, keySuffixIndex = {0},
            keyPrefix = "sf.house.redis.test.demo.BatchGetSet_getBatchGetSet")
    public void getBatchGetSet(List<Integer> keys) {
        log.info("enter batchDelete getBatchGetSet！ {}", keys);
    }

    @RedisCache(redisPair = RedisPair.BATCH_DELETE, keySuffixIndex = {0}, keyPrefix = "getBatchGetSetDiffDiy")
    public void getBatchGetSetDiff(List<Integer> keys) {
        log.info("enter batchDelete getBatchGetSetDiff！ {}", keys);
    }

    @RedisCache(redisPair = RedisPair.BATCH_DELETE, keySuffixIndex = {0},
            keyPrefix = "sf.house.redis.test.demo.BatchGetSet_getBatchGetSet")
    @RedisCache(redisPair = RedisPair.BATCH_DELETE, keySuffixIndex = {0}, keyPrefix = "getBatchGetSetDiffDiy")
    public void deleteTwo(List<Integer> keys) {
        log.info("enter deleteTwo！ {}", keys);
    }

}
