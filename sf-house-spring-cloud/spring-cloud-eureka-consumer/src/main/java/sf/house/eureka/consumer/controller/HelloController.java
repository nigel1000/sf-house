package sf.house.eureka.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/10/27.
 */
@RestController
public class HelloController {

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/hello")
    public String helloConsumer() {
        // 调用hello-service服务，注意这里用的是服务名，而不是具体的ip+port
        String result = restTemplate.getForObject("http://eureka-provider/hello", String.class);
        return "eureka consumer finish !!! result：" + result;
    }

}
