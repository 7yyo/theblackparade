package util;

import java.util.Timer;
import java.util.TimerTask;

public class timerUtil {
    public static void timerTool(int delayTime, int time) {
        Timer timer = new Timer();
        timeTask task = new timeTask();
        timer.schedule(task, delayTime, time);
    }
}

class timeTask extends TimerTask {
    int durationTime = 0;
    int time = 600;
    @Override
    public void run() {
        if (durationTime < time) {
            System.out.println("durationTime is " + durationTime++ + "s");
        } else {
            System.exit(0);
        }
    }
}

