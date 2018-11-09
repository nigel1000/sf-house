package sf.house.aop.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.aop.annotation.CatchExcp;
import sf.house.aop.annotation.EasyLog;
import sf.house.bean.beans.Response;
import sf.house.bean.excps.UnifiedException;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Component
@Slf4j
@EasyLog(module = "用户模块")
public class UserDao {

    public boolean addUser(User user) {
        log.info("save user ... ");
        return true;
    }

    @CatchExcp(module = "获取异常")
    public Response<User> getUser(boolean isNeedExcp) {
        log.info("get user ... ");
        if (isNeedExcp) {
            UnifiedException excp = UnifiedException.gen("用户模块", "我抛出了异常！");
            excp.addContext("test", User.getInstance());
            throw excp;
        }
        return Response.ok(User.getInstance());
    }

}
