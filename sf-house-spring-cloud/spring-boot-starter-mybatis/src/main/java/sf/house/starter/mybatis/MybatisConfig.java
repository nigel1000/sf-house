package sf.house.starter.mybatis;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/11/13.
 */

@Slf4j
@Configuration
// @Transactional 注意此注解默认事务处理器是 transactionManager
public class MybatisConfig {

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnClass(DruidDataSource.class)
    @ConditionalOnBean({DruidDataSource.class, DruidDataSourceStatLogger.class})
    public Boolean setDruidDataSourceStatLogger() {
        Map<String, DruidDataSource> dataSources = applicationContext.getBeansOfType(DruidDataSource.class);
        DruidDataSourceStatLogger statLogger = applicationContext.getBean(DruidDataSourceStatLogger.class);
        for (Map.Entry<String, DruidDataSource> entry : dataSources.entrySet()) {
            DruidDataSource dataSource = entry.getValue();
            dataSource.setStatLogger(statLogger);
            log.info(Constants.LOG_PREFIX, entry.getKey(), " 注入 DruidDataSourceStatLogger bean ");
        }
        return Boolean.TRUE;
    }

    @Bean
    @ConditionalOnClass(DruidDataSource.class)
    @ConditionalOnBean({DruidDataSource.class, Filter.class})
    public Boolean setDruidFilters() {
        Map<String, DruidDataSource> dataSources = applicationContext.getBeansOfType(DruidDataSource.class);
        Map<String, Filter> filterMap = applicationContext.getBeansOfType(Filter.class);
        for (Map.Entry<String, DruidDataSource> entry : dataSources.entrySet()) {
            DruidDataSource dataSource = entry.getValue();
            // set 逻辑是 addAll
            List<Filter> filterList = Lists.newArrayList();
            filterList.addAll(filterMap.values());
            dataSource.setProxyFilters(filterList);
            log.info(Constants.LOG_PREFIX + "[{}]", entry.getKey(), " 注入 Filter beans", filterMap.keySet());
        }
        return Boolean.TRUE;
    }

}
