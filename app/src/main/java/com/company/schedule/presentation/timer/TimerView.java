package com.company.schedule.presentation.timer;

public interface TimerView {
    // timer actions
    void setTimerText(String text);
    void startTimer();
    void pauseTimer();
    void resumeTimer();
    void stopTimer();
    // message show
    void showMessage(String text);
}
