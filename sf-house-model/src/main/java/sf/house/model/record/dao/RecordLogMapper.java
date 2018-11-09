package sf.house.model.record.dao;

import org.apache.ibatis.annotations.Param;
import sf.house.model.record.RecordLog;

import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018-10-23 15:13:03.
 */

public interface RecordLogMapper {

    Integer create(@Param("item") RecordLog item);

    Integer creates(List<RecordLog> items);

    Integer delete(Long id);

    Integer deletes(List<Long> ids);

    Integer update(@Param("item") RecordLog item);

    RecordLog load(Long id);

    List<RecordLog> loads(List<Long> ids);

    List<RecordLog> list(@Param("item") Map<String, Object> item);

    Integer count(@Param("item") Map<String, Object> item);

    List<RecordLog> paging(@Param("item") Map<String, Object> item);

}