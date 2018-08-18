package sf.house.bean.excps;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一可抛出的异常定义
 * Created by nijianfeng on 2018/8/14.
 */

@Data
public class UnifiedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 业务模块
     */
    private String module;

    /**
     * 自定义信息回现
     */
    private String errorMessage;

    /**
     * 异常上下文，可以设置一些关键业务参数
     */
    private Map<String, Object> context;

    public static UnifiedException gen(String module, String errorMessage) {
        return new UnifiedException(module, errorMessage);
    }

    public static UnifiedException gen(String module, String errorMessage, Throwable cause) {
        return new UnifiedException(module, errorMessage, cause);
    }

    private UnifiedException(String module, String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.module = module;
    }

    private UnifiedException(String module, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
        this.module = module;
    }

    public UnifiedException addContext(String key, Object value) {

        if (context == null) {
            context = new HashMap<>();
        }
        context.put(key, value);
        return this;
    }

}
