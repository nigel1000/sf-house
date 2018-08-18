package sf.house.bean.beans;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by nijianfeng on 2018/8/14.
 */

@ToString(callSuper = true)
@Data
public class RpcResponse<T> implements Serializable {

    private boolean success;
    private T result;
    private Object error;

    private RpcResponse() {
    }

    public static <T> RpcResponse<T> ok(T data) {
        RpcResponse<T> resp = new RpcResponse<>();
        resp.setSuccess(true);
        resp.setResult(data);
        return resp;
    }

    public static <T> RpcResponse<T> fail(Object error) {
        RpcResponse<T> resp = new RpcResponse<>();
        resp.setSuccess(false);
        resp.setError(error);
        return resp;
    }

}