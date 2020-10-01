package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class threadPoolUtil {

    public static void startJob(int threadNum, Object job) {
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(threadNum);
            for (int i = 0; i < threadNum; i++) {
                executorService.execute((Runnable) job);
            }
        } finally {
            executorService.shutdown();
        }
    }
}
