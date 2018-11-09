package sf.house.project.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nijianfeng on 2018/10/20.
 */
@Controller
@RequestMapping(value = "")
@Slf4j
public class IndexController {

    // http://localhost:9000
    @RequestMapping(value = "")
    public String get(HttpServletRequest request, Model model) {
        model.addAttribute("mybatisPage", "/mybatis/show");
        model.addAttribute("dubboTelnetPage", "/dubbo/telnet/show");
        model.addAttribute("dubboGenericPage", "/dubbo/generic/show");
        return "pages/index";
    }

}
