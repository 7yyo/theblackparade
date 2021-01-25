package util;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;

public class jdbcUtil {

    private final static int jdbc_5 = 5;
    private final static int jdbc_8 = 8;
    private final static int auto_commit = 0;
    private final static int no_auto_commit = 1;
    private final static int colLength = 50;

    /* get connection */
    public static Connection getConncetion(String ip, String database, String parameter, String username, String password, int jdbcVersion, int isAutoCommit) {
        String url = "jdbc:mysql:loadbalance://" + ip + "/" + database + "?" + parameter;
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
            if (connection != null) {
                switch (isAutoCommit) {
                    case auto_commit:
                        connection.setAutoCommit(true);
                        break;
                    case no_auto_commit:
                        connection.setAutoCommit(false);
                        break;
                }
                System.out.println("thread " + Thread.currentThread().getId() + " is start ");
                System.out.println("url = " + url);
                System.out.println("autocommit = " + isAutoCommit);
            } else {
                System.out.println("init connection fail");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /* init statement*/
    public static Statement initStatement(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return statement;
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

    /* begin to execute statement*/
    public static void executeStatement(Statement statement, String sql) {
        try {
            System.out.println(sql);
            statement.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /* begin to execute prepare */
    public static void executePrepare(PreparedStatement preparedStatement, int valuesNum) {
        try {
            for (int i = 1; i < valuesNum + 1; i++) {
                preparedStatement.setObject(i, RandomStringUtils.randomAlphabetic(colLength));
            }
//            System.out.println(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /* begin to execute prepare batch */
    public static void executePrepareBatch(PreparedStatement preparedStatement, int batchNum, int valuesNum) {
        try {
            for (int i = 0; i < batchNum; i++) {
                for (int j = 1; j < valuesNum + 1; j++) {
                    preparedStatement.setObject(j, RandomStringUtils.randomAlphabetic(colLength));
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /* rollback */
    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /* commit */
    public static void commit(Connection connection) {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /* close preparestatment */
    public static void closePrepareStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /* close statement*/
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
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
