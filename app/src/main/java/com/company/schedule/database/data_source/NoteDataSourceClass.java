package com.company.schedule.database.data_source;


import com.company.schedule.database.Note;
import com.company.schedule.database.NoteDAO;

import java.util.List;

import io.reactivex.Flowable;

public class NoteDataSourceClass implements NoteDataSource {
    private NoteDAO noteDAO;
    private static NoteDataSource mInstance;

    public NoteDataSourceClass(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }
    public static NoteDataSource getInstance(NoteDAO noteDAO){
        if(mInstance==null){
            mInstance=new NoteDataSourceClass(noteDAO);
        }
        return mInstance;
    }

    @Override
    public Flowable<Note> getOneNote(int id) {
        return noteDAO.getOneNote(id);
    }

    @Override
    public Flowable<List<Note>> getAllNotes() {
        return noteDAO.getAllNotes();
    }

    @Override
    public void insertNote(Note... notes) {
        noteDAO.insertNotes(notes);
    }

    @Override
    public void updateNote(Note... notes) {
        noteDAO.updateNotes(notes);
    }

    @Override
    public void deleteNote(Note note) {
        noteDAO.deleteNote(note);
    }

    @Override
    public void deleteAllNotes() {
        noteDAO.deleteAllNotes();
    }

}
