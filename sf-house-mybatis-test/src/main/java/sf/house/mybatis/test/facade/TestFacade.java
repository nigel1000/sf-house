package sf.house.mybatis.test.facade;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sf.house.aop.annotation.CatchExcp;
import sf.house.bean.beans.Response;
import sf.house.bean.excps.UnifiedException;
import sf.house.mybatis.test.dao.TestMapper;
import sf.house.mybatis.test.domain.Test;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/9/8.
 */

@Component
@CatchExcp(module = "TestFacade")
public class TestFacade {

    @Resource
    private TestMapper testMapper;

    @Transactional
    public Response<Boolean> rollback$catchExcp() {

        testMapper.creates(Lists.newArrayList(Test.gen(), Test.gen()));

        throw UnifiedException.gen("测试", "就是为了回滚");
    }

}
