package sf.house.redis.client.base;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import sf.house.redis.domain.RedisKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

public interface DeployClient {

    Object pull();

    default void push(JedisCommands commands) {
        if (commands == null) {
            return;
        }
        if (commands instanceof Jedis) {
            ((Jedis) commands).close();
        }
    }

    <T extends Serializable> void set(RedisKey redisKey, T object);

    default <T extends Serializable> void batchSet(Map<RedisKey, T> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<RedisKey, T> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    <T extends Serializable> boolean setIfNotExist(RedisKey redisKey, T object);

    <T extends Serializable> void setWithExpire(RedisKey redisKey, T object);

    default <T extends Serializable> void batchSetWithExpire(Map<RedisKey, T> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<RedisKey, T> entry : map.entrySet()) {
            setWithExpire(entry.getKey(), entry.getValue());
        }
    }

    <T> T get(RedisKey redisKey);

    default <T> Map<RedisKey, T> batchGet(List<RedisKey> redisKeys) {
        Map<RedisKey, T> result = new HashMap<>();
        if (redisKeys == null) {
            return result;
        }
        for (RedisKey redisKey : redisKeys) {
            Object cache = get(redisKey);
            if (cache != null) {
                result.put(redisKey, get(redisKey));
            }
        }
        return result;
    }

    void remove(List<RedisKey> redisKey);

    // 解锁和获取锁只要key一样就行
    boolean lock(RedisKey redisKey);

    void release(RedisKey redisKey);

    // 解锁和获取的 bizCode 必须一样
    boolean lockWithBiz(RedisKey redisKey, String bizCode);

    void releaseWithBiz(RedisKey redisKey, String bizCode);

}
