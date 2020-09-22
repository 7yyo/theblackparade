package ZTJC;

import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.TimeUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class ParseCommit {

    private static int threadNum = 10;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        Timer t = new Timer();
//        TimeTask task = new TimeTask();
//        t.schedule(task, 0, 1000);
        for (int i = 0; i < threadNum; i++) {
            MySQLTask mysqlTask = new MySQLTask();
            mysqlTask.start();
        }
    }
}

class TimeTask extends TimerTask {
    int i = 0;
    int time = 600;

    @Override
    public void run() {
        if (i < time) {
            System.out.println(i++);
        } else {
            System.exit(0);
        }
    }
}

class MySQLTask extends Thread {

    private static String url = "jdbc:mysql://172.16.4.103:4000/test?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSqlLimit=1&prepStmtCacheSize=1";
    private static String username = "root";
    private static String password = "";
    private static int forCount = 10;

    @Override
    public void run() {
        try {
            System.out.println("start thread : " + Thread.currentThread().getId());
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            String sql = "";
            sql = "select * from t1 where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            while(true){
//            sql = "insert into t1 values (1,?,'yuyang',3,'4',5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);";
                preparedStatement.setString(1, "111111111");
                preparedStatement.execute();
                connection.commit();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}