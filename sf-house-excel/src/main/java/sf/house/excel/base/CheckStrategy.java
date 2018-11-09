package sf.house.excel.base;

import sf.house.excel.annotations.ExcelParseField;
import sf.house.excel.excps.ExcelParseExceptionInfo;

/**
 * Created by nijianfeng on 2018/8/26.
 */
public interface CheckStrategy {

    ExcelParseExceptionInfo check(Object value, ExcelParseField excelAnnotation, ExcelParseExceptionInfo expInfo);

}
