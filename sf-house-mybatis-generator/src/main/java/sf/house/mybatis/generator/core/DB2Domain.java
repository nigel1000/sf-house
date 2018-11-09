package sf.house.mybatis.generator.core;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sf.house.bean.util.ConvertUtil;
import sf.house.mybatis.generator.model.ClassVo;
import sf.house.mybatis.generator.model.FileVo;
import sf.house.mybatis.generator.model.Table;
import sf.house.mybatis.generator.util.*;
import sf.house.mybatis.generator.util.base.DBUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by nijianfeng on 18/1/29.
 */
@Slf4j
public class DB2Domain {

    private DBUtils dbUtils = new MySqlUtils();

    private Boolean needGen = Boolean.valueOf(PropertiesLoad.getByKey("gen_domain", Boolean.TRUE));

    private String domainPackage;
    private String tableNames;
    private String domainPath;

    public DB2Domain() {
        if (needGen) {
            domainPackage = PropertiesLoad.getByKey("domain_package", Boolean.TRUE);
            tableNames = PropertiesLoad.getByKey("domain_table_names", Boolean.FALSE);
            if (StringUtils.isBlank(tableNames)) {
                tableNames = Constants.tableNames;
            }
            domainPath = PropertiesLoad.getByKey("domain_path", Boolean.FALSE);
            if (StringUtils.isBlank(domainPath)) {
                domainPath = Constants.path;
            }
        }
    }

    public synchronized List<FileVo> genDomain() {
        List<FileVo> fileVos = Lists.newArrayList();
        if (!needGen) {
            return fileVos;
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
            tplMap.put("domainPackage", domainPackage);
            String className = ConvertUtil.firstUpper(ConvertUtil.underline2Camel(t));
            tplMap.put("className", className);
            tplMap.put("tableComment", table.getComment());
            List<ClassVo> classVos = Lists.newArrayList();
            table.getFields().forEach(f -> {
                ClassVo classVo = new ClassVo();
                classVo.setType(Constants.typeMap.get(f.getType()));
                classVo.setName(ConvertUtil.firstLower(ConvertUtil.underline2Camel(f.getField())));
                classVo.setMemo(f.getMemo());
                classVos.add(classVo);
            });
            tplMap.put("classVos", classVos);
            log.info(className + ".java file template map:");
            log.info("{}", tplMap);
            String filePath = domainPath + "/domain/" + className + ".java";
            String fileName = className + ".java";
            String fileContent = TemplateUtils.genTemplate("domain.tpl", tplMap);
            fileVos.add(new FileVo(filePath, fileName, fileContent));
            FileUtils.genFile(filePath, fileContent);
        }
        return fileVos;
    }


}
