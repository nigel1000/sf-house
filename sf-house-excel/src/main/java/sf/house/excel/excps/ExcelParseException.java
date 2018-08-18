package sf.house.excel.excps;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Data
@NoArgsConstructor
public class ExcelParseException extends Exception {

    private List<ExcelParseExceptionInfo> infoList;

    public ExcelParseException(List<ExcelParseExceptionInfo> infoList) {
        super();
        this.infoList = infoList;
    }

}

