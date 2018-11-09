package sf.house.project.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import sf.house.bean.beans.Response;
import sf.house.bean.util.trace.TraceIdUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequestMapping(value = "/error")
@Controller
public class GlobalErrorController implements ErrorController {
    // 参考:http://blog.csdn.net/whatlookingfor/article/details/51548923

    // DefaultErrorAttributes
    private ErrorAttributes errorAttributes;
    @Autowired
    private ServerProperties serverProperties;

    // 初始化ExceptionController
    @Autowired
    public GlobalErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    // 定义404的ModelAndView
    @RequestMapping(produces = "text/html", value = "404")
    public ModelAndView errorHtml404(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        return new ModelAndView("404", model);
    }

    // 定义500的ModelAndView
    @RequestMapping(produces = "text/html", value = "500")
    public ModelAndView errorHtml500(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(getStatus(request).value());
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        body.put("traceId", TraceIdUtil.getTraceId());
        return new ModelAndView("500", body);
    }

    // 定义500的错误JSON信息
    @RequestMapping(value = "500")
    @ResponseBody
    public Response<ResponseEntity<Map<String, Object>>> error500(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        HttpStatus status = getStatus(request);
        Response<ResponseEntity<Map<String, Object>>> result = Response.fail("日志码:" + TraceIdUtil.getTraceId());
        result.setResult(new ResponseEntity<>(body, status));
        return result;
    }

    // 定义404的JSON数据
    @RequestMapping(value = "404")
    @ResponseBody
    public Response<ResponseEntity<Map<String, Object>>> error404(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        HttpStatus status = getStatus(request);
        Response<ResponseEntity<Map<String, Object>>> result = Response.fail(body.get("path") + " Not Found");
        result.setResult(new ResponseEntity<>(body, status));
        return result;
    }


    /**
     * Determine if the stacktrace attribute should be included.
     * 
     * @param request the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    private boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        // default never
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }


    // 获取错误的信息
    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    // 是否包含trace
    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    // 获取错误编码
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    // 实现错误路径,暂时无用,因为配置了containerCustomizer，否则默认是/error
    // @see WebApp#containerCustomizer()
    @Override
    public String getErrorPath() {
        return "";
    }
}
