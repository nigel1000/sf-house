package sf.house.mybatis.test.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import sf.house.mybatis.dao.BaseDao;
import sf.house.mybatis.test.domain.Test;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/8/18.
 */
@Repository
public class TestDao extends BaseDao<Test> {

    // 可选择数据源注入
    @Resource(name = "sqlSessionFactory")
    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.initSqlSessionFactory(sqlSessionFactory);
        this.nameSpace = this.getClass().getName();
    }

}
