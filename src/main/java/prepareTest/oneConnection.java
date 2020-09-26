package prepareTest;

import util.jdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class oneConnection {

    private final static String ip = "172.16.4.104:4000";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&cachePrepStmts?useConfigs=maxPerformance&sessionVariables=tidb_batch_commit=1&rewriteBatchedStatements=true&allowMultiQueries=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 0;
    private static int threadNum = 5;

    public static void main(String[] args) {
        Connection connection = null;
        connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
        for (int i = 0; i < threadNum; i++) {
            OneConnectionJob job = new OneConnectionJob(connection);
            job.start();
        }
    }
}

class OneConnectionJob extends Thread {

    private Connection connection;
    public OneConnectionJob(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        PreparedStatement preparedStatement = null;
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            Random random = new Random();
            String insertSql = "insert into t1 values(1,?)";
            preparedStatement = connection.prepareStatement(insertSql);
            int id = random.nextInt(99);
            while (true) {
                for (int i = 0; i < 5; i++) {
                    preparedStatement.setObject(1, id);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
