package prepareTest;

import org.apache.commons.lang3.RandomStringUtils;
import util.jdbcUtil;
import util.threadPoolUtil;

import java.sql.*;

public class statementThread {
    private static int threadNum = 5;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        threadPoolUtil.startJob(1, new StatmentJob());
    }
}

class StatmentJob extends Thread {
    private final static String ip = "172.16.4.194:4000";
    private final static String db = "test";
    private final static String parameter = "";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 1;
    @Override
    public void run() {
        Connection connection = null;
        Statement statement = null;
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            jdbcUtil.initStatement(connection);
            while (true) {
                String sql = "insert into t1(c1,c2) values('" + RandomStringUtils.randomAlphabetic(50) + "','" + RandomStringUtils.randomAlphabetic(50) + "')";
                statement = jdbcUtil.initStatement(connection);
                jdbcUtil.executeStatement(statement, sql);
                jdbcUtil.commit(connection);
            }
        } finally {
            jdbcUtil.closeStatement(statement);
            jdbcUtil.closeConnection(connection);
        }
    }
}
