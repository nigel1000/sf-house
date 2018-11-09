package sf.house.eureka.server;

/**
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
public class EurekaServerApp3 {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EurekaServerApp3.class);
        // -Dspring.profiles.active=server3
        app.setAdditionalProfiles("server3");
        app.run(args);
    }

}
