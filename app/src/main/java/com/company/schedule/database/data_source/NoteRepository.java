package com.company.schedule.database.data_source;

import com.company.schedule.database.Note;

import java.util.List;

import io.reactivex.Flowable;

public class NoteRepository implements NoteDataSource {

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
    public Flowable<Note> getOneNotify(int id) {
        return mLocalDataSource.getOneNotify(id);
    }

    @Override
    public Flowable<List<Note>> getAllNotifies() {
        return mLocalDataSource.getAllNotifies();
    }

    @Override
    public void insertNotify(Note... notifies) {
        mLocalDataSource.insertNotify(notifies);
    }

    @Override
    public void updateNotify(Note... notifies) {
        mLocalDataSource.updateNotify(notifies);
    }

    @Override
    public void deleteNotify(Note notify) {
        mLocalDataSource.deleteNotify(notify);
    }

    @Override
    public void deleteAllNotifies() {
        mLocalDataSource.deleteAllNotifies();
    }
}
