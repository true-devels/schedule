package com.company.schedule.presentation.timer;

public interface TimerView {
    // timer actions
    void setTimerText(String timeInFormat);
    void setBtnTimerText(String btnAction);
/*    void startTimer();
    void pauseTimer();
    void resumeTimer();
    void stopTimer();*/
    // message show
    void showMessage(String text);
    void showErrorMessage(String error);
}
