package sf.house.model.retry.demo;

import lombok.Getter;
import lombok.Setter;
import sf.house.model.retry.api.RetryMeta;
import sf.house.model.retry.api.RetryMsgService;

/**
 * Created by nijianfeng on 2018/9/12.
 */
public enum DemoRetryMeta implements RetryMeta {

    DEMO("goods_change", "kafka_consume", "retry_msg"),;

    @Getter
    private String bizType;
    @Getter
    private String msgType;
    @Getter
    private String tableName;
    @Setter
    @Getter
    private String msgKey;
    @Setter
    @Getter
    private RetryMsgService retryMsgService;

    DemoRetryMeta(String bizType, String msgType, String tableName) {
        this.bizType = bizType;
        this.msgType = msgType;
        this.tableName = tableName;
    }


    @Override
    public String getBizType() {
        return this.bizType;
    }

    @Override
    public String getMsgType() {
        return this.msgType;
    }

    @Override
    public String getMsgKey() {
        return this.msgKey;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public RetryMsgService getRetryMsgService() {
        return this.retryMsgService;
    }
}
