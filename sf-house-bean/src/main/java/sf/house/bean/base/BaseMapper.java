package sf.house.bean.base;

import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

public interface BaseMapper<T> {

    int create(T item);

    int creates(List<T> items);

    int delete(Long id);

    int deletes(List<Long> ids);

    int update(T item);

    T load(Long id);

    List<T> loads(List<Long> ids);

    List<T> list(T item);

    long count(T item);

    List<T> paging(Map<String, Object> item);

}

