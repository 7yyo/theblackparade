package csv;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CsvTest {

    private final static String URL = "jdbc:mysql://172.16.4.35:4007/test?useServerPrepStmts=true&cachePrepStmts=true&useConfigs=maxPerformance&characterEncoding=UTF8";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";
    private final static String CHARSET = "GBK";
    private final static String CSVFILE = "file.csv";
    private final static String SQL = "insert into test.gbk values(?,?,?)";
    private final static String DELIMITER = ",";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            try {
                InputStreamReader ir = new InputStreamReader(new FileInputStream(CSVFILE), Charset.forName(CHARSET));
                BufferedReader r = new BufferedReader(ir);
                String line;
                ps = c.prepareStatement(SQL);
                r.readLine();
                while ((line = r.readLine()) != null) {
                    String[] item = line.split(DELIMITER);
                    for (int i = 1; i < item.length; i++) {
                        ps.setObject(i, item[i]);
                    }
                    ps.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (ps != null) ps.close();
            if (c != null) c.close();
        }
    }
}
