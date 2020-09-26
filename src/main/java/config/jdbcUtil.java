package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class jdbcUtil {

    private final static int jdbc_5 = 5;
    private final static int jdbc_8 = 8;

    public static Connection getConncetion(String ip, String database, String parameter, String username, String password, int jdbcVersion,int isAutoCommit) {
        String url = "jdbc:mysql://" + ip + "/" + database + "?" + parameter;
        Connection connection = null;
        try {
            switch (jdbcVersion) {
                case jdbc_5:
                    Class.forName("com.mysql.jdbc.Driver");
                    break;
                case jdbc_8:
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    break;
            }
            connection = DriverManager.getConnection(url, username, password);
            switch (isAutoCommit){
                case 0:
                    connection.setAutoCommit(true);
                    break;
                case 1:
                    connection.setAutoCommit(false);
                    break;
            }
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
