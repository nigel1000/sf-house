package sf.house.aop.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class StringSplitUtil {

    public static final Splitter COMMA_SPLITTER = Splitter.on(",").trimResults().omitEmptyStrings();

    public static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();

    public static final Splitter COLON_SPLITTER = Splitter.on(":").trimResults().omitEmptyStrings();

    public static final Joiner COLON_JOINER = Joiner.on(":").skipNulls();

}
