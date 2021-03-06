package vocationalWork;

import pojo.TpsCountBean;
import util.jdbcUtil;
import util.sqlFileUtil;
import util.threadPoolUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class initTransferData {
    private final static String filePath = "src/main/resources/sql/initTransferTable.sql";
    private final static String url = "jdbc:mysql://172.16.4.194:4000/test";
    private final static String username = "root";
    private final static String password = "";
    private final static int jdbcVersion = 5;
    private static int threadNum = 100;

    public static void main(String[] args) {
        TpsCountBean tpsCount = new TpsCountBean();
        sqlFileUtil.readSqlFile(jdbcVersion, url, username, password, filePath);
        threadPoolUtil.startJob(threadNum, new initTransferDataJob(tpsCount));
    }
}

class initTransferDataJob implements Runnable {
    private final static String ip = "172.16.4.194:4000";
    private final static String db = "transfer";
    private final static String parameter = "useServerPrepStmts=true&useConfigs=maxPerformance&rewriteBatchedStatements=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 1;
    private final static String sql = "insert into account_card_info(card_number,card_balance) values(?,?)";
    private final static int batchNum = 100;
    private TpsCountBean tpsCount;
    public initTransferDataJob(TpsCountBean tpsCount){
        this.tpsCount = tpsCount;
    }
    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
            if (preparedStatement != null) {
                Random random = new Random();
                while (true) {
                    for (int i = 0; i < batchNum; i++) {
                        preparedStatement.setObject(1,random.nextInt(999999999));
                        preparedStatement.setObject(2,100000000);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                    jdbcUtil.commit(connection);
                    if(tpsCount.getTransactionCount().intValue() < 10000000/100){
                        tpsCount.plusOne();
                    }else{
                        System.exit(0);
                    }

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
