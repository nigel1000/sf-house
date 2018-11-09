package sf.house.aop;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import sf.house.aop.util.SplitUtil;

/**
 * Created by hznijianfeng on 2018/8/30.
 */

@Slf4j
public class SplitUtilTest {

    public static void main(String[] args) {
        log.info("{}", SplitUtil.splitByColon("11:22:33", Long::valueOf));
        log.info("{}", SplitUtil.splitByColon("aa:bb:cc", a -> a));
        log.info("{}", SplitUtil.splitByComma("11,22,33", Long::valueOf));
        log.info("{}", SplitUtil.splitByComma("aa,bb,cc", a -> a));

        log.info(SplitUtil.joinByColon(Lists.newArrayList(1, 2, 3)));
        log.info(SplitUtil.joinByComma(Lists.newArrayList("2", "3", "4")));

        SplitUtil.splitExecute(Lists.newArrayList(1, 2, 3, 4, 5, 6), 2, System.out::println);

    }

}
