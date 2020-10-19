package streamDemo;

import java.sql.*;

public class streamDemo {

    // &defaultFetchSize=-2147483648
    private final static String url = "jdbc:mysql://172.16.4.105:4000/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance";
    private final static String username = "root";
    private final static String password = "yuyang@123";
    private final static String sql = "select * from t2";

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("c1"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
    }
}
