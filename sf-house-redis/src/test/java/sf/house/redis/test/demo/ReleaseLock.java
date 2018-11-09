package sf.house.redis.test.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.aop.enums.RedisPair;
import sf.house.redis.base.Constants;

@Component
@Slf4j
public class ReleaseLock {

    @RedisCache(redisPair = RedisPair.LOCK_RELEASE, expireTime = Constants.ONE_SECOND * 5, keyAuto = {0, 1})
    public boolean releaseLockAuto(int time, int hour) {
        log.info("enter auto lock release！");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RedisCache(redisPair = RedisPair.LOCK_RELEASE, expireTime = Constants.ONE_SECOND * 5, keyDiy = "diy")
    public boolean releaseLockDiy(int time, int hour) {
        log.info("enter diy lock release！");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

}
