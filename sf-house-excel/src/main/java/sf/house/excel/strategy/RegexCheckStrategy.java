package sf.house.excel.strategy;

import lombok.NonNull;
import sf.house.excel.annotations.ExcelParseField;
import sf.house.excel.base.CheckStrategy;
import sf.house.excel.base.Constants;
import sf.house.excel.base.ErrorEnum;
import sf.house.excel.excps.ExcelParseExceptionInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nijianfeng on 2018/8/26.
 */
public class RegexCheckStrategy implements CheckStrategy {

    @Override
    public ExcelParseExceptionInfo check(Object value, @NonNull ExcelParseField excelAnnotation,
            @NonNull ExcelParseExceptionInfo expInfo) {

        // 校验日期格式
        if (value != null && value.getClass() == String.class) {
            String regex = excelAnnotation.regex();
            if (!regex.trim().equals("")) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value.toString());
                if (!matcher.matches()) {
                    String tips = Constants.fillCommonPlaceholder(
                            excelAnnotation.regexTips().replace(Constants.REGEX_PLACEHOLDER, regex), expInfo);
                    expInfo.setErrMsg(tips);
                    expInfo.setErrType(ErrorEnum.REGEX.getCode());
                    return expInfo;
                }
            }
        }

        return null;
    }
}
