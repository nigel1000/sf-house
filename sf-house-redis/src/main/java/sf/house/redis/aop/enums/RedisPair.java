package sf.house.redis.aop.enums;

import lombok.Getter;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

public enum RedisPair {


    // single get set
    GET_SET_WITH_EXPIRE("get(RedisKey redisKey)", "setWithExpire(RedisKey redisKey, T object)"),
    GET_SET("get(RedisKey redisKey)", "set(RedisKey redisKey, T object)"),

    // batch get set
    // keyAuto 必须是List<Key>的下标，keyAuto 长度只能为1，不能是diy的
    // 返回必须是Map<Key, Value>
    // List<Key> 为空不取缓存
    // Map<Key, Value> 为空不存缓存
    BATCH_GET_SET("get(RedisKey redisKey)", "set(RedisKey redisKey, T object)"),

    // delete
    // keyDiy + "_" + keyAuto
    DELETE("", "remove(RedisKey redisKey)"),
    UPDATE("", "set(RedisKey redisKey, T object)"),
    // keyAuto 必须是List<Key>的下标，keyAuto 长度只能为1
    BATCH_DELETE("", "remove(RedisKey redisKey)"),

    // distributed lock
    // 未申请到锁抛出异常,若业务需要通知请catch并换成业务性的提示
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
