package sf.house.bean;

import sf.house.bean.util.NumUtil;

/**
 * Created by hznijianfeng on 2019/3/5.
 */

public class NumUtilTest {

    public static void main(String[] args) {

        System.out.println(NumUtil.parse10kToFen("1"));
        System.out.println(NumUtil.parseFenTo10k("101200"));

    }

}
