package sf.house.redis.helper.serializes;


import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

public class HessianSerializeUtil {

    public static void serialize(Serializable obj, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(obj);
        Hessian2Output hessian2Output = new Hessian2Output(outputStream);
        hessian2Output.writeObject(obj);
        hessian2Output.close();
    }


    @SuppressWarnings("unchecked")
    public static <T> T deserialize(InputStream inputStream) throws IOException {
        Hessian2Input in = new Hessian2Input(inputStream);
        return (T) in.readObject();
    }

}
