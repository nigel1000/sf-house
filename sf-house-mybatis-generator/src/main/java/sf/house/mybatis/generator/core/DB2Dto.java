package sf.house.mybatis.generator.core;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import sf.house.mybatis.generator.model.ClassVo;
import sf.house.mybatis.generator.model.Table;
import sf.house.mybatis.generator.util.*;
import sf.house.mybatis.generator.util.base.DBUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by nijianfeng on 18/1/29.
 */
public class DB2Dto {

    private DBUtils dbUtils = new MySqlUtils();

    private Boolean needGen = Boolean.valueOf(PropertiesLoad.getByKey("gen_dto", Boolean.TRUE));

    private String dtoPackage;
    private String tableNames;
    private String dtoPath;

    public DB2Dto() {
        if (needGen) {
            dtoPackage = PropertiesLoad.getByKey("dto_package", Boolean.TRUE);
            tableNames = PropertiesLoad.getByKey("dto_table_names", Boolean.FALSE);
            if (StringUtils.isBlank(tableNames)) {
                tableNames = Constants.tableNames;
            }
            dtoPath = PropertiesLoad.getByKey("dto_path", Boolean.FALSE);
            if (StringUtils.isBlank(dtoPath)) {
                dtoPath = Constants.path;
            }
        }
    }

    public synchronized void genDto() {
        if (!needGen) {
            return;
        }
        List<String> nameList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tableNames);
        if (nameList.contains("all")) {
            nameList = dbUtils.getTableNames();
        }
        for (String t : nameList) {
            Table table = dbUtils.getTable(t);
            Map<String, Object> tplMap = Maps.newHashMap();
            tplMap.put("author", Constants.author);
            tplMap.put("currentDate", Constants.currentDate);
            tplMap.put("dtoPackage", dtoPackage);
            String className =
                    NameUtils.firstUpper(NameUtils.ruleConvert(t, Constants.dbNameRule, Constants.javaNameRule));
            tplMap.put("className", className + "Dto");
            tplMap.put("tableComment", table.getComment());
            List<ClassVo> classVos = Lists.newArrayList();
            table.getFields().forEach(f -> {
                ClassVo classVo = new ClassVo();
                classVo.setName(NameUtils
                        .firstLower(NameUtils.ruleConvert(f.getField(), Constants.dbNameRule, Constants.javaNameRule)));
                classVo.setMemo(f.getMemo());
                classVo.setType(Constants.typeMap.get(f.getType()));
                classVos.add(classVo);
            });
            tplMap.put("classVos", classVos);
            System.out.println(className + "Dto.java file template map:");
            System.out.println(tplMap);
            FileUtils.genFile(dtoPath + "/dto/" + className + "Dto.java",
                    TemplateUtils.genTemplate("classpath:tpl/", "dto.tpl", tplMap));
        }
        DBUtils.closeConn();
    }

}
