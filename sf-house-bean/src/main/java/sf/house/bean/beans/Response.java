package sf.house.bean.beans;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString(callSuper = true)
@Data
public class Response<T> implements Serializable {

    public static final int SUCCESS = 200;
    public static final int ERROR = 500;

    private int code;
    private boolean success;

    private T result;
    private Object error;

    private Response(int code, T data, Object desc) {
        this.code = code;
        this.success = (code == SUCCESS);
        this.result = data;
        this.error = desc;
    }

    public static <T> Response<T> ok() {
        return ok(null);
    }

    public static <T> Response<T> ok(int code, T data) {
        return ok(code, null, data);
    }

    public static <T> Response<T> ok(T data) {
        return ok(SUCCESS, null, data);
    }

    public static <T> Response<T> ok(int code, Object desc, T data) {
        return new Response<>(code, data, desc);
    }

    public static <T> Response<T> fail(Object desc) {
        return fail(ERROR, desc, null);
    }

    public static <T> Response<T> fail(int code, Object desc) {
        return fail(code, desc, null);
    }

    public static <T> Response<T> fail(Object desc, T data) {
        return fail(ERROR, desc, data);
    }

    public static <T> Response<T> fail(int code, Object desc, T data) {
        return new Response<>(code, data, desc);
    }

}