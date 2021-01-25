package tps;

import pojo.TpsCountBean;
import com.zaxxer.hikari.HikariDataSource;
import util.hikariUtil;
import util.jdbcUtil;
import util.threadPoolUtil;
import util.timerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class batchTpsDemo {
    private static final int threadNum = 100;
    private static final int delayTime = 0;
    private static final int time = 1000;
    private static final int durationTime = 600;
    private static final int rate = 5;

    public static void main(String[] args) {
        TpsCountBean tpsCount = new TpsCountBean();
        timerUtil.tpsTool(delayTime, time, durationTime, tpsCount, rate);
        HikariDataSource hikariDataSource = hikariUtil.getHikari();
        threadPoolUtil.startJob(threadNum, new batchJob(hikariDataSource, tpsCount));
    }
}

class batchJob implements Runnable {
    private final static String sql = "insert into t(c1,c2) values(?,?)";
    private final static int batchNum = 100;
    private final static int valuesNum = 2;
    private HikariDataSource hikariDataSource;
    private TpsCountBean tpsCount;

    public batchJob(HikariDataSource hikariDataSource, TpsCountBean tpsCount) {
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
                connection.setAutoCommit(false);
                preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
                if (preparedStatement != null) {
                    jdbcUtil.executePrepareBatch(preparedStatement, batchNum, valuesNum);
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
