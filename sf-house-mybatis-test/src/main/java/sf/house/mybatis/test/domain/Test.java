package sf.house.mybatis.test.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hznijianfeng on 2018/08/16. 测试表
 */

@Data
@NoArgsConstructor
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     * varchar(100)
     */
    private String stringType;

    /**
     * char
     */
    private String charType;

    /**
     * mediumtext
     */
    private String mediumtextType;

    /**
     * datetime
     */
    private Date datetimeType;

    /**
     * tinyint
     */
    private Integer tinyintType;

    /**
     * smallint
     */
    private Integer smallintType;

    /**
     * mediumint
     */
    private Long mediumintType;

    /**
     * int
     */
    private Integer intType;

    /**
     * bigint
     */
    private Long bigintType;

    /**
     * double
     */
    private BigDecimal doubleType;

    /**
     * decimal(3,2)
     */
    private BigDecimal decimalType;

    /**
     * bit
     */
    private Boolean bitType;

    /**
     * date
     */
    private Date dateType;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 修改时间
     */
    private Date updateAt;

    public static Test gen() {
        Test test = new Test();
        test.setBigintType(1L);
        test.setBitType(Boolean.FALSE);
        test.setCharType("char");
        test.setDatetimeType(new Date());
        test.setDateType(new Date());
        test.setDecimalType(new BigDecimal(1));
        test.setDoubleType(new BigDecimal("11.11"));
        test.setIntType(3);
        test.setMediumintType(23L);
        test.setMediumtextType("你好");
        test.setSmallintType(23);
        test.setStringType("测试");
        test.setTinyintType(3);
        return test;
    }

}
