package sf.house.spring.actuator;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import sf.house.spring.actuator.config.YamlPropertyLoaderFactory;

/**
 * Created by nijianfeng on 2018/10/27.
 */

@Configuration("actuatorAutoConfig")
@ComponentScan
@PropertySource(value = "classpath:actuator.yml", factory = YamlPropertyLoaderFactory.class)
public class AutoConfig {


}
