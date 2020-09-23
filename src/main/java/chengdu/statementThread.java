package chengdu;

import java.sql.*;
import java.util.Random;

public class statementThread {
    private static int threadNum = 5;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < threadNum; i++) {
            StatmentJob job = new StatmentJob();
            job.start();
        }
    }
}

class StatmentJob extends Thread {
    private static String url = "jdbc:mysql://172.16.4.105:4000/test";
    private static String username = "root";
    private static String password = "";
    @Override
    public void run() {
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            String sql = "insert into t1 values(1,1)";
            Statement statment = connection.createStatement();
            while (true) {
                statment.executeUpdate(sql);
                connection.commit();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
