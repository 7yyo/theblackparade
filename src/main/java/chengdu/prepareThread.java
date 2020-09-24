package chengdu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class prepareThread {

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
    private static String url = "jdbc:mysql://172.16.4.105:4000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=1&prepStmtCacheSize=0";
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
            Random random = new Random();
            String insertSql = "insert into t1 values(1,?)";
            preparedStatement = connection.prepareStatement(insertSql);
            int id = random.nextInt(99);
            while (true) {
                for (int i = 0; i < 50; i++) {
                    preparedStatement.setObject(1, id);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
