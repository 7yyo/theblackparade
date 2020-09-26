package prepareTest;

import util.jdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class prepareSingle {
    private final static String ip = "172.16.4.194:4000";
    private final static String db = "test";
    private final static String parameter = "useServerPrepStmts=true&cachePrepStmts=true";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 0;
    private final static String sql = "insert into t1(c1,c2) values(?,?)";

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            preparedStatement = jdbcUtil.initPrepareStatement(connection, sql);
            jdbcUtil.executePrepareBatch(preparedStatement, 10, 2, sql);
        } finally {
            jdbcUtil.closePrepareStatement(preparedStatement);
            jdbcUtil.closeConnection(connection);
        }
    }
}

