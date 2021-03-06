package timeOutDemo;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class socketTimeOutDemo {
    private final static String url = "jdbc:mysql://172.16.4.104:4000/test?useServerPrepStmts=true&cachePrepStmts=true&socketTimeout=1";
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
            System.out.println(sql);
            preparedStatement.execute();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
}
