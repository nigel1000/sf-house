package sf.house.aop;


import sf.house.aop.util.BeanCopyUtil;
import sf.house.bean.beans.page.PageParam;

import java.util.Map;

/**
 * Created by nijianfeng on 2018/8/16.
 */
public class BeanCopyUtilTest {

    public static void main(String[] args) {

        PageParam pageParam1 = PageParam.valueOfByPageNo(1, 10);
        Map<String, Object> map = BeanCopyUtil.object2Map(pageParam1);
        System.out.println(map);
        System.out.println(pageParam1);
        System.out.println(BeanCopyUtil.map2Object(map, PageParam.class));

        PageParam pageParam2 = PageParam.valueOfByPageNo(2, 30);
        PageParam pageParam3 = BeanCopyUtil.genBean(pageParam2, PageParam.class);
        System.out.println(pageParam2);
        System.out.println(pageParam3);

    }

}
