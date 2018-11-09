package sf.house.excel.excps;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Data
@Builder
@EqualsAndHashCode(of = {"sheetName", "colNum", "errType"})
public class ExcelParseExceptionInfo implements Serializable {

    private String sheetName;

    private String columnName;

    private int rowNum;

    private int colNum;

    private String currentValue;

    private String errType;

    private String errMsg;

    @Override
    public String toString() {
        return "{" + "sheetName='" + sheetName + '\'' + ", columnName='" + columnName + '\'' + ", rowNum=" + rowNum
                + ", colNum=" + colNum + ", currentValue='" + currentValue + '\'' + ", errType='" + errType + '\''
                + ", errMsg='" + errMsg + '\'' + '}';
    }
}
