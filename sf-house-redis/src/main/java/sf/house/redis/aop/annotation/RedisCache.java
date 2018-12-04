package sf.house.redis.aop.annotation;

import sf.house.redis.aop.enums.RedisPair;
import sf.house.redis.base.Constants;
import sf.house.redis.base.SerializeEnum;

import java.lang.annotation.*;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(RedisCaches.class)
public @interface RedisCache {

    RedisPair redisPair();

    SerializeEnum serializeEnum() default SerializeEnum.HESSIAN;

    int expireTime() default Constants.ONE_MINUTE;

    int[] keyAuto() default {};

    String keyDiy() default "";

    String failTips() default "";

}
