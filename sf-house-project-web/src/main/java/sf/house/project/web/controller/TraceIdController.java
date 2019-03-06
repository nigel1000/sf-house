package sf.house.project.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sf.house.bean.beans.Response;
import sf.house.bean.beans.ResponseUtil;
import sf.house.project.api.TraceIdService;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/10/5.
 */
@RestController
@RequestMapping(value = "/traceId")
@Slf4j
public class TraceIdController {

    @Resource
    private TraceIdService traceIdService;

    // http://localhost:9000/traceId/traceId
    @RequestMapping(value = "/traceId")
    @ResponseBody
    public Response<Boolean> traceId() {
        log.info("enter TraceIdController traceId");
        Boolean result = ResponseUtil.parse(traceIdService.traceId());
        log.info("end TraceIdController traceId");
        return Response.ok(result);
    }

}
