package sf.house.excel.strategy;

import lombok.NonNull;
import sf.house.excel.annotations.ExcelParseField;
import sf.house.excel.base.CheckStrategy;
import sf.house.excel.base.Constants;
import sf.house.excel.base.ErrorEnum;
import sf.house.excel.excps.ExcelParseExceptionInfo;

import java.math.BigDecimal;

/**
 * Created by nijianfeng on 2018/8/26.
 */
public class MaxCheckStrategy implements CheckStrategy {

    @Override
    public ExcelParseExceptionInfo check(Object value, @NonNull ExcelParseField excelAnnotation,
            @NonNull ExcelParseExceptionInfo expInfo) {

        // 校验单元格的字符串的最大长度或者数值的最大值
        long max = excelAnnotation.max();
        if (value != null && max != Long.MIN_VALUE) {
            boolean isMaxExp = false;
            Class fieldClazz = value.getClass();
            String tips = Constants.fillCommonPlaceholder(
                    excelAnnotation.maxTips().replace(Constants.MAX_PLACEHOLDER, max + ""), expInfo);
            if (fieldClazz == BigDecimal.class) {
                if (new BigDecimal(value.toString()).compareTo(BigDecimal.valueOf(max)) > 0) {
                    isMaxExp = true;
                }
            } else if (fieldClazz == Long.class) {
                if (Long.valueOf(value.toString()) - max > 0) {
                    isMaxExp = true;
                }
            } else if (fieldClazz == Integer.class) {
                if (Integer.valueOf(value.toString()) - max > 0) {
                    isMaxExp = true;
                }
            } else if (fieldClazz == String.class) {
                if (String.valueOf(value).length() > max) {
                    isMaxExp = true;
                }
            }
            if (isMaxExp) {
                expInfo.setErrMsg(tips);
                expInfo.setErrType(ErrorEnum.MAX.getCode());
                return expInfo;
            }
        }

        return null;
    }
}
