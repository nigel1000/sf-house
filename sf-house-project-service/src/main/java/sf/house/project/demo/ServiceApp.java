package sf.house.project.demo;

import io.dubbo.springboot.DubboAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by nijianfeng on 17/6/10.
 */
@ImportResource(value = {"classpath:/spring/provider.xml"})
@ComponentScan(basePackages = {"sf.house"})
@SpringBootApplication(exclude = {DubboAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement
public class ServiceApp {

    public static void main(String args[]) {
        SpringApplication.run(ServiceApp.class, args);
    }


}