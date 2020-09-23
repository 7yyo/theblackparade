package jar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class prepareJar {
    public static void main(String[] args) {
        String url = System.getProperty("url");
        String username = System.getProperty("u");
        String password = System.getProperty("p");
        System.out.println(url);
        System.out.println(username);
        System.out.println(password);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            String insertSql = "insert into t1 values(1,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            for (int i = 0; i < 1000; i++) {
                preparedStatement.setObject(1, i);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }
}
