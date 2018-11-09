package sf.house.mybatis.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

public interface BaseMapper<T> {

    Integer create(@Param("item") T item);

    Integer creates(List<T> items);

    Integer delete(Long id);

    Integer deletes(List<Long> ids);

    Integer update(@Param("item") T item);

    T load(Long id);

    List<T> loads(List<Long> ids);

    List<T> list(@Param("item") T item);

    Integer count(@Param("item") T item);

    List<T> paging(@Param("item") Map<String, Object> item);

}

