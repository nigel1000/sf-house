package sf.house.bean;

import lombok.extern.slf4j.Slf4j;
import sf.house.bean.util.IdUtil;
import sf.house.bean.util.Slf4jUtil;

/**
 * Created by hznijianfeng on 2018/10/29.
 */

@Slf4j
public class IdUtilTest {

    public static void main(String[] args) {
        log.info("{}", IdUtil.timeDiy("diy"));
        log.info("{}", IdUtil.snowflakeId());
        log.info("{}", IdUtil.snowflakeId());
        Slf4jUtil.setLogLevel("error");
        log.info("{}", IdUtil.timeDiy("diy"));
        log.info("{}", IdUtil.snowflakeId());
        log.info("{}", IdUtil.snowflakeId());
    }

}
