package sf.house.bean.base;

import lombok.NonNull;
import sf.house.bean.beans.page.PageParam;
import sf.house.bean.beans.page.PageResult;
import sf.house.bean.util.ConvertUtil;

import java.util.Map;

/**
 * Created by nijianfeng on 2018/8/16.
 */

public class PageHelper {

    // 分页
    public static <T> PageResult<T> paging(@NonNull BaseMapper<T> baseMapper,
                                           @NonNull T item, @NonNull PageParam pageParam) {
        long total = baseMapper.count(item);
        if (total <= 0L) {
            return PageResult.empty();
        }
        Map<String, Object> criteriaMap = ConvertUtil.obj2Map(item);
        criteriaMap.put("offset", pageParam.getOffset());
        criteriaMap.put("limit", pageParam.getLimit());
        criteriaMap.put("orderBy", pageParam.getOrderBy());
        return PageResult.gen(total, baseMapper.paging(criteriaMap), pageParam);
    }

}
