package sf.house.mybatis.test.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import sf.house.mybatis.test.domain.Test;

/**
 * Created by hznijianfeng on 2018/08/16.
 */

public interface TestMapper {

    Integer create(@Param("item") Test item);

    Integer creates(List<Test> items);

    Integer delete(Long id);

    Integer deletes(List<Long> ids);

    Integer update(@Param("item") Test item);

    Test load(Long id);

    List<Test> loads(List<Long> ids);

    List<Test> list(@Param("item") Map<String, Object> item);

    Integer count(@Param("item") Map<String, Object> item);

    List<Test> paging(@Param("item") Map<String, Object> item);

}