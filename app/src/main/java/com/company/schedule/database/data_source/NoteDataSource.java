package com.company.schedule.database.data_source;

import com.company.schedule.database.Note;

import java.util.List;

import io.reactivex.Flowable;

public interface NoteDataSource {
    Flowable<Note> getOneNotify(int id);
    Flowable<List<Note>> getAllNotifies();
    void insertNotify(Note... notifies);
    void updateNotify(Note... notifies);
    void deleteNotify(Note notify);
    void deleteAllNotifies();
}
