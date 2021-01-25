package util;

import pojo.TpsCountBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class timerUtil {
    public static void timerTool(int delayTime, int time, int durationTime) {
        Timer timer = new Timer();
        timeTask task = new timeTask(durationTime);
        timer.schedule(task, delayTime, time);
    }

    public static void tpsTool(int delayTime, int time, int durationTime, TpsCountBean tpsCountBean, int rate) {
        Timer timer = new Timer();
        tpsTask task = new tpsTask(durationTime, tpsCountBean, rate);
        timer.schedule(task, delayTime, time);
    }

    public static void main(String[] args) {
        timerTool(0, 500, 20);
    }
}

class timeTask extends TimerTask {
    private int durationTime;
    private int nowTime;

    public timeTask(int durationTime) {
        this.durationTime = durationTime;
    }

    @Override
    public void run() {
        if (nowTime < durationTime) {
            System.out.println("durationTime is " + ++nowTime + "s");
        } else {
            System.exit(0);
        }
    }
}

class tpsTask extends TimerTask {
    private int durationTime;
    private int nowTime;
    private TpsCountBean tpsCount;
    private int rate;

    public tpsTask(int durationTime, TpsCountBean tpsCount, int rate) {
        this.durationTime = durationTime;
        this.tpsCount = tpsCount;
        this.rate = rate;
    }

    @Override
    public void run() {
        if (nowTime < durationTime) {
            ++nowTime;
            if (nowTime % rate == 0) {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()) + " || detail:  " + tpsCount.getTransactionCount().intValue() + "/" + nowTime + " | tps: " + tpsCount.getTransactionCount().intValue() / nowTime + "/s");
            }
        } else {
            System.exit(0);
        }
    }

    public static void updateTimestamp(String timestamp){

    }

}

