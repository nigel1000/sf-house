package sf.house.model.retry;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hznijianfeng on 2018/8/30.
 */

@Data
@ToString
public class RetryMsg implements Serializable {


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

    private Date createdAt;

    private Date updatedAt;

}
