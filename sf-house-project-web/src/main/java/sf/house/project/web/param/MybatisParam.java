package sf.house.project.web.param;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.DateUtil;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

/**
 * Created by hznijianfeng on 2018/10/9.
 */

@Data
public class MybatisParam implements Serializable {

    private String db_schema;
    private String db_url;
    private String db_user;
    private String db_pwd;
    private String table_names;

    private String author;
    private String current_date;

    private String gen_domain;
    private String domain_package;

    private String gen_dto;
    private String dto_package;

    private String gen_mapper;
    private String mapper_dao_package;

    private String mapper_dao_type = "Mapper";
    private String mapper_sql_ids = "create,creates,list,paging,count,update,load,loads,delete,deletes";
    private String insert_date_to_now = "createdAt,updatedAt,createAt,updateAt";
    private String dynamic_condition_exclude = "id,createdAt,updatedAt,createAt,updateAt";

    // 存放文件目录
    private String path;

    public Properties genProperties() {
        Properties properties = new Properties();
        properties.setProperty("db_schema", db_schema);
        properties.setProperty("db_url", db_url);
        properties.setProperty("db_user", db_user);
        properties.setProperty("db_pwd", db_pwd);
        properties.setProperty("table_names", table_names);
        properties.setProperty("author", author);
        properties.setProperty("current_date", current_date);
        properties.setProperty("gen_domain", gen_domain);
        properties.setProperty("domain_package", domain_package);
        properties.setProperty("gen_dto", gen_dto);
        properties.setProperty("dto_package", dto_package);
        properties.setProperty("gen_mapper", gen_mapper);
        properties.setProperty("mapper_dao_package", mapper_dao_package);
        properties.setProperty("mapper_dao_type", mapper_dao_type);
        properties.setProperty("mapper_dao_package", mapper_dao_package);
        properties.setProperty("mapper_sql_ids", mapper_sql_ids);
        properties.setProperty("insert_date_to_now", insert_date_to_now);
        properties.setProperty("dynamic_condition_exclude", dynamic_condition_exclude);
        properties.setProperty("path", path);
        return properties;
    }


    public void validSelf() {
        String tempFilePath = System.getProperty("java.io.tmpdir");
        if (StringUtils.isEmpty(tempFilePath) || !Files.exists(Paths.get(tempFilePath))
                || !Files.isDirectory(Paths.get(tempFilePath))) {
            throw UnifiedException.gen(tempFilePath + " 不是目录或者不存在");
        }
        this.setPath(tempFilePath);

        if (StringUtils.isEmpty(db_schema)) {
            throw UnifiedException.gen("db_schema 不能为空");
        }
        if (StringUtils.isEmpty(db_url)) {
            throw UnifiedException.gen("db_url 不能为空");
        }
        if (StringUtils.isEmpty(db_user)) {
            throw UnifiedException.gen("db_user 不能为空");
        }
        if (StringUtils.isEmpty(db_pwd)) {
            throw UnifiedException.gen("db_pwd 不能为空");
        }
        if (StringUtils.isEmpty(table_names)) {
            throw UnifiedException.gen("table_names 不能为空");
        }

        if (StringUtils.isEmpty(author)) {
            this.setAuthor("linus");
        }
        if (StringUtils.isEmpty(current_date)) {
            this.setCurrent_date(DateUtil.formatByDateTimeFormatter(new Date()));
        }

        if (StringUtils.isEmpty(gen_domain)) {
            this.setGen_domain("false");
        }
        if (StringUtils.isEmpty(domain_package)) {
            throw UnifiedException.gen("domain_package 不能为空");
        }

        if (StringUtils.isEmpty(gen_dto)) {
            this.setGen_dto("false");
        }
        if (StringUtils.isEmpty(dto_package)) {
            throw UnifiedException.gen("dto_package 不能为空");
        }

        if (StringUtils.isEmpty(gen_mapper)) {
            this.setGen_mapper("false");
        }
        if (StringUtils.isEmpty(mapper_dao_package)) {
            throw UnifiedException.gen("mapper_dao_package 不能为空");
        }

    }

}
