package parse;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class parsePrepare {

    private static int threadNum = 5;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < threadNum; i++) {
            Job job = new Job();
            job.start();
        }
    }
}

class Job extends Thread {
    // &prepStmtCacheSqlLimit=128&prepStmtCacheSize=5
    private static String url = "jdbc:mysql://172.16.4.105:4000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=8192&prepStmtCacheSize=100";
    private static String username = "root";
    private static String password = "";
    private static int forCount = 10;

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            String insertSql = "insert into t2 values(?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(insertSql);
            while (true) {
                for (int i = 0; i < 50; i++) {
                    preparedStatement.setObject(1, i++);
                    preparedStatement.setObject(2,RandomStringUtils.random(40));
                    preparedStatement.setObject(3,RandomStringUtils.random(40));
                    preparedStatement.setObject(4,RandomStringUtils.random(40));
                    preparedStatement.setObject(5,RandomStringUtils.random(40));
                    preparedStatement.setObject(6,RandomStringUtils.random(40));
                    preparedStatement.setObject(7,RandomStringUtils.random(40));
                    preparedStatement.setObject(8,RandomStringUtils.random(40));
                    preparedStatement.setObject(9,RandomStringUtils.random(40));
                    preparedStatement.setObject(10,RandomStringUtils.random(40));
                    preparedStatement.setObject(11,RandomStringUtils.random(40));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}



