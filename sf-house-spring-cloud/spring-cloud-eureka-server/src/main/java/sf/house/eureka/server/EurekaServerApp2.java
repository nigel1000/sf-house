package sf.house.eureka.server; /**
 * Created by hznijianfeng on 2018/10/29.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @EnableEurekaServer 用来指定该项目为Eureka的服务注册中心
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApp2 {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EurekaServerApp2.class);
        // -Dspring.profiles.active=server2
        app.setAdditionalProfiles("server2");
        app.run(args);
    }

}
