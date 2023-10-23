package de.lukashuth;

import javax.swing.*;

public class TimeThread extends Thread {
    private final MainWindow MainWindow;
    private String formatTime(long time) {
        StringBuilder timeString = new StringBuilder();
        if (time / 60 < 10) {
            timeString.append("0");
        }
        timeString.append(time / 60).append(":");
        if (time % 60 < 10) {
            timeString.append("0");
        }
        timeString.append(time % 60);
        return timeString.toString();
    }
    @Override
    public void run() {
        while (true) {
            try {
                long time = System.currentTimeMillis() - this.MainWindow.getStartTime.get();
                MainWindow.getTime_label.get().setText(this.formatTime(time/ 1000));
                Thread.sleep(1000 - time%1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public TimeThread(MainWindow MainWindow) {
        this.MainWindow = MainWindow;
    }
}
