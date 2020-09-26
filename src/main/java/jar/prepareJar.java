package jar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class prepareJar {

}

class Job extends Thread {

    private static String url = "jdbc:mysql://localhost:4000/test?useServerPrepStmts=true&cachePrepStmts=true";
    private static String username = "root";
    private static String password = "";

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
            int id = random.nextInt(99);
            preparedStatement = connection.prepareStatement(insertSql);
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
