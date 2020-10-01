package bean;

import java.util.concurrent.atomic.AtomicInteger;

public class tpsCountBean {

    private static AtomicInteger transactionCount;

    public AtomicInteger getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(AtomicInteger transactionCount) {
        tpsCountBean.transactionCount = transactionCount;
    }

    public tpsCountBean() {
        transactionCount = new AtomicInteger(0);
    }

    public static void plusOne() {
        transactionCount.addAndGet(1);
    }
}
