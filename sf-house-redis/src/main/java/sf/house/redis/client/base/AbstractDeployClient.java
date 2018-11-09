package sf.house.redis.client.base;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import sf.house.bean.excps.UnifiedException;
import sf.house.redis.base.Constants;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Data
public abstract class AbstractDeployClient implements DeployClient, InitializingBean {

    // 连接池的配置
    // 最大连接数
    protected Integer maxTotal = 8;
    // 最大空闲连接数
    protected Integer maxIdle = 8;
    // 最小空闲连接数
    protected Integer minIdle = 0;
    protected Boolean lifo = true;
    protected Boolean fairness = false;

    // 连接的最后空闲时间，达到此值后空闲连接被移除
    protected Long minEvictableIdleTimeMillis = 60000L;
    protected Long softMinEvictableIdleTimeMillis = 1800000L;
    // 做空闲连接检测时，每次的采样数
    protected Integer numTestsPerEvictionRun = -1;
    protected String evictionPolicyClassName = "org.apache.commons.pool2.impl.DefaultEvictionPolicy";
    protected Boolean testOnCreate = false;
    // 借用连接时是否做连接有效性检测 ping
    protected Boolean testOnBorrow = false;
    // 归还时是否做连接有效性检测 ping
    protected Boolean testOnReturn = false;
    // 借用连接时是否做空闲检测
    protected Boolean testWhileIdle = true;
    // 空闲连接的检测周期
    protected Long timeBetweenEvictionRunsMillis = 30000L;
    // 连接池用尽后，调用者是否等待
    protected Boolean blockWhenExhausted = true;
    // 连接池没有连接后客户端的最大等待时间 －1表示不超时 一直等
    protected Long maxWaitMillis = -1L;
    // 开启jmx功能
    protected Boolean jmxEnabled = true;
    protected String jmxNamePrefix = "pool";
    protected String jmxNameBase = null;

    protected abstract void initPool();

    protected void validSelf() {
        if (this.maxTotal == null) {
            throw UnifiedException.gen(Constants.MODULE, "最大连接数不能为空");
        }
        if (this.maxIdle == null) {
            throw UnifiedException.gen(Constants.MODULE, "最大空闲连接数不能为空");
        }
        if (this.minIdle == null) {
            throw UnifiedException.gen(Constants.MODULE, "最小空闲连接数不能为空");
        }
    }

    @Override
    public void afterPropertiesSet() {
        validSelf();
        initPool();
    }

}
