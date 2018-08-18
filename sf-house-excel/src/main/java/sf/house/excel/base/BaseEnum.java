package sf.house.excel.base;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public interface BaseEnum<T extends BaseEnum>{

    T getEnumByDesc(String descValue);

    default String getDesc() {
        return "NULL";
    }

    default int getValue() {
        return -1;
    }

}
