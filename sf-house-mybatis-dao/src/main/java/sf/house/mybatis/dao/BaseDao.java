package sf.house.mybatis.dao;

import lombok.NonNull;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import sf.house.bean.beans.page.PageParam;
import sf.house.bean.beans.page.PageResult;
import sf.house.bean.util.ConvertUtil;

import java.util.*;

/**
 * Created by nijianfeng on 2018/8/18.
 */

public abstract class BaseDao<T> extends SqlSessionDaoSupport {

    private static final String CREATE = "create";
    private static final String CREATES = "creates";
    private static final String DELETE = "delete";
    private static final String DELETES = "deletes";
    private static final String UPDATE = "update";
    private static final String LOAD = "load";
    private static final String LOADS = "loads";
    private static final String LIST = "list";
    private static final String PAGING = "paging";
    private static final String COUNT = "count";

    protected String nameSpace;

    public BaseDao() {
        this.nameSpace = this.getClass().getName();
        // //等于泛型的全路径名
        // if(this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
        // this.nameSpace =
        // ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
        // } else {
        // this.nameSpace =
        // ((Class)((ParameterizedType)this.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
        // }
    }

    protected void initSqlSessionFactory(SqlSessionFactory factory) {
        super.setSqlSessionFactory(factory);
    }

    @Override
    public abstract void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);

    public Boolean create(T t) {
        Map<String, T> map = new HashMap<>();
        map.put("item", t);
        return this.getSqlSession().insert(this.sqlId(CREATE), map) == 1;
    }

    public Integer creates(@NonNull List<T> ts) {
        if (ts == null || ts.size() == 0) {
            return -1;
        }
        return this.getSqlSession().insert(this.sqlId(CREATES), ts);
    }

    public Integer creates(@NonNull T... ts) {
        return creates(Arrays.asList(ts));
    }

    public Boolean delete(@NonNull Long id) {
        return this.getSqlSession().delete(this.sqlId(DELETE), id) == 1;
    }

    public Integer deletes(@NonNull List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return -1;
        }
        return this.getSqlSession().delete(this.sqlId(DELETES), ids);
    }

    public Integer deletes(@NonNull Long... ids) {
        return deletes(Arrays.asList(ids));
    }

    public Boolean update(@NonNull T t) {
        Map<String, T> map = new HashMap<>();
        map.put("item", t);
        return this.getSqlSession().update(this.sqlId(UPDATE), map) == 1;
    }

    public T load(@NonNull Long id) {
        return this.getSqlSession().selectOne(this.sqlId(LOAD), id);
    }

    public List<T> loads(@NonNull List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return this.getSqlSession().selectList(this.sqlId(LOADS), ids);
    }

    public List<T> loads(@NonNull Long... ids) {
        return loads(Arrays.asList(ids));
    }

    public List<T> list(@NonNull T t) {
        Map<String, T> map = new HashMap<>();
        map.put("item", t);
        return this.getSqlSession().selectList(this.sqlId(LIST), map);
    }

    public Integer count(@NonNull T t) {
        Map<String, T> map = new HashMap<>();
        map.put("item", t);
        return this.getSqlSession().selectOne(this.sqlId(COUNT), map);
    }

    public PageResult<T> paging(@NonNull T t, @NonNull PageParam pageParam) {
        Integer total = count(t);
        if (total <= 0L) {
            return PageResult.empty();
        }
        Map<String, Object> criteriaMap = ConvertUtil.obj2Map(t);
        criteriaMap.put("offset", pageParam.getOffset());
        criteriaMap.put("limit", pageParam.getLimit());
        criteriaMap.put("sortBy", pageParam.getSortBy());
        Map<String, Object> map = new HashMap<>();
        map.put("item", criteriaMap);
        List<T> datas = this.getSqlSession().selectList(this.sqlId(PAGING), map);
        return PageResult.gen(total, datas, pageParam);
    }

    private String sqlId(@NonNull String id) {
        return this.nameSpace + "." + id;
    }

}
