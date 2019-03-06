package sf.house.bean.beans;

import sf.house.bean.excps.UnifiedException;
import sf.house.bean.excps.base.CommonError;

/**
 * Created by nijianfeng on 2018/8/14.
 */

public class ResponseUtil {

    private ResponseUtil() {
    }

    public static <T> T parse(Response<T> response) {
        if (response == null) {
            throw UnifiedException.gen(CommonError.RPC_INVOKE_ERROR.getErrorMessage());
        }
        if (response.isSuccess()) {
            return response.getResult();
        }
        if (response.getError() != null) {
            throw UnifiedException.gen(String.valueOf(response.getError()));
        }
        throw UnifiedException.gen(CommonError.RPC_INVOKE_ERROR.getErrorMessage());
    }

}
