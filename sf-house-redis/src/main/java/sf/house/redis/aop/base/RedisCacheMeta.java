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
    private String prefixKey;
    private Class<?> returnType;
    private Object[] args;
    private SerializeEnum serializeEnum;
    private String keyAuto;
    private String keyDiy;
    private String key; // keyDiy 有则使用 prefixKey + keyDiy，否则使用 prefixKey + keyAuto
    private Integer expireTime;

    public RedisCacheMeta(ProceedingJoinPoint point, RedisCache redisCache) {

        this.point = point;
        this.redisCache = redisCache;

        this.className = point.getTarget().getClass().getName();
        this.methodName = point.getSignature().getName();
        this.prefixKey = this.className + "_" + this.methodName + "_";
        this.returnType = ((MethodSignature) point.getSignature()).getReturnType();
        this.args = point.getArgs();
        this.serializeEnum = redisCache.serializeEnum();

        StringBuilder keyAuto = new StringBuilder();
        int[] argKeys = redisCache.keyAuto();
        for (int argKey : argKeys) {
            if (argKey < this.args.length) {
                keyAuto.append(String.valueOf(this.args[argKey])).append("_");
            }
        }
        this.keyAuto = keyAuto.toString();

        this.keyDiy = redisCache.keyDiy();
        if (StringUtils.isNotBlank(this.keyDiy)) {
            this.keyDiy = this.keyDiy + "_";
            this.key = diyKey();
        } else {
            this.key = autoKey();
        }

        this.expireTime = redisCache.expireTime() + (int) (Math.random() * 10);
    }

    public String autoKey() {
        return this.prefixKey + this.keyAuto;
    }

    public String diyKey() {
        return this.prefixKey + this.keyDiy;
    }

}
