package sf.house.excel.excps;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Data
@NoArgsConstructor
public class ExcelParseExceptionInfo implements Serializable {

    private String sheetName;

    private int rowNum;

    private String columnName;

    private String errMsg;

    public ExcelParseExceptionInfo(int rowNum, String columnName, String errMsg) {
        this.rowNum = rowNum;
        this.columnName = columnName;
        this.errMsg = errMsg;
    }

    public ExcelParseExceptionInfo(String sheetName, int rowNum, String columnName, String errMsg) {
        this.sheetName = sheetName;
        this.rowNum = rowNum;
        this.columnName = columnName;
        this.errMsg = errMsg;
    }

}
