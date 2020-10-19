package jar;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BatchInsertJar {

    public static void main(String[] args) {
        String url = System.getProperty("url");
        String username = System.getProperty("u");
        String password = System.getProperty("p");
        String threadNum = System.getProperty("t");
        String batchNum = System.getProperty("b");
        int threadNumI = Integer.valueOf(threadNum);
        int batchNumI = Integer.valueOf(batchNum);
        System.out.println(url);
        System.out.println(username);
        System.out.println(password);
        System.out.println(threadNumI);
        System.out.println(batchNumI);
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(threadNumI);
            for (int i = 0; i < threadNumI; i++) {
                executorService.execute(new job(url, username, password, batchNumI));
            }
        } finally {
            executorService.shutdown();
        }
    }
}

class job implements Runnable {
    private String url;
    private String username;
    private String password;
    private int batchNum;
    private String sql = "insert into tt1(id,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public job(String url, String username, String password, int batchNum) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.batchNum = batchNum;
    }
    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            Random random = new Random();
            if (preparedStatement != null) {
                while (true) {
                    for (int i = 0; i < batchNum; i++) {
                        preparedStatement.setInt(1, random.nextInt(999999999));
                        preparedStatement.setString(2, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(3, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(4, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(5, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(6, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(7, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(8, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(9, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(10, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(11, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(12, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(13, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(14, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(15, RandomStringUtils.randomAlphabetic(20));
                        preparedStatement.setString(16, "2020-10-19 10:10:10");
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                    connection.commit();
                }
            }
        } catch (ClassNotFoundException | SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

