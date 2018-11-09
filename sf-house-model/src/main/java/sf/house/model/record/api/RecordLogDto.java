package sf.house.model.record.api;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by nijianfeng on 2018/10/28.
 */

@Data
@ToString
public class RecordLogDto {

    /**
     *
     */
    private Long id;

    /**
     * 业务 id
     */
    private String bizId;

    /**
     * 模块类型
     */
    private Integer moduleType;

    /**
     * 模块类型 名称
     */
    private String moduleTypeName;

    /**
     * 操作类型
     */
    private Integer bizType;

    /**
     * 操作类型 名称
     */
    private String bizTypeName;

    /**
     * 先前的值 建议转成 json
     */
    private String beforeValue;

    /**
     * 修改参数 建议转成 json
     */
    private String updateValue;

    /**
     * 修改后的值 建议转成 json
     */
    private String afterValue;

    /**
     * 额外的扩展字段，存sql等
     */
    private String extra;

    /**
     * 操作备注
     */
    private String operateRemark;

    /**
     * 操作人 id
     */
    private String operatorId;

    /**
     * 操作人 姓名
     */
    private String operatorName;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 修改时间
     */
    private Date updateAt;

    public static RecordLogDto gen() {
        return gen(null, null, null, null, null, null);
    }

    public static RecordLogDto gen(String bizId) {
        return gen(bizId, null, null, null, null, null);
    }

    public static RecordLogDto gen(String bizId, String beforeValue, String updateValue) {
        return gen(bizId, beforeValue, updateValue, null, null, null);
    }

    public static RecordLogDto gen(String bizId, String beforeValue, String updateValue, String afterValue, String operatorId, String operatorName) {
        RecordLogDto result = new RecordLogDto();
        result.setBizId(bizId);
        result.setBeforeValue(beforeValue);
        result.setUpdateValue(updateValue);
        result.setAfterValue(afterValue);
        result.setOperatorId(operatorId);
        result.setOperatorName(operatorName);
        return result;
    }

}
