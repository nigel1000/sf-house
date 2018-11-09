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

    @RedisCache(redisPair = RedisPair.BATCH_DELETE, keyAuto = {0},
            keyDiy = "sf.house.redis.test.demo.BatchGetSet_getBatchGetSet")
    public void batchDelete(List<Integer> keys) {
        log.info("enter batchDelete！ {}", keys);
    }

}
