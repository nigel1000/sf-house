package sf.house.mybatis.generator.util.base;


import sf.house.bean.excps.UnifiedException;
import sf.house.mybatis.generator.model.Table;
import sf.house.mybatis.generator.util.Constants;

import java.sql.*;
import java.util.List;

/**
 * Created by nijianfeng on 18/1/29.
 */
public abstract class DBUtils {

    public abstract Table getTable(String tableName);

    public abstract List<String> getTableNames();

    private static Connection connection;

    protected synchronized static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(Constants.dbUrl, Constants.dbUser, Constants.dbPwd);
            }
            return connection;
        } catch (Exception e) {
            String message = "get db connection failed.";
            throw UnifiedException.gen(message);
        }
    }

    public static void closeConn() {
        close(null, null, getConnection());
    }

    protected static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        // 关闭记录集
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                String message = "close rs failed.";
                throw UnifiedException.gen(message);
            }
        }
        // 关闭声明
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                String message = "close ps failed.";
                throw UnifiedException.gen(message);
            }
        }
        // 关闭链接对象
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                String message = "close conn failed.";
                throw UnifiedException.gen(message);
            }
        }
    }
}
