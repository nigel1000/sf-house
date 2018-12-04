package sf.house.redis.client;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisSentinelPool;
import sf.house.aop.annotation.PreDefine;
import sf.house.aop.define.enums.RunType;
import sf.house.aop.util.BeanUtil;
import sf.house.bean.excps.UnifiedException;
import sf.house.redis.base.Constants;
import sf.house.redis.client.base.JedisDeployClient;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/9/6.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@PreDefine(runType = {RunType.VALID})
public class SentinelDeployClient extends JedisDeployClient {

    // 主节点名
    private String masterName;
    // 哨兵节点集合
    private List<String> sentinels;
    // 连接超时
    private int connectTimeout = 30000;
    // 读写超时
    private int soTimeout = 30000;
    private String password;
    // 当前数据库索引
    private int database = 0;
    // 客户端名
    private String clientName;

    @Override
    protected void validSelf() {

        super.validSelf();

        if (masterName == null) {
            throw UnifiedException.gen(Constants.MODULE, "主节点名不能为空");
        }
        if (CollectionUtils.isEmpty(this.sentinels)) {
            throw UnifiedException.gen(Constants.MODULE, "哨兵节点不能为空");
        }
    }

    @Override
    protected void initPool() {
        GenericObjectPoolConfig genericObjectPoolConfig = BeanUtil.genBean(this, GenericObjectPoolConfig.class);
        pool = new JedisSentinelPool(this.getMasterName(), Sets.newHashSet(this.getSentinels()),
                genericObjectPoolConfig, this.getConnectTimeout(), this.getSoTimeout(), this.getPassword(),
                this.getDatabase(), this.getClientName());

        // 关闭 oss 客户端
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (pool != null) {
                pool.close();
            }
        }));
    }


}
