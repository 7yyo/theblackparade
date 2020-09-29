package timeOutDemo;

import java.sql.*;

public class queryTimeOutDemo {
    private final static String url = "jdbc:mysql://172.16.4.104:4000/test?useServerPrepStmts=true&cachePrepStmts=true";
    private final static String username = "root";
    private final static String password = "yuyang@123";
    private final static String sql = "select * from t2 order by c1";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(1);
            System.out.println("begin to execute = " + sql);
            preparedStatement.execute();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
}
