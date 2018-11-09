package sf.house.model.retry.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import sf.house.bean.excps.UnifiedException;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/30.
 * <p>
 * 要谨慎使用重新发送，顺序问题很难保证，是否幂等也不可预估，在重发期间可能已经做了很多事
 */

public abstract class AbstractRetryProcess implements InitializingBean {


    protected RetryMeta retryMeta;

    @Override
    public void afterPropertiesSet() throws Exception {
        initRetryMeta();
        if (retryMeta == null || StringUtils.isEmpty(retryMeta.getBizType())
                || StringUtils.isEmpty(retryMeta.getMsgKey()) || StringUtils.isEmpty(retryMeta.getMsgType())
                || StringUtils.isEmpty(retryMeta.getTableName()) || retryMeta.getRetryMsgService() == null) {
            throw UnifiedException.gen(this.getClass().getName(), "初始化失败");
        }
    }

    public abstract void initRetryMeta();

    public void handleRetry() {
        List<RetryMsgDto> retryMsgs = retryMeta.getRetryMsgService().loadNeedRetryMsg(retryMeta);
        for (RetryMsgDto retryMsg : retryMsgs) {
            try {
                if (bizExecute(retryMsg)) {
                    retryMeta.getRetryMsgService().success(retryMsg.getId(), retryMeta);
                } else {
                    retryMeta.getRetryMsgService().fail(retryMsg.getId(), retryMeta);
                }
            } catch (Exception ex) {
                retryMeta.getRetryMsgService().failExp(retryMsg.getId(), ex, retryMeta);
            }
        }
    }

    public abstract boolean bizExecute(RetryMsgDto retryMsg) throws Exception;


}
