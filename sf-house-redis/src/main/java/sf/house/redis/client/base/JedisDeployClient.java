package sf.house.redis.client.base;

import com.google.common.collect.Lists;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;
import sf.house.aop.annotation.PreDefine;
import sf.house.aop.define.enums.RunType;
import sf.house.redis.base.Constants;
import sf.house.redis.domain.RedisKey;
import sf.house.redis.enums.EXPXEnum;
import sf.house.redis.enums.NXXXEnum;
import sf.house.redis.helper.SerializeHelper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@PreDefine(runType = {RunType.VALID})
public abstract class JedisDeployClient extends AbstractDeployClient {

    protected Pool<Jedis> pool;

    @Override
    public Jedis pull() {
        return pool.getResource();
    }

    @Override
    public <T extends Serializable> void set(RedisKey redisKey, T object) {
        Jedis jedis = pull();
        jedis.set(redisKey.getKey().getBytes(), SerializeHelper.serialize(object));
        push(jedis);
    }

    @Override
    public <T extends Serializable> boolean setIfNotExist(RedisKey redisKey, T object) {
        Jedis jedis = pull();
        String ret = jedis.set(redisKey.getKey().getBytes(), SerializeHelper.serialize(object),
                NXXXEnum.NX.getCode().getBytes(), EXPXEnum.EX.getCode().getBytes(), redisKey.getExpireTime());
        push(jedis);
        if (Constants.RETURN_OK.equals(ret)) {
            return true;
        }
        return false;
    }

    @Override
    public <T extends Serializable> void setWithExpire(RedisKey redisKey, T object) {
        Jedis jedis = pull();
        jedis.setex(redisKey.getKey().getBytes(), redisKey.getExpireTime(), SerializeHelper.serialize(object));
        push(jedis);
    }

    @Override
    public <T> T get(RedisKey redisKey) {
        Jedis jedis = pull();
        byte[] object = jedis.get(redisKey.getKey().getBytes());
        T result = SerializeHelper.deserialize(object);
        push(jedis);
        return result;
    }

    @Override
    public void remove(List<RedisKey> redisKey) {
        Jedis jedis = pull();
        byte[][] keys = new byte[redisKey.size()][];
        for (int i = 0; i < redisKey.size(); i++) {
            keys[i] = redisKey.get(i).getKey().getBytes();
        }
        jedis.del(keys);
        push(jedis);
    }

    @Override
    public boolean lock(RedisKey redisKey) {
        return setIfNotExist(redisKey, 1);
    }

    @Override
    public void release(RedisKey redisKey) {
        remove(Lists.newArrayList(redisKey));
    }

    @Override
    public boolean lockWithBiz(RedisKey redisKey, String bizCode) {
        return setIfNotExist(redisKey, bizCode);
    }

    @Override
    public void releaseWithBiz(RedisKey redisKey, String bizCode) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] " + "then return redis.call('del', KEYS[1]) "
                + "else return 0 end";
        Jedis jedis = pull();
        jedis.eval(script, Collections.singletonList(redisKey.getKey()), Collections.singletonList(bizCode));
        push(jedis);
    }

}
