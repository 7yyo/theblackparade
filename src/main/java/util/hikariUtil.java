package util;

import com.zaxxer.hikari.HikariDataSource;

public class hikariUtil {
    private static final String prepareJdbcUrl = "jdbc:mysql:loadbalance://172.16.4.88:4007,172.16.4.89:4007,172.16.4.91:4007/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=10000&rewriteBatchedStatements = true&useConfigs=maxPerformance";
//    private static final String prepareJdbcUrl = "jdbc:mysql:loadbalance://172.16.4.89:4007/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=10000&rewriteBatchedStatements = true&useConfigs=maxPerformance";
//private static final String prepareJdbcUrl = "jdbc:mysql:loadbalance://172.16.4.89:8000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=10000&rewriteBatchedStatements = true&useConfigs=maxPerformance";
    private static final String username = "root";
    private static final String password = "yuyang@123";
    private static final String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private static final int maximumPoolSize = 240;
    private static final int minimumIdle = 0;

    public static HikariDataSource getHikari(){
        System.out.println("start init hikari..");
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(prepareJdbcUrl);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setDriverClassName(jdbcDriver);
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        hikariDataSource.setMinimumIdle(minimumIdle);
        hikariDataSource.setAutoCommit(false);
        System.out.println("init hikari success..");
        return hikariDataSource;
    }
}
