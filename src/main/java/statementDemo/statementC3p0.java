package statementDemo;

import bean.c3p0ConnectionPool;
import org.apache.commons.lang3.RandomStringUtils;
import util.jdbcUtil;
import util.threadPoolUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class statementC3p0 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        threadPoolUtil.startJob(1, new StatmentJob());
    }
}

class C3p0StatmentJob extends Thread {
    private final static int colLength = 50;
    @Override
    public void run() {
        Connection connection = null;
        Statement statement = null;
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            connection = c3p0ConnectionPool.getC3p0Connection();
            while (true) {
                String sql = "insert into t1(c1,c2) values('" + RandomStringUtils.randomAlphabetic(colLength) + "','" + RandomStringUtils.randomAlphabetic(colLength) + "')";
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
