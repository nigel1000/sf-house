package sf.house.project.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sf.house.project.demo.ServiceApp;
import sf.house.project.demo.dao.TestMapper;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApp.class)
@WebAppConfiguration
@Slf4j
public class TestMapperTest {

    @Resource
    private TestMapper testMapper;

    @Test
    public void testDemo() throws Exception {

        log.info("################# count:{}", testMapper.count());

    }


}
