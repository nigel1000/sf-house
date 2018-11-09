package sf.house.redis.test.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.enums.RedisPair;
import sf.house.redis.base.Constants;

/**
 * Created by hznijianfeng on 2018/9/7.
 */

@Component
@Slf4j
public class GetSetWithExpire {

    @RedisCache(redisPair = RedisPair.GET_SET_WITH_EXPIRE, expireTime = Constants.ONE_SECOND * 5, keyAuto = {0, 1})
    public boolean getSetWithExpireAuto(int time, int hour) {
        log.info("enter getPutWithExpireAuto");
        return true;
    }

    @RedisCache(redisPair = RedisPair.GET_SET_WITH_EXPIRE, expireTime = Constants.ONE_SECOND * 5, keyDiy = "diy")
    public boolean getSetWithExpireDiy(int time, int hour) {
        log.info("enter getPutWithExpireDiy");
        return true;
    }

}
