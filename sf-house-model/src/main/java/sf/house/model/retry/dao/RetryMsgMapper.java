package sf.house.model.retry.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import sf.house.model.retry.RetryMsg;
import sf.house.model.retry.api.RetryMeta;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/30.
 */

public interface RetryMsgMapper {

    @Insert("insert into ${retryMeta.tableName}"
            + "(id,biz_type,msg_type,msg_key,extra,body,try_times,max_try_times,init_error_message,end_error_message,status,create_at,update_at) "
            + "values "
            + "(null,#{retryMsg.bizType},#{retryMsg.msgType},#{retryMsg.msgKey},#{retryMsg.extra},#{retryMsg.body},#{retryMsg.tryTimes},#{retryMsg.maxTryTimes},#{retryMsg.initErrorMessage},#{retryMsg.endErrorMessage},#{retryMsg.status},now(),now())")
    Integer create(@Param("retryMsg") RetryMsg retryMsg, @Param("retryMeta") RetryMeta retryMeta);

    @Select("select * from ${retryMeta.tableName} "
            + "where msg_key=#{retryMeta.msgKey} and msg_type=#{retryMeta.msgType} and biz_type=#{retryMeta.bizType} "
            + "and status=0 and try_times<max_try_times")
    List<RetryMsg> loadNeedRetryMsg(@Param("retryMeta") RetryMeta retryMeta);

    @Update("update ${retryMeta.tableName} set status=0,try_times=try_times+1,end_error_message='' where id=#{id}")
    Integer fail(@Param("id") Long id, @Param("retryMeta") RetryMeta retryMeta);

    @Update("update ${retryMeta.tableName} set status=0, end_error_message=#{errorMessage} ,try_times=try_times+1 where id=#{id}")
    Integer failExp(@Param("id") Long id, @Param("errorMessage") String errorMessage, @Param("retryMeta") RetryMeta retryMeta);

    @Update("update ${retryMeta.tableName} set status=1 where id=#{id}")
    Integer success(@Param("id") Long id, @Param("retryMeta") RetryMeta retryMeta);

}
