package prepareStatementDemo;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class prepareSimple {
    private final static String url = "jdbc:mysql://localhost:4000/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance&prepStmtCacheSqlLimit=1000";
    private final static String username = "root";
    private final static String password = "";
    private final static String sql = "insert into t(c1,c2) values(?,?)";
    private final static int colLength = 4;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < 10000; i++) {
                preparedStatement.setObject(1, RandomStringUtils.randomAlphabetic(colLength));
                preparedStatement.setObject(2, RandomStringUtils.randomAlphabetic(colLength));
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
//                System.out.println(n);
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
}
