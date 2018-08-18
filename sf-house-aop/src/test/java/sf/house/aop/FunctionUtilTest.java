package sf.house.aop;


import sf.house.aop.util.FunctionUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class FunctionUtilTest {

    public static void main(String[] args) {
        List<Integer> id = Arrays.asList(1, 2, 3, 4, 5, 3, 4, 5, 6, 7, 8);
        int everyTime = 2;

        FunctionUtil.splitExecute(id, everyTime, (t) -> {
            System.out.println(t);
        });
    }

}
