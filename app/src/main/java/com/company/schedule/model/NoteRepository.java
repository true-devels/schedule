package com.company.schedule.model;

import com.company.schedule.database.Note;
import com.company.schedule.database.data_source.NoteDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class NoteRepository implements NoteDataSource {

    // TODO write comment what means (m) on start name
    private NoteDataSource mLocalDataSource;
    private static NoteRepository mInstance;

    public NoteRepository(NoteDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }
    public static NoteRepository getmInstance(NoteDataSource mLocalDataSource){
        if(mInstance==null){
            mInstance=new NoteRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<Note> getOneNote(int id) {
        return mLocalDataSource.getOneNote(id);
    }

    @Override
    public Flowable<List<Note>> getAllNotes() {
        return mLocalDataSource.getAllNotes();
    }

    @Override
    public void insertNote(Note... notes) {
        mLocalDataSource.insertNote(notes);
    }

    @Override
    public void updateNote(Note... notes) {
        mLocalDataSource.updateNote(notes);
    }

    @Override
    public void deleteNote(Note note) {
        mLocalDataSource.deleteNote(note);
    }

    @Override
    public void deleteAllNotes() {
        mLocalDataSource.deleteAllNotes();
    }
}
