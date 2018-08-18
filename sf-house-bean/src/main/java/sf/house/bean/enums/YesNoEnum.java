package sf.house.bean.enums;

import lombok.Getter;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

public enum YesNoEnum {

    NULL(-1, null, "异常状态"),

    YES(1, Boolean.TRUE, "是"),

    NO(0, Boolean.FALSE, "否"),;

    @Getter
    private int code;
    @Getter
    private Boolean isYes;
    @Getter
    private String desc;

    YesNoEnum(int code, Boolean isYes, String desc) {
        this.code = code;
        this.isYes = isYes;
        this.desc = desc;
    }

    public boolean isEqual(int code) {
        return this.getCode() == code;
    }

    public static boolean isAvailable(int code) {
        return !parseCode(code).equals(NULL);
    }

    public static YesNoEnum parseCode(int code) {

        for (YesNoEnum item : YesNoEnum.values()) {
            if (item.getCode() == code)
                return item;
        }
        return NULL;
    }

}
