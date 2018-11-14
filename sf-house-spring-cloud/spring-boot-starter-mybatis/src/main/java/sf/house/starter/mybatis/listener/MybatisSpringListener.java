package sf.house.starter.mybatis.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import sf.house.starter.mybatis.Constants;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/11/13.
 */

@Slf4j
public class MybatisSpringListener implements SpringApplicationRunListener {

    private MybatisMultiProperties mybatisMultiProperties;

    private DefaultListableBeanFactory registry;

    public MybatisSpringListener(SpringApplication application, String... args) {
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        BindResult<MybatisMultiProperties> bindResult =
                Binder.get(environment).bind(MybatisMultiProperties.PREFIX, Bindable.of(MybatisMultiProperties.class));
        if (!bindResult.isBound()) {
            return;
        }
        mybatisMultiProperties = bindResult.get();
        if (MapUtils.isEmpty(mybatisMultiProperties.getMybatisMap())) {
            return;
        }
        mybatisMultiProperties.validSelf();
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        context.getBeanFactory().registerSingleton("mybatisMultiProperties", mybatisMultiProperties);
        for (Map.Entry<String, MybatisProperties> entry : mybatisMultiProperties.getMybatisMap().entrySet()) {
            MybatisProperties single = entry.getValue();
            log.info(Constants.LOG_PREFIX, "load mybatis " + entry.getKey(), single);
            ConfigurableListableBeanFactory factory = context.getBeanFactory();
            if (factory instanceof DefaultListableBeanFactory) {
                registry = (DefaultListableBeanFactory) factory;
                // 注册 DataSource
                BeanDefinition source = getDataSourceDefinition(single);
                registry.registerBeanDefinition(single.getDataSourceBeanName(), source);
                // 注册 TransactionManager
                BeanDefinition tx = getTransactionManagerDefinition(single);
                registry.registerBeanDefinition(single.getTransactionManagerBeanName(), tx);
                // 注册 SqlSessionFactory
                SqlSessionFactory sessionFactory =
                        getSqlSessionFactory(single, (DataSource) registry.getBean(single.getDataSourceBeanName()));
                registry.registerSingleton(single.getSqlSessionFactoryBeanName(), sessionFactory);
                // 注册 SqlSessionTemplate
                if (StringUtils.isNotEmpty(single.getSqlSessionTemplateBeanName())) {
                    BeanDefinition template = getSqlSessionTemplateDefinition(single);
                    registry.registerBeanDefinition(single.getSqlSessionTemplateBeanName(), template);
                }
                // 注册 MapperScannerConfigurer
                BeanDefinition scannerConfigurer = getMapperScannerConfigurer(single);
                registry.registerBeanDefinition(single.getMapperScannerConfigurerBeanName(), scannerConfigurer);

            } else {
                throw new IllegalArgumentException("BeanFactory 不是 DefaultListableBeanFactory 实例");
            }
        }
    }

    private BeanDefinition getMapperScannerConfigurer(MybatisProperties single) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(MapperScannerConfigurer.class);
        addPropertyValue(builder, "sqlSessionFactory", registry.getBean(single.getSqlSessionFactoryBeanName()));
        addPropertyValue(builder, "basePackage", single.getBasePackage());
        return builder.getBeanDefinition();
    }

    private BeanDefinition getSqlSessionTemplateDefinition(MybatisProperties single) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SqlSessionTemplate.class);
        builder.addConstructorArgReference(single.getSqlSessionFactoryBeanName());
        builder.addConstructorArgValue(single.getExecutorType());
        return builder.getBeanDefinition();
    }

    private SqlSessionFactory getSqlSessionFactory(MybatisProperties single, DataSource dataSource) {
        try {
            SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
            // 设置属性
            factory.setDataSource(dataSource);
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            if (StringUtils.isNotEmpty(single.getConfigLocation())) {
                factory.setConfigLocation(resolver.getResource(single.getConfigLocation()));
            } else {
                factory.setTypeAliasesSuperType(Serializable.class);
            }
            if (null != single.getTypeAliases()) {
                factory.setTypeAliases(single.getTypeAliases());
            }
            if (null != single.getTypeAliasesPackage()) {
                factory.setTypeAliasesPackage(single.getTypeAliasesPackage());
            }
            if (null != single.getTypeHandlersPackage()) {
                factory.setTypeHandlersPackage(single.getTypeHandlersPackage());
            }
            if (null != single.getMapperLocations()) {
                factory.setMapperLocations(resolver.getResources(single.getMapperLocations()));
            }
            return factory.getObject();
        } catch (Exception ex) {
            throw new IllegalArgumentException("生成 SqlSessionFactory 失败", ex);
        }
    }

    private BeanDefinition getTransactionManagerDefinition(MybatisProperties single) {
        BeanDefinitionBuilder builder =
                BeanDefinitionBuilder.rootBeanDefinition(single.getTransactionManagerBeanClass());
        builder.addConstructorArgReference(single.getDataSourceBeanName());
        addPropertyValue(builder, "nestedTransactionAllowed", single.getNestedTransactionAllowed());
        return builder.getBeanDefinition();
    }

    private BeanDefinition getDataSourceDefinition(MybatisProperties single) {
        // 组装 dataSource
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(single.getDataSourceBeanClass());
        if (StringUtils.isNotEmpty(single.getDataSourceInitMethod())) {
            builder.setInitMethodName(single.getDataSourceInitMethod());
        }
        if (StringUtils.isNotEmpty(single.getDataSourceDestroyMethod())) {
            builder.setDestroyMethodName(single.getDataSourceDestroyMethod());
        }
        addPropertyValue(builder, "driverClassName", single.getDriverClassName());
        addPropertyValue(builder, "url", single.getUrl());
        addPropertyValue(builder, "username", single.getUsername());
        addPropertyValue(builder, "password", single.getPassword());
        if ("com.alibaba.druid.pool.DruidDataSource".equals(single.getDataSourceBeanClass().trim())) {
            addPropertyValue(builder, "testOnBorrow", single.getTestOnBorrow());
            addPropertyValue(builder, "validationQuery", single.getValidationQuery());
            addPropertyValue(builder, "maxActive", single.getMaxActive());
            addPropertyValue(builder, "filters", single.getFilters());
            addPropertyValue(builder, "initialSize", single.getInitialSize());
            addPropertyValue(builder, "maxWait", single.getMaxActive());
            addPropertyValue(builder, "minIdle", single.getMinIdle());
            addPropertyValue(builder, "timeBetweenEvictionRunsMillis", single.getTimeBetweenEvictionRunsMillis());
            addPropertyValue(builder, "minEvictableIdleTimeMillis", single.getMinEvictableIdleTimeMillis());
            addPropertyValue(builder, "testWhileIdle", single.getTestWhileIdle());
            addPropertyValue(builder, "testOnReturn", single.getTestOnReturn());
            addPropertyValue(builder, "poolPreparedStatements", single.getPoolPreparedStatements());
            addPropertyValue(builder, "maxOpenPreparedStatements", single.getMaxOpenPreparedStatements());
            addPropertyValue(builder, "defaultAutoCommit", single.getDefaultAutoCommit());
        }
        return builder.getBeanDefinition();
    }

    private void addPropertyValue(BeanDefinitionBuilder builder, String key, Object value) {
        try {
            builder.addPropertyValue(key, value);
        } catch (Exception ex) {
            log.info(Constants.LOG_PREFIX, key, "未被设置", ex);
        }
    }


    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
