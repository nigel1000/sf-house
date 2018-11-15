package sf.house.starter.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sf.house.starter.mybatis.config.DruidConfig;

/**
 * Created by hznijianfeng on 2018/11/13.
 */

@Slf4j
@Configuration
// @Transactional 注意此注解默认事务处理器是 transactionManager
@Import({DruidConfig.class})
public class MybatisConfig {



}
