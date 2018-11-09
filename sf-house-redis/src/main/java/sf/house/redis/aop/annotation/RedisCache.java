package sf.house.redis.aop.annotation;

import sf.house.redis.aop.enums.RedisPair;
import sf.house.redis.base.Constants;
import sf.house.redis.base.SerializeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RedisCache {

    RedisPair redisPair();

    SerializeEnum serializeEnum() default SerializeEnum.HESSIAN;

    int expireTime() default Constants.ONE_MINUTE;

    // keyDiy为空时：
    // key为args[index]的toString组合
    // 前缀为：类名_方法名_
    int[] keyAuto() default {};

    // 优先使用diy
    // 自定义key不为空则keyAuto配置失效
    String keyDiy() default "";

    String failTips() default "";

}
