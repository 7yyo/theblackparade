package util;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class initDataUtil {
    private final static String url = "jdbc:mysql://172.16.4.104:4000/test";
    private final static String username = "root";
    private final static String password = "yuyang@123";

    public static void createTableBySql(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.createStatement();
                statement.execute(sql);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } finally {
            if (statement != null) jdbcUtil.closeStatement(statement);
            if (connection != null) jdbcUtil.closeConnection(connection);
        }
    }
}

