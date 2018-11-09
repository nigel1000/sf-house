package sf.house.excel.enums;

import com.google.common.collect.Lists;
import sf.house.excel.base.BaseEnum;
import sf.house.excel.base.Constants;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public enum ExcelBool implements BaseEnum {

    /**
     * NULL
     */
    NULL(Constants.ENUM_ILLEGAL_VALUE_NULL, Constants.ENUM_ILLEGAL_DESC_NULL),

    FALSE(0, "否"),
    TRUE(1, "是"),;

    /**
     * 值
     */
    private final Integer value;
    /**
     * 描述
     */
    private final String desc;

    /**
     * 构造函数
     */
    ExcelBool(Integer v, String d) {
        value = v;
        desc = d;
    }

    @Override
    public ExcelBool getEnumByDesc(String descValue) {
        for (ExcelBool type : ExcelBool.values()) {
            if (type.desc.equals(descValue))
                return type;
        }
        return NULL;
    }

    @Override
    public String getAllDesc() {
        List<String> descs = Lists.newArrayList();
        for (ExcelBool type : ExcelBool.values()) {
            if (!type.getDesc().equals(NULL.getDesc())) {
                descs.add(type.getDesc());
            }

        }
        return String.join(",", descs);
    }


    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
