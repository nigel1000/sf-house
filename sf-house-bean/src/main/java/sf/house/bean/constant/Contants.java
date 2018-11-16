package sf.house.bean.constant;

import java.util.regex.Pattern;

/**
 * Created by hznijianfeng on 2018/11/16.
 */

public class Contants {

    /**
     * 中文字符正则
     */
    public static final String CHINESE_REG_EX = "[\\u4e00-\\u9fa5]";
    public static final Pattern SPECIAL_MARK =
            Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】；：。？]");

}
