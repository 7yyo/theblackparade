package parse;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class parsePrepare {

    private static int threadNum = 50;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < threadNum; i++) {
            Job job = new Job();
            job.start();
        }
    }
}

class Job extends Thread {

    private static String url = "jdbc:mysql://172.16.4.105:4000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=81920&prepStmtCacheSize=1000&rewriteBatchedStatements=true&allowMultiQueries=true";
    private static String username = "root";
    private static String password = "";
    private static int mutilCount = 26;

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            String insertSql = "insert into t2 values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(insertSql);
            while (true) {
                for (int i = 0; i < 500; i++) {
                    preparedStatement.setObject(1, i++);
                    for (int j = 2; j < mutilCount; j++) {
                        preparedStatement.setObject(j, RandomStringUtils.randomAlphabetic(50));
                    }
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



