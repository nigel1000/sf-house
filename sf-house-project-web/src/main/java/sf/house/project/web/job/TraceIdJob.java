package sf.house.project.web.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sf.house.aop.annotation.TraceId;
import sf.house.project.api.TraceIdService;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/10/5.
 */

@EnableScheduling
@Component
@Slf4j
public class TraceIdJob {

    @Resource
    private TraceIdService traceIdService;

    @TraceId
    @Scheduled(cron = "0 */1 * * * ?")
    public void traceId() {
        log.info("enter TraceIdJob traceId");
        traceIdService.traceId();
        log.info("end TraceIdJob traceId");
    }

}
