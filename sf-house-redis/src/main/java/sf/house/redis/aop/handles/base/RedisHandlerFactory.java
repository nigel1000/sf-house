package sf.house.redis.aop.handles.base;

import org.springframework.stereotype.Component;
import sf.house.redis.aop.enums.RedisPair;
import sf.house.redis.aop.handles.*;

import javax.annotation.Resource;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Component
public class RedisHandlerFactory {

    @Resource
    private LockReleaseHandler lockReleaseHandler;
    @Resource
    private GetSetHandler getSetHandler;
    @Resource
    private GetSetWithExpireHandler getSetWithExpireHandler;
    @Resource
    private BatchGetSetHandler batchGetSetHandler;
    @Resource
    private DeleteHandler deleteHandler;
    @Resource
    private UpdateHandler updateHandler;
    @Resource
    private BatchDeleteHandler batchDeleteHandler;

    public RedisCacheHandler createHandler(RedisPair redisPair) {
        if (redisPair == null) {
            return null;
        }
        switch (redisPair) {
            case GET_SET:
                return getSetHandler;
            case GET_SET_WITH_EXPIRE:
                return getSetWithExpireHandler;
            case BATCH_GET_SET:
                return batchGetSetHandler;
            case DELETE:
                return deleteHandler;
            case UPDATE:
                return updateHandler;
            case BATCH_DELETE:
                return batchDeleteHandler;
            case LOCK_RELEASE:
                return lockReleaseHandler;
        }
        return null;
    }
}
