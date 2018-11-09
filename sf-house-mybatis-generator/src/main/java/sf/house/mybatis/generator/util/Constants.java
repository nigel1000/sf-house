package sf.house.mybatis.generator.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by nijianfeng on 18/1/29.
 */

@Slf4j
public class Constants {

    public static String path;

    static {
        init();
        String temp = PropertiesLoad.getByKey("path", Boolean.FALSE);
        if ("default".equals(temp) || StringUtils.isBlank(temp)) {
            String targetPath = Constants.class.getResource("/").getPath();
            if (targetPath.contains("C:")) {
                path = targetPath.substring(1, targetPath.indexOf("target")) + "src/main/resources/dir/";
            } else {
                path = targetPath.substring(0, targetPath.indexOf("target")) + "src/main/resources/dir/";
            }
        } else {
            path = temp;
        }
        log.info("文件生成位置:" + path);
    }

    public static String dbUrl;
    public static String dbUser;
    public static String dbPwd;
    public static String dbSchema;
    public static String author;
    public static String currentDate;
    public static String tableNames;

    public static void init(){
        dbUrl = PropertiesLoad.getByKey("db_url", Boolean.TRUE);
        dbUser = PropertiesLoad.getByKey("db_user", Boolean.TRUE);
        dbPwd = PropertiesLoad.getByKey("db_pwd", Boolean.TRUE);
        dbSchema = PropertiesLoad.getByKey("db_schema", Boolean.TRUE);

        author = PropertiesLoad.getByKey("author", Boolean.TRUE);
        currentDate = PropertiesLoad.getByKey("current_date", Boolean.TRUE);

        tableNames = PropertiesLoad.getByKey("table_names", Boolean.FALSE);
    }

    public static Map<String, String> typeMap = Maps.newHashMap();

    static {
        typeMap.put("datetime", "Date");
        typeMap.put("date", "Date");
        typeMap.put("timestamp", "Date");

        typeMap.put("varchar", "String");
        typeMap.put("char", "String");
        typeMap.put("mediumtext", "String");

        typeMap.put("tinyint", "Integer");
        typeMap.put("smallint", "Integer");
        typeMap.put("int", "Integer");


        typeMap.put("mediumint", "Long");
        typeMap.put("bigint", "Long");

        typeMap.put("double", "BigDecimal");
        typeMap.put("decimal", "BigDecimal");

        typeMap.put("bit", "Boolean");
    }


}
