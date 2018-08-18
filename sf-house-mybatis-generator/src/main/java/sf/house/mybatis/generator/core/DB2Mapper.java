package sf.house.mybatis.generator.core;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import sf.house.mybatis.generator.model.MapperVo;
import sf.house.mybatis.generator.model.Table;
import sf.house.mybatis.generator.util.*;
import sf.house.mybatis.generator.util.base.DBUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by nijianfeng on 18/1/29.
 */
public class DB2Mapper {

    private DBUtils dbUtils = new MySqlUtils();

    private Boolean needGen = Boolean.valueOf(PropertiesLoad.getByKey("gen_mapper", Boolean.TRUE));

    private String daoPackage;
    private String daoType;
    private String baseMapper;
    private String domainPackage;
    private String tableNames;
    private String dateNowVal;
    private String dynamicCondition;
    private String mapperIds;
    private String mapperPath;

    public DB2Mapper() {
        if (needGen) {
            daoPackage = PropertiesLoad.getByKey("mapper_dao_package", Boolean.TRUE);
            daoType = PropertiesLoad.getByKey("mapper_dao_type", Boolean.TRUE);
            baseMapper = PropertiesLoad.getByKey("base_mapper_path", Boolean.TRUE);
            domainPackage = PropertiesLoad.getByKey("mapper_domain_package", Boolean.FALSE);
            if (StringUtils.isBlank(domainPackage)) {
                domainPackage = PropertiesLoad.getByKey("domain_package", Boolean.TRUE);
            }
            tableNames = PropertiesLoad.getByKey("mapper_table_names", Boolean.FALSE);
            if (StringUtils.isBlank(tableNames)) {
                tableNames = Constants.tableNames;
            }
            dateNowVal = PropertiesLoad.getByKey("insert_date_to_now", Boolean.FALSE);
            dynamicCondition = PropertiesLoad.getByKey("dynamic_condition_exclude", Boolean.FALSE);
            mapperIds = PropertiesLoad.getByKey("mapper_sql_ids", Boolean.TRUE);
            mapperPath = PropertiesLoad.getByKey("mapper_path", Boolean.FALSE);
            if (StringUtils.isBlank(mapperPath)) {
                mapperPath = Constants.path;
            }
        }
    }

    public synchronized void genMapper() {
        if (!needGen) {
            return;
        }
        List<String> dateNowValList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(dateNowVal);
        List<String> dynamicCondList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(dynamicCondition);
        List<String> mapperIdList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(mapperIds);
        List<String> nameList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tableNames);
        if (nameList.contains("all")) {
            nameList = dbUtils.getTableNames();
        }
        for (String t : nameList) {
            Table table = dbUtils.getTable(t);
            Map<String, Object> tplMap = Maps.newHashMap();
            tplMap.put("daoPackage", daoPackage);
            tplMap.put("daoType", daoType);
            tplMap.put("domainPackage", domainPackage);
            tplMap.put("tableName", t);
            tplMap.put("dateNowValList", dateNowValList);
            tplMap.put("dynamicCondList", dynamicCondList);
            tplMap.put("mapperIdList", mapperIdList);
            String className =
                    NameUtils.firstUpper(NameUtils.ruleConvert(t, Constants.dbNameRule, Constants.javaNameRule));
            tplMap.put("className", className);
            List<MapperVo> mapperVos = Lists.newArrayList();
            table.getFields().forEach(f -> {
                MapperVo mapperVo = new MapperVo();
                mapperVo.setType(Constants.typeMap.get(f.getType()));
                mapperVo.setDbName(f.getField());
                mapperVo.setJavaName(NameUtils
                        .firstLower(NameUtils.ruleConvert(f.getField(), Constants.dbNameRule, Constants.javaNameRule)));
                mapperVos.add(mapperVo);
            });
            tplMap.put("mapperVos", mapperVos);
            System.out.println(className + "Mapper.java file template map:");
            System.out.println(tplMap);
            FileUtils.genFile(mapperPath + "/mapper/" + className + "Mapper.xml",
                    TemplateUtils.genTemplate("classpath:tpl/", "mapper.tpl", tplMap));
        }
        DBUtils.closeConn();
    }

    public synchronized void genDao() {
        if (!needGen) {
            return;
        }
        List<String> nameList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tableNames);
        if (nameList.contains("all")) {
            nameList = dbUtils.getTableNames();
        }
        for (String t : nameList) {
            Map<String, Object> tplMap = Maps.newHashMap();
            tplMap.put("author", Constants.author);
            tplMap.put("currentDate", Constants.currentDate);
            tplMap.put("domainPackage", domainPackage);
            tplMap.put("daoPackage", daoPackage);
            tplMap.put("daoType", daoType);
            tplMap.put("baseMapper", baseMapper);
            String className =
                    NameUtils.firstUpper(NameUtils.ruleConvert(t, Constants.dbNameRule, Constants.javaNameRule));
            tplMap.put("className", className);
            System.out.println(className + daoType + ".java file template map:");
            System.out.println(tplMap);
            FileUtils.genFile(mapperPath + "/dao/" + className + daoType + ".java",
                    TemplateUtils.genTemplate("classpath:tpl/", "dao.tpl", tplMap));
        }
        DBUtils.closeConn();
    }

}
