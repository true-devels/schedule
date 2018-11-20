package com.company.schedule.presentation.oneNote;

import com.company.schedule.model.data.base.Note;

public interface OneNoteView {
    void goToMainActivity();
    void onLaterButtonClicked();
    void onDoneButtonClicked();
    void onDoneCanceled();
    void onLaterCanceled();
    // Timer
    // actions
    void setTimerText(String timeInFormat);
    void setBtnTimerText(String btnAction);
    /*    void startTimer();
        void pauseTimer();
        void resumeTimer();
        void stopTimer();*/
    // message show
    void showMessage(String text);
    void showErrorMessage(String error);

    Note getNote();// interface View should be stupid, and it should have only void methods
}
