package util;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class jdbcUtil {

    private final static int jdbc_5 = 5;
    private final static int jdbc_8 = 8;

    /*get connection*/
    public static Connection getConncetion(String ip, String database, String parameter, String username, String password, int jdbcVersion, int isAutoCommit) {
        String url = "jdbc:mysql://" + ip + "/" + database + "?" + parameter;
        Connection connection = null;
        try {
            switch (jdbcVersion) {
                case jdbc_5:
                    Class.forName("com.mysql.jdbc.Driver");
                    break;
                case jdbc_8:
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    break;
            }
            connection = DriverManager.getConnection(url, username, password);
            switch (isAutoCommit) {
                case 0:
                    connection.setAutoCommit(true);
                    break;
                case 1:
                    connection.setAutoCommit(false);
                    break;
            }
            connection.setAutoCommit(false);
            System.out.println("thread " + Thread.currentThread().getId() + " is start ");
            System.out.println("url = " + url);
            System.out.println("autocommit = " + isAutoCommit );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /* init prepareStatement*/
    public static PreparedStatement initPrepareStatement(Connection connection, String sql) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return preparedStatement;
    }

    /* begin to execute prepare statement*/
    public static void executePrepare(PreparedStatement preparedStatement, int batchNum, int valuesNum) {
        try {
            for (int i = 0; i < batchNum; i++) {
                for (int j = 1; j < valuesNum + 1; j++) {
                    preparedStatement.setObject(j, RandomStringUtils.randomAlphabetic(50));
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /* commit */
    public static void commit(Connection connection){
        if(connection != null){
            try {
                connection.commit();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /* close preparestatment */
    public static void closePrepare(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /* close connection */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
