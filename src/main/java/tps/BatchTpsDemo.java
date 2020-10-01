package tps;

import bean.tpsCountBean;
import com.zaxxer.hikari.HikariDataSource;
import util.hikariUtil;
import util.jdbcUtil;
import util.threadPoolUtil;
import util.timerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchTpsDemo {
    private static final int threadNum = 100;
    private static final int delayTime = 0;
    private static final int time = 1000;
    private static final int durationTime = 600;
    private static final int rate = 5;

    public static void main(String[] args) {
        tpsCountBean tpsCount = new tpsCountBean();
        timerUtil.tpsTool(delayTime, time, durationTime, tpsCount, rate);
        HikariDataSource hikariDataSource = hikariUtil.getHikari();
        threadPoolUtil.startJob(threadNum, new batchJob(hikariDataSource, tpsCount));
    }
}

class batchJob implements Runnable {
    private final static String sql = "insert into t1(c1,c2) values(?,?)";
    private final static int batchNum = 10;
    private final static int valuesNum = 2;
    private HikariDataSource hikariDataSource;
    private tpsCountBean tpsCount;

    public batchJob(HikariDataSource hikariDataSource, tpsCountBean tpsCount) {
        this.hikariDataSource = hikariDataSource;
        this.tpsCount = tpsCount;
    }

    @Override
    public void run() {
        while (true) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try {
                connection = hikariDataSource.getConnection();
                preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
                if (preparedStatement != null) {
                    jdbcUtil.executePrepareBatch(preparedStatement, batchNum, valuesNum, sql);
                    jdbcUtil.commit(connection);
                    tpsCount.plusOne();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                jdbcUtil.closePrepareStatement(preparedStatement);
                jdbcUtil.closeConnection(connection);
            }
        }
    }
}
