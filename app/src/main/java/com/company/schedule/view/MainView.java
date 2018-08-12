package com.company.schedule.view;

import android.content.Intent;

import com.company.schedule.database.Note;

import java.util.List;

public interface MainView {
    void startActivityForResult(Intent intent, int requestCode);

    void setAllNotes(List<Note> myLinks);
    void toast(String toast_message);
    void toastLong(String toast_message);
}
