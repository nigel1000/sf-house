package sf.house.redis.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import sf.house.aop.annotation.PreDefine;
import sf.house.aop.define.enums.RunType;
import sf.house.aop.util.BeanUtil;
import sf.house.bean.excps.UnifiedException;
import sf.house.redis.base.Constants;
import sf.house.redis.client.base.JedisDeployClient;

/**
 * Created by hznijianfeng on 2018/9/6.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@PreDefine(runType = {RunType.VALID})
public class SingleDeployClient extends JedisDeployClient {

    private String hostName = "localhost";
    private int port = 6379;
    private int timeout = 30000;
    private String password;

    @Override
    protected void validSelf() {

        super.validSelf();

        if (hostName == null) {
            throw UnifiedException.gen(Constants.MODULE, "主机名不能为空");
        }
    }

    @Override
    protected void initPool() {
        JedisPoolConfig poolConfig = BeanUtil.genBean(this, JedisPoolConfig.class);
        pool = new JedisPool(poolConfig, this.getHostName(), this.getPort(), this.getTimeout(), this.getPassword());

        // 关闭 oss 客户端
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (pool != null) {
                pool.close();
            }
        }));
    }


}
