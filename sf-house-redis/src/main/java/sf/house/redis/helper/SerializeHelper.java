package sf.house.redis.helper;

import lombok.extern.slf4j.Slf4j;
import sf.house.bean.excps.UnifiedException;
import sf.house.redis.base.Constants;
import sf.house.redis.base.SerializeEnum;
import sf.house.redis.helper.serializes.HessianSerializeUtil;
import sf.house.redis.helper.serializes.JavaSerializeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Slf4j
public class SerializeHelper {

    public static byte[] serialize(Serializable obj) {

        if (obj == null) {
            return null;
        }

        try {
            SerializeEnum serializeEnum = Constants.getSerializeEnum();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
            switch (serializeEnum) {
                case HESSIAN:
                    HessianSerializeUtil.serialize(obj, outputStream);
                    break;
                case PROTO_STUFF:
                    throw UnifiedException.gen(Constants.MODULE, "暂不支持");
                case JAVA:
                    JavaSerializeUtil.serialize(obj, outputStream);
                    break;
                case KRYO:
                    throw UnifiedException.gen(Constants.MODULE, "暂不支持");
            }
            return outputStream.toByteArray();
        } catch (Exception ex) {
            // log.info(Constants.LOG_PREFIX, "序列化失败", ex);
            throw UnifiedException.gen(Constants.MODULE, "序列化失败", ex);
        }

    }

    public static <T> T deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            T value = null;
            SerializeEnum serializeEnum = Constants.getSerializeEnum();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            switch (serializeEnum) {
                case HESSIAN:
                    value = HessianSerializeUtil.deserialize(inputStream);
                    break;
                case PROTO_STUFF:
                    throw UnifiedException.gen(Constants.MODULE, "暂不支持");
                case JAVA:
                    value = JavaSerializeUtil.deserialize(inputStream);
                    break;
                case KRYO:
                    throw UnifiedException.gen(Constants.MODULE, "暂不支持");
            }
            return value;

        } catch (Exception ex) {
            // log.info(Constants.LOG_PREFIX, "反序列化失败", ex);
            throw UnifiedException.gen(Constants.MODULE, "反序列化失败", ex);
        }
    }

}
