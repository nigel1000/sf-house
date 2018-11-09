package sf.house.bean.util.trace;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nijianfeng on 2018/10/5.
 */
public class TraceIdRequestUtil {

    public static void initTraceId(HttpServletRequest request) {

        String contextTraceId = request.getHeader(TraceConstants.TRACE_ID_KEY);
        if (contextTraceId == null || "".equals(contextTraceId.trim())) {
            contextTraceId = request.getParameter(TraceConstants.TRACE_ID_KEY);
        }
        if (contextTraceId != null && !"".equals(contextTraceId.trim())) {
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
