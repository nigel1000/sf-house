package sf.house.eureka.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by hznijianfeng on 2018/10/29.
 */

@RestController
@Slf4j
public class HelloController {

    @Resource
    private DiscoveryClient discoveryClient;

    @RequestMapping("/hello")
    public String hello() {
        ServiceInstance instance = discoveryClient.getLocalServiceInstance();
        //打印服务的服务id
        log.info("eureka 服务的服务id：" + instance.getServiceId());
        return "hello, this is eureka-provider!";
    }
}
