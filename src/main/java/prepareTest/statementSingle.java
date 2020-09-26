package prepareTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class statementSingle {

    private static String url = "jdbc:mysql://172.16.4.105:4000/test";
    private static String username = "root";
    private static String password = "";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statment = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            String sql = "insert into t1 values(1,12345);";
            statment = connection.createStatement();
            statment.executeUpdate(sql);
            connection.commit();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                statment.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
