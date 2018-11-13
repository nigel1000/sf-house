package sf.house.starter.mybatis.listener;

import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import sf.house.aop.util.BeanUtil;

import java.util.Map;

/**
 * Created by hznijianfeng on 2018/11/9.
 */

@ConfigurationProperties(prefix = MybatisMultiProperties.PREFIX, ignoreInvalidFields = true)
@Data
public class MybatisMultiProperties {

    public static final String PREFIX = "sf.mybatis";

    public MybatisProperties defaultConfig;

    public Map<String, MybatisProperties> mybatisMap;

    public void validSelf() {
        if (MapUtils.isEmpty(mybatisMap)) {
            return;
        }
        for (Map.Entry<String, MybatisProperties> entry : mybatisMap.entrySet()) {
            MybatisProperties target = entry.getValue();
            if (defaultConfig != null) {
                BeanUtil.genBeanIgnoreTargetNotNullProperty(defaultConfig, target);
            }
            target.validSelf();
        }
    }

}
