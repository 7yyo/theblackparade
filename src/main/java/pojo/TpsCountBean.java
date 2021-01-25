package pojo;

import java.util.concurrent.atomic.AtomicInteger;

public class TpsCountBean {

    private static AtomicInteger transactionCount;

    public AtomicInteger getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(AtomicInteger transactionCount) {
        TpsCountBean.transactionCount = transactionCount;
    }

    public TpsCountBean() {
        transactionCount = new AtomicInteger(0);
    }

    public static void plusOne() {
        transactionCount.addAndGet(1);
    }
}
