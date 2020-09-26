package util;

import java.util.Timer;
import java.util.TimerTask;

public class timerUtil {
    public static void timerTool(int delayTime, int time) {
        Timer timer = new Timer();
        timeTask task = new timeTask(delayTime, time);
        timer.schedule(task, delayTime, time);
    }
}

class timeTask extends TimerTask {
    private int durationTime;
    private int time;
    public timeTask(int durationTime, int time) {
        this.durationTime = durationTime;
        this.time = time;
    }
    @Override
    public void run() {
        if (durationTime < time) {
            System.out.println("durationTime is " + durationTime++ + "s");
        } else {
            System.exit(0);
        }
    }
}

