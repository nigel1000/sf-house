package sf.house.model.retry.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.aop.util.JsonUtil;
import sf.house.model.retry.api.AbstractRetryProcess;
import sf.house.model.retry.api.RetryMsgDto;
import sf.house.model.retry.api.RetryMsgService;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/9/12.
 */
@Component
@Slf4j
public class DemoRetryProcess extends AbstractRetryProcess {

    @Resource
    private RetryMsgService retryMsgService;


    @Override
    public void initRetryMeta() {
        this.retryMeta = DemoRetryMeta.DEMO;
        ((DemoRetryMeta) this.retryMeta).setMsgKey("topic_goods_change");
        ((DemoRetryMeta) this.retryMeta).setRetryMsgService(retryMsgService);
    }

    @Override
    public boolean bizExecute(RetryMsgDto retryMsg) throws Exception {
        log.info("bizExecute retryMsg : [{}]", JsonUtil.bean2json(retryMsg));

        return false;
    }
}
