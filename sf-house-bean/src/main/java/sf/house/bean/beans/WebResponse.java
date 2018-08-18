package sf.house.bean.beans;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by nijianfeng on 2018/8/14.
 */

@ToString(callSuper = true)
@Data
public class WebResponse<T> implements Serializable {

    public final static int SUCCESS = 200;
    public final static int ERROR = 500;

    private int code;
    private T result;
    private Object error;

    private WebResponse() {
    }

    public static <T> WebResponse<T> ok(T data) {
        WebResponse<T> resp = new WebResponse<>();
        resp.setCode(SUCCESS);
        resp.setResult(data);
        return resp;
    }

    public static <T> WebResponse<T> fail(Object error) {
        WebResponse<T> resp = new WebResponse<>();
        resp.setCode(ERROR);
        resp.setError(error);
        return resp;
    }

    public static <T> WebResponse<T> fail(int code, Object error) {
        WebResponse<T> resp = new WebResponse<>();
        resp.setCode(code);
        resp.setError(error);
        return resp;
    }

}
