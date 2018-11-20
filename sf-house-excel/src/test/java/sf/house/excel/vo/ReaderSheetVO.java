package sf.house.excel.vo;

import lombok.Data;
import sf.house.excel.annotations.ExcelParseField;
import sf.house.excel.enums.ExcelBool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Data
public class ReaderSheetVO implements Serializable {
    private static final long serialVersionUID = 2673802046675941279L;

    @ExcelParseField(cellIndex = 0, title = "属性1", required = false, desc = "字符串")
    private String attr1;
    @ExcelParseField(cellIndex = 1, title = "属性2", required = false, desc = "Integer")
    private Integer attr2;
    @ExcelParseField(cellIndex = 2, title = "属性3", required = false, desc = "Long")
    private Long attr3;
    @ExcelParseField(cellIndex = 3, title = "属性4", required = false, desc = "BigDecimal")
    private BigDecimal attr4;
    @ExcelParseField(cellIndex = 4, title = "属性5", required = false, desc = "范围类型")
    private ExcelBool bool;
    @ExcelParseField(cellIndex = 5, title = "属性6", required = false, desc = "日期类型", dateParse = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    @ExcelParseField(startIndex = 0, endIndex = 8, ignoreNull = true, dataType = String.class, required = false)
    private List<String> fixed2end1;
    @ExcelParseField(startIndex = 0, endIndex = 8, ignoreNull = false, dataType = String.class, required = false)
    private List<String> fixed2end2;
    @ExcelParseField(startIndex = 2, endIndex = Integer.MAX_VALUE, ignoreNull = true, dataType = String.class, required = false)
    private List<String> fixed2last1;
    @ExcelParseField(startIndex = 2, endIndex = Integer.MAX_VALUE, ignoreNull = false, dataType = String.class, required = false)
    private List<String> fixed2last2;
    @ExcelParseField(cellIndex = {1, 2}, dataType = Long.class, required = false)
    private List<Long> fixed;
}
