package com.company.schedule.database.data_source;

import com.company.schedule.database.Note;

import java.util.List;

import io.reactivex.Flowable;

public interface NoteDataSource {
    Flowable<Note> getOneNote(int id);
    Flowable<List<Note>> getAllNotes();
    void insertNote(Note... notes);
    void updateNote(Note... notes);
    void deleteNote(Note note);
    void deleteAllNotes();
}
