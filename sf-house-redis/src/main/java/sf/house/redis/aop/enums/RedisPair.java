package sf.house.redis.aop.enums;

import lombok.Getter;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

public enum RedisPair {


    GET_SET_WITH_EXPIRE("get(RedisKey redisKey)", "setWithExpire(RedisKey redisKey, T object)"),
    GET_SET("get(RedisKey redisKey)", "set(RedisKey redisKey, T object)"),

    BATCH_GET_SET("get(RedisKey redisKey)", "set(RedisKey redisKey, T object)"),

    DELETE("", "remove(RedisKey redisKey)"),
    UPDATE("", "set(RedisKey redisKey, T object)"),
    BATCH_DELETE("", "remove(RedisKey redisKey)"),

    LOCK_RELEASE("lock(RedisKey redisKey)", "release(RedisKey redisKey)"),

    ;

    @Getter
    private String head;
    @Getter
    private String tail;

    RedisPair(String head, String tail) {
        this.head = head;
        this.tail = tail;
    }

}
