package sf.house.model.retry.api;

import com.google.common.base.Throwables;
import lombok.Data;
import lombok.ToString;
import sf.house.aop.util.JsonUtil;

import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/9/12.
 */

@Data
@ToString
public class RetryMsgDto implements Serializable {

    private Long id;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 消息类型 rabbitmq kafka
     */
    private String msgType;

    /**
     * 消息名称: rabbitmq queue 主题名称 kafka topic
     */
    private String msgKey;

    /**
     * 譬如tag，key，集群ip，port等 供你确认是否是被需要重试的消息
     */
    private String extra;

    /**
     * 消息体
     */
    private String body;

    /**
     * 尝试次数
     */
    private Integer tryTimes;

    /**
     * 最大尝试次数 默认三次
     */
    private Integer maxTryTimes;

    /**
     * 消费时的错误信息
     */
    private String initErrorMessage;

    /**
     * 最后一次重试的错误信息
     */
    private String endErrorMessage;

    /**
     * 状态 0：失败 1：成功
     */
    private Integer status;

    public static RetryMsgDto gen(Object body, Exception ex) {
        RetryMsgDto retryMsg = new RetryMsgDto();
        String temp = JsonUtil.bean2json(body);
        retryMsg.body = temp.length() > 2000 ? temp.substring(0, 2000) : temp;
        retryMsg.status = StatusEnum.RETRY_FAILED.getCode();
        retryMsg.tryTimes = 0;
        retryMsg.maxTryTimes = 3;
        retryMsg.initErrorMessage = subErrorMessage(ex);
        return retryMsg;
    }

    public static String subErrorMessage(Exception ex) {
        if (ex == null) {
            return null;
        }
        String temp = Throwables.getStackTraceAsString(ex);
        return temp.length() > 1000 ? temp.substring(0, 1000) : temp;
    }

}
