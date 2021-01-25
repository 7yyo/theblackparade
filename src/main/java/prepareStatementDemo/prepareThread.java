package prepareStatementDemo;

import com.zaxxer.hikari.HikariDataSource;
import util.hikariUtil;
import util.threadPoolUtil;
import util.jdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class prepareThread {
    private static int threadNum = 1;
    HikariDataSource hikariDataSource = hikariUtil.getHikari();
    public static void main(String[] args) {
        threadPoolUtil.startJob(threadNum, new prepareJob());
    }
}

class prepareJob implements Runnable {
    private final static String ip = "172.16.4.89:4007,172.16.4.88:4007,172.16.4.91:4007";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance&rewriteBatchedStatements=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 1;
    private final static String sql = "insert into t(c1,c2) values(?,?)";
    private final static int batchNum = 50;
    private final static int valuesNum = 2;

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
            if (preparedStatement != null) {
                while (true) {
                    jdbcUtil.executePrepareBatch(preparedStatement, batchNum, valuesNum);
                    jdbcUtil.commit(connection);
                }
            }
        } finally {
            jdbcUtil.closePrepareStatement(preparedStatement);
            jdbcUtil.closeConnection(connection);
        }
    }
}