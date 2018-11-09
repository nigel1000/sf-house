package sf.house.trace.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;
import sf.house.bean.util.trace.TraceConstants;
import sf.house.bean.util.trace.TraceIdUtil;

/**
 * Created by nijianfeng on 2018/10/5.
 */

@Slf4j
@Activate(group = {Constants.PROVIDER})
public class GetTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            RpcContext context = RpcContext.getContext();
            if (context.isProviderSide()) {
                TraceIdUtil.initTraceId(context.getAttachment(TraceConstants.TRACE_ID_KEY));
            }
        } catch (Exception ex) {
            log.info("GetTraceIdFilter exception:", ex);
        }
        return invoker.invoke(invocation);
    }


}
