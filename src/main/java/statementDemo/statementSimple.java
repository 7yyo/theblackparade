package statementDemo;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;

public class statementSimple {
    private final static String url = "jdbc:mysql://172.16.4.35:4007/test";
    private final static String username = "root";
    private final static String password = "";
    private final static int colLength = 50;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            String sql = "desc SELECT i.order_id,i.id,i.price FROM order_item AS i ,  order_entry as e where e.order_id = i.order_id and e.member_id = 'abcdefghi' and (i.status = 'active' or e.member_id = '') ORDER BY i.id DESC";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                System.out.println(rs.getString("id"));
            }
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}