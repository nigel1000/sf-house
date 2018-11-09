package sf.house.redis.test;

import lombok.extern.slf4j.Slf4j;
import sf.house.redis.base.Constants;
import sf.house.redis.base.SerializeEnum;
import sf.house.redis.domain.RedisKey;
import sf.house.redis.helper.SerializeHelper;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Slf4j
public class SerializeHelperTest {

    public static void main(String[] args) {

        RedisKey redisKey = RedisKey.createPrefix("redis");
        RedisKey before = redisKey.createByPrefix("redis key", 20);
        log.info("source:\t" + before);
        byte[] hessian = SerializeHelper.serialize(before);
        log.info("{}", hessian.length);
        RedisKey hessianRet = SerializeHelper.deserialize(hessian);
        log.info("{}", hessianRet);

        Constants.SERIALIZE_ENUM.set(SerializeEnum.JAVA);
        byte[] java = SerializeHelper.serialize(before);
        log.info("{}", java.length);
        RedisKey javaRet = SerializeHelper.deserialize(java);
        log.info("{}", javaRet);
    }

}
