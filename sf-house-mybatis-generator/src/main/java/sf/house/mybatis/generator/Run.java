package sf.house.mybatis.generator;


import sf.house.mybatis.generator.core.DB2Domain;
import sf.house.mybatis.generator.core.DB2Dto;
import sf.house.mybatis.generator.core.DB2Mapper;
import sf.house.mybatis.generator.util.PropertiesLoad;
import sf.house.mybatis.generator.util.base.DBUtils;

/**
 * Created by nijianfeng on 18/1/29.
 */
public class Run {

    public static void main(String[] args) {
        PropertiesLoad.init("mapper.properties");
        // DB2Domain
        DB2Domain db2Domain = new DB2Domain();
        db2Domain.genDomain();
        // DB2Dto
        DB2Dto db2Dto = new DB2Dto();
        db2Dto.genDto();
        // DB2Mapper
        DB2Mapper db2Mapper = new DB2Mapper();
        db2Mapper.genMapper();
        db2Mapper.genDao();

        DBUtils.closeConn();
    }
}
