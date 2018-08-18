package sf.house.bean.util;


import sf.house.bean.excps.UnifiedException;
import sf.house.bean.excps.base.CommonError;
import sf.house.bean.beans.RpcResponse;

/**
 * Created by nijianfeng on 2018/8/14.
 */

public class ResponseUtil {

    private static final String MODULE = "parseResponse";

    private ResponseUtil() {}

    public static <T> T parse(RpcResponse<T> response) {
        UnifiedException excp = UnifiedException.gen(MODULE, CommonError.RPC_INVOKE_ERROR.getErrorMessage());
        if (response == null) {
            throw excp;
        }
        if (response.isSuccess()) {
            return response.getResult();
        }
        excp.addContext("RpcResponse Error", response.getError());
        throw excp;
    }

    public static Object returnBaseDataType(Class returnType) {

        if (Boolean.TYPE == returnType) {
            return false;
        }
        if (Byte.TYPE == returnType) {
            return (byte) 0;
        }
        if (Short.TYPE == returnType) {
            return (short) 0;
        }
        if (Integer.TYPE == returnType) {
            return 0;
        }
        if (Float.TYPE == returnType) {
            return 0f;
        }
        if (Long.TYPE == returnType) {
            return 0L;
        }
        if (Double.TYPE == returnType) {
            return 0d;
        }
        if (Character.TYPE == returnType) {
            return (char) 0;
        }
        return null;
    }

}
