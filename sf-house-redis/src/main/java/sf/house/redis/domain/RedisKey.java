package sf.house.redis.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import sf.house.aop.define.Valid;
import sf.house.bean.excps.UnifiedException;
import sf.house.redis.base.Constants;

import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Data
@ToString
@EqualsAndHashCode(of = {"prefixKey", "suffixKey"})
public class RedisKey implements Valid, Serializable {

    private String prefixKey = "";

    private String suffixKey = "";

    private Integer expireTime = Constants.ONE_MINUTE;

    public static RedisKey createKey(String key) {
        RedisKey redisKey = new RedisKey();
        redisKey.setSuffixKey(key);
        return redisKey;
    }

    public static RedisKey createKey(String key, Integer expireTime) {
        RedisKey redisKey = new RedisKey();
        redisKey.setSuffixKey(key);
        redisKey.setExpireTime(expireTime);
        return redisKey;
    }

    public static RedisKey createPrefix(String prefix) {
        RedisKey redisKey = new RedisKey();
        redisKey.setPrefixKey(prefix);
        return redisKey;
    }

    public RedisKey createByPrefix(String suffixKey, Integer expireTime) {
        RedisKey redisKey = new RedisKey();
        redisKey.setPrefixKey(this.getPrefixKey());
        redisKey.setSuffixKey(suffixKey);
        redisKey.setExpireTime(expireTime);
        return redisKey;
    }


    @Override

    public void validSelf() {
        if (StringUtils.isBlank(prefixKey) && StringUtils.isBlank(suffixKey)) {
            throw UnifiedException.gen(Constants.MODULE, "缓存 key 不能为空");
        }
    }

    public String getKey() {
        return this.getPrefixKey() + this.getSuffixKey();
    }

}
