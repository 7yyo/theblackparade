package util;

import com.zaxxer.hikari.HikariDataSource;

public class hikariUtil {
    private static final String prepareJdbcUrl = "jdbc:mysql://172.16.4.194:4000/test?useServerPrepStmts=true&useConfigs=maxPerformance";
    private static final String username = "root";
    private static final String password = "";
    private static final String jdbcDriver = "com.mysql.jdbc.Driver";
    private static final int maximumPoolSize = 1000;
    private static final int minimumIdle = 200;

    public static HikariDataSource getHikari(){
        System.out.println("start init hikari..");
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(prepareJdbcUrl);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setDriverClassName(jdbcDriver);
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        hikariDataSource.setMinimumIdle(minimumIdle);
        hikariDataSource.setAutoCommit(true);
        System.out.println("init hikari success..");
        return hikariDataSource;
    }
}
