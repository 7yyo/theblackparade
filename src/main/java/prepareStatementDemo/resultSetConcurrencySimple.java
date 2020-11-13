package prepareStatementDemo;

import java.sql.*;

public class resultSetConcurrencySimple {

    private final static String url = "jdbc:mysql://localhost:3306/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance";
    private final static String username = "root";
    private final static String password = "yuyang@123";
    private final static String sql = "select id,dt1,dt2 from t3 where id = ?";
    private final static int colLength = 50;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setObject(1, "2");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("id"));
                System.out.println(resultSet.getTimestamp("dt1"));
                System.out.println(resultSet.getTimestamp("dt2"));
            }
            resultSet.beforeFirst();
            while (resultSet.next()) {
                //Retrieve by column name
                String id = resultSet.getString("id") + 5000;
                resultSet.updateString("id", id);
                resultSet.updateRow();
            }
            resultSet.beforeFirst();
            while (resultSet.next()) {
                //Retrieve by column name
                System.out.println(resultSet.getString("id"));
                System.out.println(resultSet.getTimestamp("dt1"));
                System.out.println(resultSet.getTimestamp("dt2"));
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }

}
