package sf.house.eureka.consumer.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/10/27.
 */

// 获取启动类，寻找主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest
// 引入 spring 对 junit4 的支持
@RunWith(SpringRunner.class)
// 开启 web 应用的配置 模拟 ServletContext
@WebAppConfiguration
public class ControllerTest {

    @Resource
    private WebApplicationContext ctx;

    protected MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        // 相当于 @WebMvcTest(HelloController.class) 只 mock 指定的类
        // mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();

        // mock 了此web的所有类
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }


}