package chengdu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class prepareSingle {

    private static String url = "jdbc:mysql://172.16.4.105:4000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=128&prepStmtCacheSize=5";
    private static String username = "root";
    private static String password = "";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            String insertSql = "insert into t1 values(1,?,1,1,1)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setObject(1, 1);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }
}


