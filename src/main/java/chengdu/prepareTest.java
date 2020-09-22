package chengdu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class prepareTest {

    private static int threadNum = 10;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < threadNum; i++) {
            Job job = new Job();
            job.start();
        }
    }

}

class Job extends Thread {

    private static String url = "jdbc:mysql://172.16.4.194:4000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=128&prepStmtCacheSize=5";
    private static String username = "root";
    private static String password = "";
    private static int forCount = 10;

    @Override
    public void run() {
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            Random random = new Random();
            String insertSql = " insert into t1 values(1,?) ";
            int id = random.nextInt(99);
            String updateSql = " update t1 set c1 = ? where id = ?";
            PreparedStatement preparedStatement01 = connection.prepareStatement(insertSql);
            PreparedStatement preparedStatement02 = connection.prepareStatement(updateSql);
            while (true) {
                for (int i = 0; i < 50; i++) {
                    preparedStatement01.setObject(1, id);
                    preparedStatement01.addBatch();
                }
                preparedStatement01.executeBatch();

                preparedStatement02.setString(1, "update == " + random.nextInt(99));
                preparedStatement02.setInt(2, id);
                preparedStatement02.executeUpdate();

                connection.commit();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
