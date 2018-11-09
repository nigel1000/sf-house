package sf.house.bean;

import lombok.extern.slf4j.Slf4j;
import sf.house.bean.beans.page.PageParam;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

@Slf4j
public class PageParamTest {

    public static void main(String[] args) {
        log.info("{}",PageParam.valueOfByPageNo(null, null));
        log.info("{}",PageParam.valueOfByPageNo(0, 30));
        log.info("{}",PageParam.valueOfByPageNo(1, 30));

        log.info("{}",PageParam.valueOfByPageNo(null, null, 20));
        log.info("{}",PageParam.valueOfByPageNo(0, 30, 20));
        log.info("{}",PageParam.valueOfByPageNo(1, 30, 20));

    }

}
