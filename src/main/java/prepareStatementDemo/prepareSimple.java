package prepareStatementDemo;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class prepareSimple {
    private final static String url = "jdbc:mysql://172.16.4.194:4000/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance";
    private final static String username = "root";
    private final static String password = "";
    private final static String sql = "insert into t1(c1,c2) values(?,?)";
    private final static int colLength = 50;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < 10000; i++) {
                preparedStatement.setObject(1, RandomStringUtils.randomAlphabetic(colLength));
                preparedStatement.setObject(2, RandomStringUtils.randomAlphabetic(colLength));
                preparedStatement.execute();
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
}
