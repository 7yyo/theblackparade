package tps;

import bean.c3p0ConnectionPool;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import util.jdbcUtil;
import util.threadPoolUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchTpsTest {
    private static int threadNum = 10;
    c3p0ConnectionPool c3p0ConnectionPool = new c3p0ConnectionPool();
    public static void main(String[] args) {
        c3p0ConnectionPool c3p0ConnectionPool = new c3p0ConnectionPool();
        threadPoolUtil.startJob(threadNum, new batchJob(c3p0ConnectionPool), true);
    }
}

class batchJob implements Runnable {
    private final static String sql = "insert into t1(c1,c2) values(?,?)";
    private final static int batchNum = 50;
    private final static int valuesNum = 2;
    private c3p0ConnectionPool c3p0ConnectionPool;
    public batchJob(c3p0ConnectionPool c3p0ConnectionPool){
        this.c3p0ConnectionPool = c3p0ConnectionPool;
    }
    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = c3p0ConnectionPool.getC3p0Connection();
            connection.setAutoCommit(false);
            preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
            if (preparedStatement != null) {
                while (true) {
                    jdbcUtil.executePrepareBatch(preparedStatement, batchNum, valuesNum, sql);
                    jdbcUtil.commit(connection);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            jdbcUtil.closePrepareStatement(preparedStatement);
            jdbcUtil.closeConnection(connection);
        }
    }
}
