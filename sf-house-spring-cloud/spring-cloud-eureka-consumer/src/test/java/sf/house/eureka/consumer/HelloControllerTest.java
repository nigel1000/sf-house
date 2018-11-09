package sf.house.eureka.consumer;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sf.house.eureka.consumer.base.ControllerTest;
import sf.house.eureka.consumer.controller.HelloController;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by nijianfeng on 2018/10/27.
 */

public class HelloControllerTest extends ControllerTest {

    @Before
    @Override
    public void setUp() throws Exception {
        // 相当于 @WebMvcTest(HelloController.class)
        mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    public void hello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(status().isOk()) // 期待返回状态吗码200
                .andExpect(content().string(equalTo("Hello World")));
    }


}
