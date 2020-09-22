package chengdu;

import java.sql.*;
import java.util.Random;

public class statementTest {

    private static int threadNum = 10;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < threadNum; i++) {
            StatmentJob job = new StatmentJob();
            job.start();
        }
    }

}

class StatmentJob extends Thread {

    private static String url = "jdbc:mysql://172.16.4.194:4000/test";
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
            Statement statment = connection.createStatement();
            while (true) {
                for (int i = 0; i < 50; i++) {
                    statment.addBatch(insertSql);
                }
                statment.executeBatch();
                connection.commit();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
