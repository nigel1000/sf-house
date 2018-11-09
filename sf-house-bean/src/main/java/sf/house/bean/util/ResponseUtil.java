package sf.house.bean.util;


import sf.house.bean.beans.Response;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.excps.base.CommonError;

/**
 * Created by nijianfeng on 2018/8/14.
 */

public class ResponseUtil {

    private static final String MODULE = "parseResponse";

    private ResponseUtil() {}

    public static <T> T parse(Response<T> response) {
        if (response == null) {
            throw UnifiedException.gen(MODULE, CommonError.RPC_INVOKE_ERROR.getErrorMessage());
        }
        if (response.isSuccess()) {
            return response.getResult();
        }
        if (response.getError() != null) {
            throw UnifiedException.gen(MODULE, String.valueOf(response.getError()));
        }
        throw UnifiedException.gen(MODULE, CommonError.RPC_INVOKE_ERROR.getErrorMessage());
    }

}
