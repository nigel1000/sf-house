package sf.house.project.web;

import io.dubbo.springboot.DubboAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by nijianfeng on 17/6/10.
 */
@ImportResource(value = {"classpath:/spring/consumer.xml"})
@ComponentScan(basePackages = {"sf.house"})
@SpringBootApplication(exclude = {DubboAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableScheduling
public class WebApp implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    public static void main(String args[]) {
        SpringApplication.run(WebApp.class, args);
    }

    @Override
    public void customize(ConfigurableWebServerFactory container) {
        container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
        container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
    }
}
