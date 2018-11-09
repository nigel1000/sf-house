package sf.house.project.web.config;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import sf.house.aop.constants.LogConstant;
import sf.house.bean.beans.Response;
import sf.house.bean.excps.UnifiedException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by nijianfeng on 2018/8/14.
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class ControllerErrorHandler {

    private static final String LOGGING_REQUEST_PARAMETERS = "request parameters:{}";
    private static final String PARAM_ERROR_MESSAGE = "输入参数不合法";
    private static final String TIMEOUT_EXCEPTION_MESSAGE = "服务连接超时";
    private static final String DUBBO_RPC_EXCEPTION_MESSAGE = "远程服务调用失败";
    private static final String DB_EXCEPTION_MESSAGE = "数据库异常";
    private static final String UNKNOW_EXCEPTION_MESSAGE = "发生未知错误，请联系技术人员排查!";

    /**
     * Valid标签 校验失败
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, BindException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        List<FieldError> fieldErrors = ex.getFieldErrors();
        if (CollectionUtils.isEmpty(fieldErrors)) {
            return Response.fail(HttpStatus.BAD_REQUEST.value(), PARAM_ERROR_MESSAGE);
        }
        FieldError fieldError = fieldErrors.get(0);
        return Response.fail(HttpStatus.BAD_REQUEST.value(), fieldError.getDefaultMessage());
    }

    /**
     * Validated标签 校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, ConstraintViolationException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return Response.fail(HttpStatus.BAD_REQUEST.value(), PARAM_ERROR_MESSAGE);
        }
        ConstraintViolation<?> next = constraintViolations.iterator().next();
        return Response.fail(HttpStatus.BAD_REQUEST.value(), next.getMessage());
    }

    /**
     * required=true 校验失败
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, MissingServletRequestParameterException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.BAD_REQUEST.value(), "缺少必要参数");
    }

    /**
     * JSR303注解参数 校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processJsr303ValidatorError(NativeWebRequest request, MethodArgumentNotValidException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        BindingResult errors = ex.getBindingResult();
        if (errors == null || CollectionUtils.isEmpty(errors.getFieldErrors())) {
            return Response.fail(HttpStatus.BAD_REQUEST.value(), PARAM_ERROR_MESSAGE);
        }
        FieldError fieldError = errors.getFieldErrors().get(0);
        return Response.fail(HttpStatus.BAD_REQUEST.value(), fieldError.getDefaultMessage());
    }

    /**
     * 输入参数数据类型转换错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, HttpMessageNotReadableException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.BAD_REQUEST.value(), PARAM_ERROR_MESSAGE);
    }

    /**
     * 连接超时
     */
    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, TimeoutException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), TIMEOUT_EXCEPTION_MESSAGE);
    }

    /**
     * dubbo异常
     */
    @ExceptionHandler(RpcException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, RpcException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), DUBBO_RPC_EXCEPTION_MESSAGE);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(UnifiedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, UnifiedException ex) {
        String message = ex.getErrorMessage();
        printLogInfo("[{}] message:{}, context:{}, request parameters:{}", ex.getModule(), message, ex.getContext(),
                LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * spring封装的数据库异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, DataAccessException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), DB_EXCEPTION_MESSAGE);
    }

    /**
     * spring未捕获的数据库异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, SQLException ex) {
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), DB_EXCEPTION_MESSAGE);
    }

    /**
     * 兜底1
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, RuntimeException ex) {
        printLogInfo("======================Exception======================");
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), UNKNOW_EXCEPTION_MESSAGE);
    }

    /**
     * 兜底2
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, Exception ex) {
        printLogInfo("======================Exception======================");
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), UNKNOW_EXCEPTION_MESSAGE);
    }

    /**
     * 兜底3
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    public Response processControllerError(NativeWebRequest request, Throwable ex) {
        printLogInfo("======================Throwable======================");
        printLogInfo(LOGGING_REQUEST_PARAMETERS, LogConstant.getObjString(request.getParameterMap()), ex);
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), UNKNOW_EXCEPTION_MESSAGE);
    }

    /**
     * 打印日志信息
     */
    private void printLogInfo(String message, Object... values) {
        log.info(message, values);
    }

}