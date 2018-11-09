package sf.house.trace.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;
import sf.house.bean.util.trace.TraceConstants;
import sf.house.bean.util.trace.TraceIdUtil;

@Slf4j
@Activate(group = {Constants.CONSUMER})
public class SetTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            RpcContext context = RpcContext.getContext();
            if (context.isConsumerSide()) {
                context.setAttachment(TraceConstants.TRACE_ID_KEY,
                        TraceIdUtil.initTraceIdIfAbsent());
            }
        } catch (Exception ex) {
            log.info("SetTraceIdFilter exception:", ex);
        }
        return invoker.invoke(invocation);
    }

}