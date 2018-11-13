package sf.house.project.demo.dao;

import org.apache.ibatis.annotations.Select;

/**
 * Created by nijianfeng on 2018/8/16.
 */
public interface TestMapper {

    @Select("select count(*) from test")
    Integer count();

}
