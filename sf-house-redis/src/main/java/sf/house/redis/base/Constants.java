package sf.house.redis.base;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Slf4j
public class Constants {

    public static final String MODULE = "redis 缓存组件";

    // 线程变量 设置序列化方式
    public static ThreadLocal<SerializeEnum> SERIALIZE_ENUM = ThreadLocal.withInitial(() -> SerializeEnum.HESSIAN);

    public static SerializeEnum getSerializeEnum() {
        SerializeEnum serializeEnum = SERIALIZE_ENUM.get();
        if (serializeEnum == null) {
            serializeEnum = SerializeEnum.HESSIAN;
        }
        return serializeEnum;
    }


    // 过期时间使用
    public static final int ONE_SECOND = 1;
    public static final int ONE_MINUTE = 60 * ONE_SECOND;
    public static final int ONE_HOUR = 60 * ONE_MINUTE;
    public static final int ONE_DAY = 24 * ONE_HOUR;

    // redis 返回
    public static final String RETURN_OK = "OK";


    public static void logGet(List<String> keys) {
        log.info("[module:{}] [from cache] [key:{}] ", MODULE, keys);
    }

    public static void logDel(List<String> keys) {
        log.info("[module:{}] [del cache] [key:{}] ", MODULE, keys);
    }

    public static void logUpdate(List<String> keys) {
        log.info("[module:{}] [update cache] [key:{}] ", MODULE, keys);
    }

    public static void logLock(List<String> keys) {
        log.info("[module:{}] [lock cache] [key:{}] ", MODULE, keys);
    }

    public static void logRelease(List<String> keys) {
        log.info("[module:{}] [release cache] [key:{}] ", MODULE, keys);
    }

}
