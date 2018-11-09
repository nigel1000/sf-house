package sf.house.trace.web;

import org.springframework.util.StringUtils;
import sf.house.trace.common.TraceConstants;
import sf.house.trace.common.TraceIdUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nijianfeng on 2018/10/5.
 */
public class TraceIdRequestUtil {

    public static void initTraceId(HttpServletRequest request) {

        String contextTraceId = request.getHeader(TraceConstants.TRACE_ID_KEY);
        if (StringUtils.isEmpty(contextTraceId)) {
            contextTraceId = request.getParameter(TraceConstants.TRACE_ID_KEY);
        }
        if (!StringUtils.isEmpty(contextTraceId)) {
            try {
                TraceIdUtil.initTraceId(contextTraceId);
            } catch (Exception ex) {
                TraceIdUtil.initTraceId();
            }
        } else {
            TraceIdUtil.initTraceId();
        }

    }

}
