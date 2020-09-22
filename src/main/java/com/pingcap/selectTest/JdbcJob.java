package com.pingcap.selectTest;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JdbcJob {

    private static final String url = "jdbc:mysql://172.16.249.172:3306/test?useServerPreparedStmts=true";
    private static final String username = "root";
    private static final String password = "yuyang@123";
    private static final int connectionCount = 100;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(200);
        for (int i = 0; i < connectionCount; i++) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                connection.setAutoCommit(false);
                threadPool.execute(new Job(connection));
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
    }

}

class Job implements Runnable {

    private final Connection connection;

    public Job(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5);
                String sql = "select * from person";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute(sql);
                connection.commit();
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
