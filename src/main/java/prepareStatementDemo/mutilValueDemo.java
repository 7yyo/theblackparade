package prepareStatementDemo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class mutilValueDemo {

    private final static String url = "jdbc:mysql://localhost:3306/test?" +
            "useServerPrepStmts=true&" +
            "cachePrepStmts=true&" +
            "useConfigs=maxPerformance&" +
            "characterEncoding=utf-8&" +
            "useUnicode=true&" +
            "useSSL=false&" +
            "rewriteBatchedStatements=true&" +
            "allowMultiQueries=true&" +
            "prepStmtCacheSize=250&" +
            "prepStmtCacheSqlLimit=40960&" +
            "rewriteBatchedStatements=true";
    private final static String username = "root";
    private final static String password = "yuyang@123";
//    private final static String sql = "insert into test(ticket_id,pool_id,selection_no) values(?,?,?)";
//    private final static String sql = "insert into test1(f1,f2,f3) values(?,?,?)";
//    private final static String sql = "insert into test2(ticket_id,pool_id,t1_id) values(?,?,?)";
    private final static String sql = "insert into test (ticket_id,pool_id,selection_no) select f1,f2,f3 from test1;";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, username, password);
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < 3; i++) {
            System.out.println(new BigDecimal(2330536870911814121.0).add(new BigDecimal(i)));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }
}
