package sf.house.bean;

import sf.house.bean.beans.page.PageParam;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

public class PageParamTest {

    public static void main(String[] args) {
        System.out.println(PageParam.valueOfByPageNo(null,null));
        System.out.println(PageParam.valueOfByPageNo(0,30));
        System.out.println(PageParam.valueOfByPageNo(1,30));

        System.out.println(PageParam.valueOfByPageNo(null,null,20));
        System.out.println(PageParam.valueOfByPageNo(0,30,20));
        System.out.println(PageParam.valueOfByPageNo(1,30,20));

    }

}
