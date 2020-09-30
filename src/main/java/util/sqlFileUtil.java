package util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

import java.io.*;

public class sqlFileUtil {
    private static String sql = "";
    private final static int jdbc_5 = 5;
    private final static int jdbc_8 = 8;

    public static void readSqlFile(int jdbcVersion, String url, String username, String password, String filePath) {
        SQLExec sqlExec = new SQLExec();
        switch (jdbcVersion){
            case jdbc_5: sqlExec.setDriver("com.mysql.jdbc.Driver");break;
            case jdbc_8: sqlExec.setDriver("com.mysql.cj.jdbc.Driver");break;
        }
        sqlExec.setUrl(url);
        sqlExec.setUserid(username);
        sqlExec.setPassword(password);
        sqlExec.setSrc(new File(filePath));
        sqlExec.setPrint(true);
        sqlExec.setProject(new Project());
        sqlExec.execute();
    }
}
