package joda;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.*;

public class JodaTimeDemo {

    private final static String URL = "jdbc:mysql://172.16.4.35:4007/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance&sessionVariables=tidb_txn_mode=optimistic;";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "yuyang@123";
    private final static String SQL = "select ts from t where id = ?";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            ps = c.prepareStatement(SQL);
            ps.setObject(1, 1);
            rs = ps.executeQuery();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-mm-dd");
            while (rs.next()) {
                System.out.println(rs.getString("ts"));
                System.out.println(DateTime.parse(rs.getString("ts"), formatter));
            }
        } finally {
            if (ps != null) ps.close();
            if (c != null) c.close();
        }
    }
}
