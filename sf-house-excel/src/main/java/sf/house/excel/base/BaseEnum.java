package sf.house.excel.base;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public interface BaseEnum {

    BaseEnum getEnumByDesc(String descValue);

    String getAllDesc();

    default String getDesc() {
        return Constants.ENUM_ILLEGAL_DESC_NULL;
    }

    default Integer getValue() {
        return -1;
    }

}
