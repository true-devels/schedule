package com.company.schedule.Database;

import com.company.schedule.CustomNotify;

import java.util.List;

import io.reactivex.Flowable;

public class NotifyRepository implements NotifyDataSource{

    private NotifyDataSource mLocalDataSource;
    private static NotifyRepository mInstance;

    public NotifyRepository(NotifyDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }
    public static NotifyRepository getmInstance(NotifyDataSource mLocalDataSource){
        if(mInstance==null){
            mInstance=new NotifyRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<CustomNotify> getOneNotify(int id) {
        return mLocalDataSource.getOneNotify(id);
    }

    @Override
    public Flowable<List<CustomNotify>> getAllNotifies() {
        return mLocalDataSource.getAllNotifies();
    }

    @Override
    public void insertNotify(CustomNotify... notifies) {
        mLocalDataSource.insertNotify(notifies);
    }

    @Override
    public void updateNotify(CustomNotify... notifies) {
        mLocalDataSource.updateNotify(notifies);
    }

    @Override
    public void deleteNotify(CustomNotify notify) {
        mLocalDataSource.deleteNotify(notify);
    }

    @Override
    public void deleteAllNotifies() {
        mLocalDataSource.deleteAllNotifies();
    }
}
