package sf.house.redis.test.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.enums.RedisPair;

/**
 * Created by hznijianfeng on 2018/9/7.
 */

@Component
@Slf4j
public class Delete {

    @RedisCache(redisPair = RedisPair.DELETE, keyAuto = {0, 1},
            keyDiy = "sf.house.redis.test.demo.GetSetWithExpire_getSetWithExpireAuto")
    public void delete(int time, int hour) {
        log.info("enter delete");
    }

}
