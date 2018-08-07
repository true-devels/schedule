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
    public Flowable<Note> getOneNotify(int id) {
        return noteDAO.getOneNotify(id);
    }

    @Override
    public Flowable<List<Note>> getAllNotifies() {
        return noteDAO.getAllNotifies();
    }

    @Override
    public void insertNotify(Note... notifies) {
        noteDAO.insertNotes(notifies);
    }

    @Override
    public void updateNotify(Note... notifies) {
        noteDAO.updateNotes(notifies);
    }

    @Override
    public void deleteNotify(Note notify) {
        noteDAO.deleteNote(notify);
    }

    @Override
    public void deleteAllNotifies() {
        noteDAO.deleteAllNotes();
    }

}
