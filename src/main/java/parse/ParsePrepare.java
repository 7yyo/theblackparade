package parse;

import util.jdbcUtil;
import util.threadPoolUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParsePrepare {
    private static int threadNum = 50;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        threadPoolUtil.startJob(threadNum, new Job());
    }
}

class Job implements Runnable {
    private final static String ip = "172.16.4.104:4000";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&useConfigs=maxPerformance&rewriteBatchedStatements=true&allowMultiQueries=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 0;
    private final static int batchNum = 50;
    private final static int valuesNum = 26;
    private final static String sql = "insert into t2 values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
            while (true) {
                jdbcUtil.executePrepareBatch(preparedStatement, batchNum, valuesNum);
                jdbcUtil.commit(connection);
            }
        } finally {
            jdbcUtil.closePrepareStatement(preparedStatement);
            jdbcUtil.closeConnection(connection);
        }
    }
}



