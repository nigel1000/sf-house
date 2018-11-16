package sf.house.redis.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import sf.house.aop.util.BeanUtil;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.FunctionUtil;
import sf.house.redis.base.Constants;
import sf.house.redis.client.base.JedisClusterDeployClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ClusterDeployClient extends JedisClusterDeployClient {

    // 部分或全部cluster节点信息
    private List<String> nodes;
    // 连接超时
    private int connectTimeout = 30000;
    // 读写超时
    private int soTimeout = 30000;
    // 重试次数
    private int maxAttempts = 3;

    @Override
    protected void validSelf() {
        super.validSelf();
        if (CollectionUtils.isEmpty(this.nodes)) {
            throw UnifiedException.gen(Constants.MODULE, "cluster 节点信息不能为空");
        }
    }

    @Override
    protected void initPool() {
        GenericObjectPoolConfig genericObjectPoolConfig = BeanUtil.genBean(this, GenericObjectPoolConfig.class);
        cluster = new JedisCluster(FunctionUtil.valueSet(this.getNodes(), HostAndPort::parseString),
                this.getConnectTimeout(), this.getSoTimeout(), this.getMaxAttempts(), genericObjectPoolConfig);

        // 关闭 oss 客户端
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (cluster != null) {
                try {
                    cluster.close();
                } catch (IOException e) {
                    log.info("关闭 redis cluster [nodes:{}] 失败!", this.getNodes(), e);
                }
            }
        }));
    }

}
