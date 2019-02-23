package sf.house.mybatis.generator.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import sf.house.bean.excps.UnifiedException;
import sf.house.mybatis.generator.model.Field;
import sf.house.mybatis.generator.model.Table;
import sf.house.mybatis.generator.util.base.DBUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by nijianfeng on 18/1/29.
 */

@Slf4j
public class MySqlUtils extends DBUtils {

//    private static final LoadingCache<String, Table> tableCache;
//
//    static {
//        tableCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<String, Table>() {
//                    @Override
//                    public Table load(String tableName) throws Exception {
//                        return getTableByName(tableName);
//                    }
//                });
//    }
//
//    private static final LoadingCache<String, List<String>> tableNameCache;
//
//    static {
//        tableNameCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<String, List<String>>() {
//                    @Override
//                    public List<String> load(String schema) throws Exception {
//                        return getNameBySchema(schema);
//                    }
//                });
//    }

    // 获取表信息
    @Override
    public Table getTable(String tableName) {
        return getTableByName(tableName);
    }

    // 获取库下所有表
    @Override
    public List<String> getTableNames() {
        return getNameBySchema(Constants.dbSchema);
    }

    private static List<String> getNameBySchema(String schema) {
        List<String> names = Lists.newArrayList();
        try {
            // 获取表名列表
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT table_name FROM Information_schema.tables WHERE table_schema = " + "'" + schema + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                names.add(rs.getString(1));
            }
            close(rs, ps, null);
        } catch (Exception e) {
            closeConn();
            throw UnifiedException.gen("获取 table name 失败", e);
        }
        return names;
    }


    private static Table getTableByName(String tableName) {
        log.info("start query " + tableName + " #################################");

        final String SELECT_FIELD = " column_name as field,";
        final String SELECT_TYPE = " data_type as type,";
        final String SELECT_MEMO = " column_comment as memo,";
        final String SELECT_NUMERIC_LENGTH = " numeric_precision as numericLength,";
        final String SELECT_NUMERIC_SCALE = " numeric_scale as numericScale, ";
        final String SELECT_IS_NULLABLE = " is_nullable as isNullable,";
        final String SELECT_IS_AUTO_INCREMENT =
                " CASE WHEN extra = 'auto_increment' THEN 'true' ELSE 'false' END as isAutoIncrement,";
        final String SELECT_IS_DEFAULT = " column_default as isDefault,";
        final String SELECT_CHARACTER_LENGTH = " character_maximum_length  AS characterLength ";
        final String SELECT_SCHEMA = "SELECT " + SELECT_FIELD + SELECT_TYPE + SELECT_MEMO
                + SELECT_NUMERIC_LENGTH + SELECT_NUMERIC_SCALE + SELECT_IS_NULLABLE + SELECT_IS_AUTO_INCREMENT
                + SELECT_IS_DEFAULT + SELECT_CHARACTER_LENGTH + " FROM Information_schema.columns "
                + "WHERE  table_schema ='" + Constants.dbSchema + "' AND table_Name = ";
        String sql = SELECT_SCHEMA + "'" + tableName + "'";
        log.info("execute sql: " + sql);
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Field> fields = Lists.newArrayList();
            while (rs.next()) {
                Field field = new Field();
                field.setField(rs.getString(1));
                field.setType(rs.getString(2));
                field.setMemo(rs.getString(3));
                field.setNumericLength(rs.getString(4));
                field.setNumericScale(rs.getString(5));
                field.setIsNullable(rs.getString(6));
                field.setIsAutoIncrement(Boolean.valueOf(rs.getString(7)));
                field.setIsDefault(rs.getString(8));
                field.setCharacterLength(rs.getString(9));
                fields.add(field);
                // 打印数据库某个表每列的返回数据
                log.info("{}", field);
            }
            // 获取表描述
            ps = getConnection()
                    .prepareStatement("SELECT table_comment FROM Information_schema.tables WHERE table_Name =" + "'" + tableName + "'");
            rs = ps.executeQuery();
            String tableComment = "无";
            while (rs.next()) {
                tableComment = rs.getString(1);
            }
            close(rs, ps, null);
            log.info("end query " + tableName + " #################################");
            return Table.builder().fields(fields).name(tableName).comment(tableComment).build();
        } catch (Exception e) {
            closeConn();
            throw UnifiedException.gen("获取schema数据失败", e);
        }
    }

}
