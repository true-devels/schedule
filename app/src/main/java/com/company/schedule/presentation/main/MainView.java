package com.company.schedule.presentation.main;

import com.company.schedule.model.data.base.Note;

import java.util.List;

public interface MainView {

    void setAllNotes(List<Note> newNotes);
    void showMessage(String toast_message);
    void showMessageLong(String toast_message);
}
