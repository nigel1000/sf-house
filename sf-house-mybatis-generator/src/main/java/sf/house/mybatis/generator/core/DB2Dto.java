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

    public synchronized List<FileVo> genDto() {
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
            tplMap.put("dtoPackage", dtoPackage);
            String className = ConvertUtil.firstUpper(ConvertUtil.underline2Camel(t));
            tplMap.put("className", className + "Dto");
            tplMap.put("tableComment", table.getComment());
            List<ClassVo> classVos = Lists.newArrayList();
            table.getFields().forEach(f -> {
                ClassVo classVo = new ClassVo();
                classVo.setName(ConvertUtil.firstLower(ConvertUtil.underline2Camel(f.getField())));
                classVo.setMemo(f.getMemo());
                classVo.setType(Constants.typeMap.get(f.getType()));
                classVos.add(classVo);
            });
            tplMap.put("classVos", classVos);
            log.info(className + "Dto.java file template map:");
            log.info("{}", tplMap);
            String filePath = dtoPath + "/dto/" + className + "Dto.java";
            String fileName = className + "Dto.java";
            String fileContent = TemplateUtils.genTemplate("dto.tpl", tplMap);
            fileVos.add(new FileVo(filePath, fileName, fileContent));
            FileUtils.genFile(filePath, fileContent);
        }
        return fileVos;
    }

}
