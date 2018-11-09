package sf.house.model.record;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hznijianfeng on 2018-10-23 15:13:03.
 * 操作日志记录流程表
 */

@Data
@NoArgsConstructor
public class RecordLog implements Serializable {

    private static final long serialVersionUID = 1L;

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


}