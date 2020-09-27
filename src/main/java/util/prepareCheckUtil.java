package util;

import java.util.ArrayList;

public class prepareCheckUtil {

    public static boolean checkSqlLegth(ArrayList<String> sqlList, int prepStmtCacheSqlLimit) {
        if (sqlList.size() > 0) {
            for (int i = 0; i < sqlList.size(); i++) {
                if (prepStmtCacheSqlLimit > sqlList.get(i).length()) {
                    return false;
                }
            }
        }
        return true;
    }
}
