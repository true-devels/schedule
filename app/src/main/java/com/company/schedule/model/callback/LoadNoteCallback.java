package com.company.schedule.model.callback;

import com.company.schedule.model.data.base.Note;

import java.util.List;

public interface LoadNoteCallback {
    void onLoadData(List<Note> myNewNotes); //load data

}
