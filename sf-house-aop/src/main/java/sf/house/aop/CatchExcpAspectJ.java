package sf.house.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import sf.house.aop.constants.LogConstant;
import sf.house.aop.annotation.CatchExcp;
import sf.house.aop.util.AspectUtil;
import sf.house.aop.util.SerializeUtil;
import sf.house.bean.beans.RpcResponse;
import sf.house.bean.beans.WebResponse;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.excps.base.CommonError;
import sf.house.bean.util.ResponseUtil;

import java.sql.SQLException;

/**
 * Created by hznijianfeng on 2018/8/15. 加在provider上，譬如facade等
 */

@Aspect
@Component
@Order(value = 9)
@Slf4j
public class CatchExcpAspectJ {

    @Pointcut("@within(sf.house.aop.annotation.CatchExcp)")
    public void clazz() {}

    @Pointcut("@annotation(sf.house.aop.annotation.CatchExcp)")
    public void method() {}

    @Around("clazz() || method()")
    public Object around(final ProceedingJoinPoint point) {

        CatchExcp catchExcp = AspectUtil.getAnnotation(point, CatchExcp.class);
        if (catchExcp == null) {
            log.error("==========impossible=========");
            return null;
        }
        String module = catchExcp.module();
        log.info(LogConstant.START_LOG_PREFIX + " args:{}", module, point.getTarget().getClass().getName(),
                point.getSignature().getName(), SerializeUtil.getObjString(point.getArgs()));
        return errorHandler(point, module);
    }

    private Object errorHandler(final ProceedingJoinPoint point, String module) {

        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Class returnType = ((MethodSignature) point.getSignature()).getReturnType();
        try {
            Object proceed = point.proceed();
            log.info(LogConstant.FINISH_LOG_PREFIX + " return:{}", module, className, methodName,
                    SerializeUtil.getObjString(proceed));
            return proceed;
        } catch (UnifiedException ex) {
            log.error(LogConstant.EXCP_LOG_PREFIX + " excpModule:{}, excpMessage:{}, excpContext:{}", module, className,
                    methodName, ex.getModule(), ex.getErrorMessage(), ex.getContext(), ex);
            return returnResult(ex.getErrorMessage(), returnType);
        } catch (DataAccessException | SQLException ex) {
            log.error(LogConstant.EXCP_LOG_PREFIX, module, className, methodName, ex);
            return returnResult(CommonError.DB_ERROR.getErrorMessage(), returnType);
        } catch (RuntimeException ex) {
            log.error("======================RuntimeException======================");
            log.error(LogConstant.EXCP_LOG_PREFIX, module, className, methodName, ex);
            return returnResult(CommonError.SYSTEM_ERROR.getErrorMessage(), returnType);
        } catch (Exception ex) {
            log.error("======================Exception======================");
            log.error(LogConstant.EXCP_LOG_PREFIX, module, className, methodName, ex);
            return returnResult(CommonError.SYSTEM_ERROR.getErrorMessage(), returnType);
        } catch (Throwable ex) {
            log.error("======================Throwable======================");
            log.error(LogConstant.EXCP_LOG_PREFIX, module, className, methodName, ex);
            return returnResult(CommonError.SYSTEM_ERROR.getErrorMessage(), returnType);
        }
    }

    private Object returnResult(String errorMessage, Class returnType) {

        if (RpcResponse.class == returnType) {
            return RpcResponse.fail(errorMessage);
        }
        if (WebResponse.class == returnType) {
            return WebResponse.fail(errorMessage);
        }
        return ResponseUtil.returnBaseDataType(returnType);
    }


}
