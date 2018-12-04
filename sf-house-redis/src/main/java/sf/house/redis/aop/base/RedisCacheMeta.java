package sf.house.redis.aop.base;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import sf.house.redis.aop.annotation.RedisCache;
import sf.house.redis.base.SerializeEnum;

import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Data
@ToString(exclude = {"args"})
public class RedisCacheMeta implements Serializable {

    private ProceedingJoinPoint point;
    private RedisCache redisCache;

    private String className;
    private String methodName;
    private Class returnType;
    private Object[] args;
    private String prefixKey;
    private String suffixKey;
    private String key;
    private int[] keySuffixIndex;
    private String keyPrefix;
    private Integer expireTime;

    private SerializeEnum serializeEnum;

    public RedisCacheMeta(ProceedingJoinPoint point, RedisCache redisCache) {

        this.point = point;
        this.redisCache = redisCache;
        this.serializeEnum = redisCache.serializeEnum();
        this.className = point.getTarget().getClass().getName();
        this.methodName = point.getSignature().getName();

        this.keyPrefix = redisCache.keyPrefix();
        if (StringUtils.isNotBlank(this.keyPrefix)) {
            this.prefixKey = this.keyPrefix + "_";
        } else {
            this.prefixKey = this.className + "_" + this.methodName + "_";
        }
        this.returnType = ((MethodSignature) point.getSignature()).getReturnType();
        this.args = point.getArgs();
        this.keySuffixIndex = redisCache.keySuffixIndex();
        StringBuilder keySuffixStr = new StringBuilder();
        for (int index : keySuffixIndex) {
            if (index < this.args.length) {
                keySuffixStr.append(String.valueOf(this.args[index])).append("_");
            }
        }
        this.suffixKey = keySuffixStr.toString();
        this.key = this.prefixKey + this.suffixKey;
        this.expireTime = redisCache.expireTime() + (int) (Math.random() * 10);
    }
}
