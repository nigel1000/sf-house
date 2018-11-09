package sf.house.model.retry;

import org.springframework.stereotype.Component;
import sf.house.aop.util.BeanUtil;
import sf.house.model.retry.api.RetryMeta;
import sf.house.model.retry.api.RetryMsgDto;
import sf.house.model.retry.api.RetryMsgService;
import sf.house.model.retry.dao.RetryMsgMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/9/12.
 */

@Component
public class RetryMsgServiceImpl implements RetryMsgService {

    @Resource
    private RetryMsgMapper retryMsgMapper;

    @Override
    public Integer create(RetryMsgDto retryMsg, RetryMeta retryMeta) {
        retryMsg.setBizType(retryMeta.getBizType());
        retryMsg.setMsgType(retryMeta.getMsgType());
        retryMsg.setMsgKey(retryMeta.getMsgKey());
        return retryMsgMapper.create(BeanUtil.genBean(retryMsg, RetryMsg.class), retryMeta);
    }

    @Override
    public List<RetryMsgDto> loadNeedRetryMsg(RetryMeta retryMeta) {
        return BeanUtil.genBeanList(retryMsgMapper.loadNeedRetryMsg(retryMeta), RetryMsgDto.class);
    }

    @Override
    public Integer fail(Long id, RetryMeta retryMeta) {
        return retryMsgMapper.fail(id, retryMeta);
    }

    @Override
    public Integer failExp(Long id, Exception ex, RetryMeta retryMeta) {
        return retryMsgMapper.failExp(id, RetryMsgDto.subErrorMessage(ex), retryMeta);
    }

    @Override
    public Integer success(Long id, RetryMeta retryMeta) {
        return retryMsgMapper.success(id, retryMeta);
    }
}
