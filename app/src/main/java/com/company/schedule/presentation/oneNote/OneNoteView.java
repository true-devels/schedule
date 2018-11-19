package com.company.schedule.presentation.oneNote;

public interface OneNoteView {
    void goToMainActivity();
    void setStatusLater();
    void setStatusDone();
    void setStatusToBeDone();
    void setBtnDoneVisible();
    void setBtnDoneInvisible();  // setVisible(Gone)
    void setBtnLaterVisible();
    void setBtnLaterInvisible();  // setVisible(Gone)

}
