package sf.house.spring.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Created by nijianfeng on 2018/10/27.
 */

@Component
public class DefineHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
//        Health.down().withDetail("errorMsg","自定义启动失败");
        return Health.up().build();
    }
}
