package sf.house.excel.base;

import lombok.Getter;

/**
 * Created by nijianfeng on 2018/8/26.
 */
public enum ErrorEnum {

    NULL("NULL", "未知错误"),
    MAX("MAX", "最大限制"),
    REQUIRE("REQUIRE", "不能为空"),
    OPTION_LIST("OPTION_LIST", "限定值"),
    DATE_PARSE("DATE_PARSE", "日期解析"),
    REGEX("REGEX", "正则匹配"),;

    @Getter
    private String code;
    @Getter
    private String desc;

    ErrorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean isEqual(String code) {
        return this.getCode() == code;
    }

    public static boolean isAvailable(String code) {
        return !parseCode(code).equals(NULL);
    }

    public static ErrorEnum parseCode(String code) {

        for (ErrorEnum item : ErrorEnum.values()) {
            if (item.getCode().equals(code))
                return item;
        }
        return NULL;
    }

}
