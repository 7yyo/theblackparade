package statementDemo;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;

public class statementSimple {
    private final static String url = "jdbc:mysql://172.16.4.194:4000/test";
    private final static String username = "root";
    private final static String password = "";
    private final static int colLength = 50;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            String sql = "insert into t1(c1,c2) values(" + RandomStringUtils.randomAlphabetic(colLength) + "," + RandomStringUtils.randomAlphabetic(colLength) + ")";
            statement.execute(sql);
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}