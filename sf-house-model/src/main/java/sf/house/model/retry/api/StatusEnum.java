package sf.house.model.retry.api;

import lombok.Getter;

/**
 * Created by hznijianfeng on 2018/8/30.
 */

public enum StatusEnum {
    RETRY_FAILED(0, "消费失败"),
    RETRY_SUCCESS(1, "重试成功"),

    ;

    @Getter
    private Integer code;
    @Getter
    private String desc;

    StatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
