package statementDemo;

import org.apache.commons.lang3.RandomStringUtils;
import util.jdbcUtil;

import java.sql.Connection;
import java.sql.Statement;

public class statementSingle {
    private final static String ip = "172.16.4.35:4007";
    private final static String db = "test";
    private final static String parameter = "";
    private final static String user = "root";
    private final static String pwd = "";
    private final static int jdbcVersion = 5;
    private final static int isAutoCommit = 1;
    private final static int colLength = 50;
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = jdbcUtil.getConncetion(ip, db, parameter, user, pwd, jdbcVersion, isAutoCommit);
            jdbcUtil.initStatement(connection);
            String sql = "insert into t1(c1,c2) values('" + RandomStringUtils.randomAlphabetic(colLength) + "','" + RandomStringUtils.randomAlphabetic(colLength) + "')";
            statement = jdbcUtil.initStatement(connection);
            jdbcUtil.executeStatement(statement, sql);
            jdbcUtil.commit(connection);
        } finally {
            jdbcUtil.closeStatement(statement);
            jdbcUtil.closeConnection(connection);
        }
    }
}
