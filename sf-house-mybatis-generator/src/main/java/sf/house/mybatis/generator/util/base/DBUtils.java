package sf.house.mybatis.generator.util.base;


import sf.house.bean.excps.UnifiedException;
import sf.house.mybatis.generator.model.Table;
import sf.house.mybatis.generator.util.Constants;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nijianfeng on 18/1/29.
 */
public abstract class DBUtils {

    public abstract Table getTable(String tableName);

    public abstract List<String> getTableNames();

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    protected synchronized static Connection getConnection() {
        try {
            String key = Constants.dbUrl + Constants.dbUser;
            Connection connection = connectionMap.get(key);
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(Constants.dbUrl, Constants.dbUser, Constants.dbPwd);
                connectionMap.put(key, connection);
            }
            return connection;
        } catch (Exception e) {
            String message = "get db connection failed.";
            throw UnifiedException.gen(message);
        }
    }

    public static void closeConn() {
        close(null, null, connectionMap.get(Constants.dbUrl + Constants.dbUser));
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
