package loadbalance;

import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadbalanceDemo {

    private static final int t = 1;

    public static void main(String[] args) {
        ExecutorService e = null;
        try {
            e = Executors.newFixedThreadPool(t);
            for (int i = 0; i < t; i++) {
                e.execute(new LoadBalancePrepareJob());
            }
        } finally {
            assert e != null;
            e.shutdown();
        }
    }

}

class LoadBalancePrepareJob implements Runnable {

    private final static String url = "jdbc:mysql:loadbalance://172.16.4.35:4007,172.16.4.34:4007,172.16.4.35:4007/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=10000&useConfigs=maxPerformance&rewriteBatchedStatements=true";
    //    private final static String url = "jdbc:mysql://172.16.4.35:8000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=10000&useConfigs=maxPerformance&rewriteBatchedStatements=true";
    private final static String userName = "root";
    private final static String password = "";
    private final static String sql = "insert into t(c1,c2) values(?,?)";

    @Override
    public void run() {
        System.out.println("Thread -" + Thread.currentThread().getId() + " start...");
        Connection c = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url, userName, password);
            if (c != null) {
                c.setAutoCommit(false);
                ps = c.prepareStatement(sql);
            }
            if (ps != null) {
                while (true) {
                    ps.setObject(1, RandomStringUtils.randomAlphabetic(4));
                    ps.setObject(2, RandomStringUtils.randomAlphabetic(4));
                    ps.execute();
                    c.commit();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert ps != null;
                ps.close();
                c.close();
                System.out.println("Thread - " + Thread.currentThread().getId() + " connection close");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
