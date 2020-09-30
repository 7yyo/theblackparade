package vocationalWork;

import util.initDataUtil;
import util.sqlFileUtil;

public class initTransferData {
    private final static String filePath = "src/main/resources/sql/initTransferTable.sql";
    private final static String url = "jdbc:mysql://172.16.4.104:4000/test";
    private final static String username = "root";
    private final static String password = "yuyang@123";

    public static void main(String[] args) {
        sqlFileUtil.readSqlFile(5, url, username, password, filePath);
    }
}
