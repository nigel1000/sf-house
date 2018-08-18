package sf.house.aop.test;

import lombok.Data;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Data
public class User {

    private Long id;
    private String name;

    public static User getInstance(){
        User user = new User();
        user.setId(11L);
        user.setName("测试");
        return user;
    }

}
