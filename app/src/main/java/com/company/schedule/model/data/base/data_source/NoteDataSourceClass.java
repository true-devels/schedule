package com.company.schedule.model.data.base.data_source;


import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.data.base.NoteDAO;

import java.util.List;

import io.reactivex.Flowable;

public class NoteDataSourceClass {
    private NoteDAO noteDAO;
    private static NoteDataSourceClass mInstance;

    NoteDataSourceClass(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public static NoteDataSourceClass getInstance(NoteDAO noteDAO){
        if(mInstance == null){
            mInstance = new NoteDataSourceClass(noteDAO);
        }
        return mInstance;
    }

    public Flowable<Note> getOneNote(int id) {
        return noteDAO.getOneNote(id);
    }

    public Flowable<List<Note>> getAllNotes() {
        return noteDAO.getAllNotes();
    }

    public void insertNote(Note... notes) {
        noteDAO.insertNotes(notes);
    }

    public void updateNote(Note... notes) {
        noteDAO.updateNotes(notes);
    }

    public void deleteNote(Note note) {
        noteDAO.deleteNote(note);
    }

    public void deleteAllNotes() {
        noteDAO.deleteAllNotes();
    }

}
