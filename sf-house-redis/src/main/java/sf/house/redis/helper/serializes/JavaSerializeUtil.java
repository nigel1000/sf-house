package sf.house.redis.helper.serializes;

import java.io.*;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

public class JavaSerializeUtil {

    public static void serialize(Object object, OutputStream outputStream) throws IOException {
        // 序列化
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(object);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        return (T) ois.readObject();
    }

}
