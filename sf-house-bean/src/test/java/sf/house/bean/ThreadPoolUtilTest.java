package sf.house.bean;

import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.ThreadPoolUtil;

/**
 * Created by hznijianfeng on 2019/2/28.
 */

public class ThreadPoolUtilTest {

    public static void main(String[] args) throws Exception {
        ThreadPoolUtil.exec(()->{
            throw UnifiedException.gen("打印日志");
        });

        Thread.sleep(1000);
        System.exit(-1);
    }
}