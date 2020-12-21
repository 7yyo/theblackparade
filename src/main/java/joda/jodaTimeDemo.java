package joda;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.*;

public class jodaTimeDemo {
    private final static String url = "jdbc:mysql://172.16.4.89:4007/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance&sessionVariables=tidb_txn_mode=optimistic;";
    private final static String username = "root";
    private final static String password = "yuyang@123";
    private final static String sql = "select ts from t where id = ?";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, 1);
            resultSet = preparedStatement.executeQuery();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("ts"));
                System.out.println(DateTime.parse(resultSet.getString("ts"), formatter));
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
}
