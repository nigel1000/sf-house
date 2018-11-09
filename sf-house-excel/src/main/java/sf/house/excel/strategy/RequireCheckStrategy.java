package sf.house.excel.strategy;

import lombok.NonNull;
import sf.house.excel.annotations.ExcelParseField;
import sf.house.excel.base.CheckStrategy;
import sf.house.excel.base.Constants;
import sf.house.excel.base.ErrorEnum;
import sf.house.excel.excps.ExcelParseExceptionInfo;

/**
 * Created by nijianfeng on 2018/8/26.
 */
public class RequireCheckStrategy implements CheckStrategy {

    @Override
    public ExcelParseExceptionInfo check(Object value, @NonNull ExcelParseField excelAnnotation,
            @NonNull ExcelParseExceptionInfo expInfo) {

        boolean required = excelAnnotation.required();
        if (required && (value == null || (value instanceof String && "".equals(((String) value).trim())))) {
            String tips = Constants.fillCommonPlaceholder(excelAnnotation.requiredTips(), expInfo);
            expInfo.setErrMsg(tips);
            expInfo.setErrType(ErrorEnum.REQUIRE.getCode());
            return expInfo;
        }

        return null;
    }
}
