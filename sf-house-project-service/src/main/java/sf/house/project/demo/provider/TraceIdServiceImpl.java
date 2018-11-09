package sf.house.project.demo.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sf.house.bean.beans.Response;
import sf.house.bean.util.ThreadPoolUtil;
import sf.house.project.api.TraceIdService;

/**
 * Created by nijianfeng on 2018/10/5.
 */
@Slf4j
@Service("traceIdService")
public class TraceIdServiceImpl implements TraceIdService {

    @Override
    public Response<Boolean> traceId() {
        log.info("enter TraceIdServiceImpl traceId");
        ThreadPoolUtil.exec(() -> {
            log.info("enter ThreadPoolUtil runnable traceId");
        });
        ThreadPoolUtil.submit(() -> {
            log.info("enter ThreadPoolUtil callable traceId");
            return Boolean.TRUE;
        });
        log.info("end TraceIdServiceImpl traceId");
        return Response.ok(Boolean.TRUE);
    }
}
