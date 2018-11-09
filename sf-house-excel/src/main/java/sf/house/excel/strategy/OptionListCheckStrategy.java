package sf.house.excel.strategy;

import lombok.NonNull;
import sf.house.excel.annotations.ExcelParseField;
import sf.house.excel.base.BaseEnum;
import sf.house.excel.base.CheckStrategy;
import sf.house.excel.base.Constants;
import sf.house.excel.base.ErrorEnum;
import sf.house.excel.excps.ExcelParseExceptionInfo;

/**
 * Created by nijianfeng on 2018/8/26.
 */
public class OptionListCheckStrategy implements CheckStrategy {

    @Override
    public ExcelParseExceptionInfo check(Object value, @NonNull ExcelParseField excelAnnotation,
            @NonNull ExcelParseExceptionInfo expInfo) {

        // 属性类型是枚举的校验
        if (value != null && value instanceof BaseEnum) {
            Integer valueTemp = ((BaseEnum) value).getValue();
            String descTemp = ((BaseEnum) value).getDesc();
            if (Constants.ENUM_ILLEGAL_DESC_NULL.equals(descTemp) && valueTemp == Constants.ENUM_ILLEGAL_VALUE_NULL) {
                String tips = Constants.fillCommonPlaceholder(excelAnnotation.optionListTips()
                        .replace(Constants.OPTION_LIST_PLACEHOLDER, ((BaseEnum) value).getAllDesc()), expInfo);
                expInfo.setErrMsg(tips);
                expInfo.setErrType(ErrorEnum.OPTION_LIST.getCode());
                return expInfo;
            }
        }

        return null;
    }
}
