package parse;

import org.apache.commons.lang3.RandomStringUtils;
import util.jdbcUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class parsePrepare {

    private static int threadNum = 50;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < threadNum; i++) {
            Job job = new Job();
            job.start();
        }
    }
}

class Job extends Thread {

    private final static String ip = "172.16.4.104:4000";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&cachePrepStmts?useConfigs=maxPerformance&sessionVariables=tidb_batch_commit=1&rewriteBatchedStatements=true&allowMultiQueries=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 0;

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            String insertSql = "insert into t2 values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(insertSql);
            while (true) {
                for (int i = 0; i < 50; i++) {
                    preparedStatement.setObject(1, i++);
                    for (int j = 2; j < 26; j++) {
                        preparedStatement.setString(j,RandomStringUtils.randomAlphabetic(50));
                    }
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.closePrepareStatement(preparedStatement);
            jdbcUtil.closeConnection(connection);
        }
    }
}



