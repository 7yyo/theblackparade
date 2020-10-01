package parse;

import util.jdbcUtil;
import util.threadPoolUtil;
import util.timerUtil;

import java.sql.*;

public class parseCommit {
    private final static int threadNum = 10;
    private final static int delayTime = 0;
    private final static int time = 1000;
    private final static int durationTime = 600;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        timerUtil.timerTool(delayTime, time, durationTime);
        threadPoolUtil.startJob(threadNum, new ParseCommitJob());
    }
}

class ParseCommitJob implements Runnable {
    private final static String ip = "172.16.4.194:4000";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&cachePrepStmts&prepStmtCacheSqlLimit=2048&useConfigs=maxPerformance&rewriteBatchedStatements=true&allowMultiQueries=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 1;
    private final static int batchNum = 0;
    private final static int valuesNum = 1000;
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
                    jdbcUtil.executePrepareBatch(preparedStatement, batchNum, valuesNum, sql);
                    jdbcUtil.commit(connection);
                }
            }
        } finally {
            jdbcUtil.closePrepareStatement(preparedStatement);
            jdbcUtil.closeConnection(connection);
        }
    }
}