package prepareTest;

import config.jdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class mysqlPrepare {

    private final static String ip = "172.16.4.104:4000";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&cachePrepStmts";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 1;

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = jdbcUtil.getConncetion(ip,db,parameter,user,pwd,jdbcVersion,isAutoCommit);
            statement = connection.createStatement();
            statement.execute("PREPARE stmt FROM 'insert into t1 values(1,?)'");
            statement.execute("set @parameter = 123");
            statement.execute("EXECUTE stmt USING @a;");
            connection.commit();
        } catch (SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}
