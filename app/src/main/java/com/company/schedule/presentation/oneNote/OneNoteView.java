package com.company.schedule.presentation.oneNote;

import com.company.schedule.model.data.base.Note;

public interface OneNoteView {
    void goToMainActivity();
    // actions with textView
    void setStatusDone();
    void setStatusLater();
    void setStatusToBeDone();
    // button done
    void setBtnDoneVisible();
    void setBtnDoneInvisible();  // setVisible(Gone)
    // button later
    void setBtnLaterVisible();
    void setBtnLaterInvisible();  // setVisible(Gone)

    // Timer
    void setTimerText(String timeInFormat);
    void setBtnTimerText(String btnAction);
    // message show
    void showMessage(String text);
    void showErrorMessage(String error);

    Note getNote();// interface View should be stupid, and it should have only void methods
}
