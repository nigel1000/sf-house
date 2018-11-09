package sf.house.aop;


import lombok.extern.slf4j.Slf4j;
import sf.house.aop.util.BeanUtil;
import sf.house.bean.beans.page.PageParam;

import java.util.Map;

/**
 * Created by nijianfeng on 2018/8/16.
 */

@Slf4j
public class BeanUtilTest {

    public static void main(String[] args) {

        PageParam pageParam1 = PageParam.valueOfByPageNo(1, 10);
        Map<String, Object> map = BeanUtil.obj2Map(pageParam1);
        log.info("{}", map);
        log.info("{}", pageParam1);
        log.info("{}", BeanUtil.map2Obj(map, PageParam.class));

        PageParam pageParam2 = PageParam.valueOfByPageNo(2, 30);
        PageParam pageParam3 = BeanUtil.genBean(pageParam2, PageParam.class);
        log.info("{}", pageParam2);
        log.info("{}", pageParam3);

        PageParam from = new PageParam();
        from.setPageNo(2);
        from.setSortBy("id desc");
        PageParam to = new PageParam();
        to.setLimit(32);
        to.setSortBy("id asc");
        log.info("{}", BeanUtil.genBeanIgnoreSourceNullProperty(from, to));
        to = new PageParam();
        to.setLimit(32);
        to.setSortBy("id asc");
        log.info("{}", BeanUtil.genBeanIgnoreTargetNotNullProperty(from, to));
        to = new PageParam();
        to.setLimit(32);
        to.setSortBy("id asc");
        log.info("{}", BeanUtil.genBean(from, to));

    }

}
