package sf.house.project.web.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sf.house.mybatis.generator.core.DB2Domain;
import sf.house.mybatis.generator.core.DB2Dto;
import sf.house.mybatis.generator.core.DB2Mapper;
import sf.house.mybatis.generator.model.FileVo;
import sf.house.mybatis.generator.util.PropertiesLoad;
import sf.house.mybatis.generator.util.base.DBUtils;
import sf.house.project.web.param.MybatisParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/10/9.
 */

@Controller
@RequestMapping(value = "/mybatis")
@Slf4j
public class MybatisController {

    // http://localhost:9000/mybatis/show
    @RequestMapping(value = "/show")
    public String get(HttpServletRequest request, Model model) {
        Object fileVos = request.getSession().getAttribute("fileVos");
        if (fileVos != null) {
            model.addAttribute("fileVos", fileVos);
        }
        Object param = request.getSession().getAttribute("param");
        if (param != null) {
            model.addAttribute("param", param);
        }
        request.getSession().removeAttribute("fileVos");
        request.getSession().removeAttribute("param");
        return "pages/mybatis";
    }

    // http://localhost:9000/mybatis/gen
    @RequestMapping(value = "/gen")
    public String gen(HttpServletRequest request, MybatisParam param) {

        param.validSelf();
        PropertiesLoad.init(param.genProperties());

        List<FileVo> result = Lists.newArrayList();
        // DB2Domain
        DB2Domain db2Domain = new DB2Domain();
        result.addAll(db2Domain.genDomain());
        // DB2Dto
        DB2Dto db2Dto = new DB2Dto();
        result.addAll(db2Dto.genDto());
        // DB2Mapper
        DB2Mapper db2Mapper = new DB2Mapper();
        result.addAll(db2Mapper.genDao());
        result.addAll(db2Mapper.genMapper());

        DBUtils.closeConn();

        for (FileVo fileVo : result) {
            fileVo.setFileContent(StringEscapeUtils.escapeXml11(fileVo.getFileContent()));
        }
        // 重定向传递参数的两种方法
        request.getSession().setAttribute("fileVos", result);
        request.getSession().setAttribute("param", param);
        PropertiesLoad.clear();
        return "redirect:/mybatis/show";
    }

}
