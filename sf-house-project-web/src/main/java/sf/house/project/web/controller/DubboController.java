package sf.house.project.web.controller;

import com.alibaba.dubbo.rpc.service.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sf.house.aop.util.JsonUtil;
import sf.house.project.web.param.DubboGenericParam;
import sf.house.project.web.param.DubboTelnetParam;
import sf.house.project.web.util.DubboUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nijianfeng on 2018/10/20.
 */
@Controller
@RequestMapping(value = "/dubbo")
@Slf4j
public class DubboController {

    @RequestMapping(value = "/telnet/show")
    public String telnetGet(HttpServletRequest request, Model model) {
        Object result = request.getSession().getAttribute("result");
        if (result != null) {
            model.addAttribute("result", result);
        }
        Object param = request.getSession().getAttribute("param");
        if (param != null) {
            model.addAttribute("param", param);
        }
        request.getSession().removeAttribute("result");
        request.getSession().removeAttribute("param");
        return "pages/dubbo_telnet";
    }

    @RequestMapping(value = "/telnet/gen")
    public static String telnetGen(HttpServletRequest request, DubboTelnetParam param) {

        param.validSelf();

        String result = DubboUtil.getResultByTelnet(param, DubboUtil.getUrlByGenericService(param));

        // 重定向传递参数的两种方法
        request.getSession().setAttribute("result", StringEscapeUtils.escapeXml11(result));
        request.getSession().setAttribute("param", param);

        return "redirect:/dubbo/telnet/show";
    }

    @RequestMapping(value = "/generic/show")
    public String genericGet(HttpServletRequest request, Model model) {
        Object result = request.getSession().getAttribute("result");
        if (result != null) {
            model.addAttribute("result", result);
        }
        Object param = request.getSession().getAttribute("param");
        if (param != null) {
            model.addAttribute("param", param);
        }
        request.getSession().removeAttribute("result");
        request.getSession().removeAttribute("param");
        return "pages/dubbo_generic";
    }

    @RequestMapping(value = "/generic/gen")
    public static String genericGen(HttpServletRequest request, DubboGenericParam param) {

        param.validSelf();

        GenericService service = DubboUtil.getGenericService(param.getClassName(), param.getGroup(), param.getVersion(),
                param.getZkAddress(), param.getZkPort());
        Object result =
                service.$invoke(param.getMethodName(), param.getMethodParamTypes(), param.getMethodParamValues());

        // 重定向传递参数的两种方法
        request.getSession().setAttribute("result", StringEscapeUtils.escapeXml11(JsonUtil.bean2json(result)));
        request.getSession().setAttribute("param", param);

        return "redirect:/dubbo/generic/show";
    }

    // public static void main(String[] args) {
    // DubboGenericParam param = new DubboGenericParam();
    // param.setClassName("com.distmerchant.api.outer.goods.GoodsReadApiFacade");
    // param.setMethodName("queryGoodsDesc");
    // param.setMethodParamType("java.util.List");
    // param.setMethodParamValue("[[59149962]]");
    // param.setVersion("1.0");
    // param.setGroup("user_test4jd");
    // param.setZkAddress("127.0.0.1");
    // param.setZkPort(2181);
    //
    // param.validSelf();
    //
    // GenericService service = DubboUtil.getGenericService(param);
    // Object result =
    // service.$invoke(param.getMethodName(), param.getMethodParamTypes(), param.getMethodParamValues());
    // log.info("{}", result);
    // }

}
