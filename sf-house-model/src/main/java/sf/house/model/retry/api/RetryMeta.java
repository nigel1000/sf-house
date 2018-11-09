package sf.house.model.retry.api;

/**
 * Created by nijianfeng on 2018/9/12.
 */
public interface RetryMeta {

    String getBizType();

    String getMsgType();

    String getMsgKey();

    String getTableName();

    RetryMsgService getRetryMsgService();

}
