package csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class csvTest {
    private final static String url = "jdbc:mysql://172.16.4.89:4007/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance&characterEncoding=UTF8";
    private final static String username = "root";
    private final static String password = "";
    private final static int colLength = 50;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String value = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/yuyang/Desktop/test.gbk.csv"), Charset.forName("GBK")));
                String line = null;
                preparedStatement = connection.prepareStatement("insert into test.gbk values(?,?,?)");
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    value = "";
                    String item[] = line.split(",");
                    for (int i = 0; i < item.length; i++) {
                        preparedStatement.setObject(i + 1, item[i]);
                    }
                    preparedStatement.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
}
