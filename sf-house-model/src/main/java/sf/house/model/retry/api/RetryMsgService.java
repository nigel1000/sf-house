package sf.house.model.retry.api;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/9/12.
 */

public interface RetryMsgService {

    Integer create(RetryMsgDto retryMsg, RetryMeta retryMeta);

    List<RetryMsgDto> loadNeedRetryMsg(RetryMeta retryMeta);

    Integer fail(Long id, RetryMeta retryMeta);

    Integer failExp(Long id, Exception ex, RetryMeta retryMeta);

    Integer success(Long id, RetryMeta retryMeta);

}
