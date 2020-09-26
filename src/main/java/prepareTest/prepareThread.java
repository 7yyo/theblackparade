package prepareTest;

import util.threadPoolUtil;
import util.jdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class prepareThread {
    private static int threadNum = 2;

    public static void main(String[] args) {
        threadPoolUtil.startJob(2, new prepareJob());
    }
}

class prepareJob implements Runnable {
    private final static String ip = "172.16.4.194:4000";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&cachePrepStmts?useConfigs=maxPerformance&sessionVariables=tidb_batch_commit=1&rewriteBatchedStatements=true&allowMultiQueries=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 1;
    private final static String sql = "insert into t1(c1,c2) values(?,?)";

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
            if (preparedStatement != null) {
                while (true) {
                    jdbcUtil.executePrepareBatch(preparedStatement, 10, 2, sql);
                    jdbcUtil.commit(connection);
                }
            }
        } finally {
            jdbcUtil.closePrepareStatement(preparedStatement);
            jdbcUtil.closeConnection(connection);
        }
    }
}