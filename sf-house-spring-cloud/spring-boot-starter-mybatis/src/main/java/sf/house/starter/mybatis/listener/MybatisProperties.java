package sf.house.starter.mybatis.listener;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.session.ExecutorType;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/11/9.
 */

@Data
@ToString(exclude = {"password"})
public class MybatisProperties {

    // dataSource
    private String dataSourceBeanName;
    private String dataSourceBeanClass;
    private String dataSourceInitMethod;
    private String dataSourceDestroyMethod;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer maxActive = 20;
    private String filters;
    private Integer initialSize = 1;
    private Integer maxWait = 1000;
    private Integer minIdle = 1;
    private Integer timeBetweenEvictionRunsMillis = 60000;
    private Integer minEvictableIdleTimeMillis = 300000;
    private String validationQuery = "SELECT sysdate()";
    private Boolean testWhileIdle = Boolean.TRUE;
    private Boolean testOnBorrow = Boolean.FALSE;
    private Boolean testOnReturn = Boolean.FALSE;
    private Boolean poolPreparedStatements = Boolean.TRUE;
    private Integer maxOpenPreparedStatements = 20;
    private Boolean defaultAutoCommit = Boolean.TRUE;
    private List<String> proxyFilters;
    private Integer timeBetweenLogStatsMillis = 60000;
    private String statLogger;

    // transactionManager
    private String transactionManagerBeanName;
    private String transactionManagerBeanClass = "org.springframework.jdbc.datasource.DataSourceTransactionManager";
    private Boolean nestedTransactionAllowed = Boolean.TRUE;

    // sqlSessionFactory
    private String sqlSessionFactoryBeanName;
    private String configLocation;
    private String mapperLocations;
    private String typeAliasesPackage;
    private Class<?>[] typeAliases;
    private String typeHandlersPackage;
    // sqlSessionTemplate
    private String sqlSessionTemplateBeanName;
    private ExecutorType executorType = ExecutorType.SIMPLE;

    // mapperScannerConfigurer
    private String mapperScannerConfigurerBeanName;
    private String basePackage;

    public void validSelf() {
        if (null == dataSourceBeanName) {
            throw new IllegalArgumentException("dataSourceBeanName 不能为空");
        }
        if (null == dataSourceBeanClass) {
            throw new IllegalArgumentException("dateSourceBeanClass 不能为空");
        }
        if (null == driverClassName) {
            throw new IllegalArgumentException("driverClassName 不能为空");
        }
        if (null == url) {
            throw new IllegalArgumentException("url 不能为空");
        }
        if (null == username) {
            throw new IllegalArgumentException("username 不能为空");
        }
        if (null == password) {
            throw new IllegalArgumentException("password 不能为空");
        }
        if (null == transactionManagerBeanName) {
            throw new IllegalArgumentException("transactionManagerBeanName 不能为空");
        }
        if (null == sqlSessionFactoryBeanName) {
            throw new IllegalArgumentException("sqlSessionFactoryBeanName 不能为空");
        }
        if (null == mapperScannerConfigurerBeanName) {
            throw new IllegalArgumentException("mapperScannerConfigurerBeanName 不能为空");
        }
    }

}
