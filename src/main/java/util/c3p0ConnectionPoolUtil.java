package util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.c3p0.jboss.C3P0PooledDataSource;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class c3p0ConnectionPoolUtil {

    static ComboPooledDataSource dataSource = new ComboPooledDataSource("statement");

    public static Connection getC3p0Connection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

}
