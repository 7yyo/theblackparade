package bean;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class c3p0ConnectionPool {
    private static ComboPooledDataSource comboPooledDataSource;
    private static final String jdbcUrl = "jdbc:mysql://172.16.4.194:4000/test";
    private static final String user = "root";
    private static final String password = "";
    private static final int initialPoolSize = 200;
    private static final int minPoolSize = 200;
    private static final int maxPoolSize = 200;
    private static final int maxIdleTime = 200;

    public c3p0ConnectionPool() {
        this.comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setJdbcUrl(jdbcUrl);
        comboPooledDataSource.setUser(user);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setInitialPoolSize(initialPoolSize);
        comboPooledDataSource.setMinPoolSize(minPoolSize);
        comboPooledDataSource.setMaxPoolSize(maxPoolSize);
        comboPooledDataSource.setMaxIdleTime(maxIdleTime);
        comboPooledDataSource.setAutoCommitOnClose(false);
        System.out.println("c3p0 connectionpool init success");
    }

    public static Connection getC3p0Connection() {
        Connection connection = null;
        try {
            connection = comboPooledDataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}
