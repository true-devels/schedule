package com.company.schedule.Local;

import com.company.schedule.CustomNotify;
import com.company.schedule.Database.NotifyDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class NotifyDataSourceClass implements NotifyDataSource {
    private NotifyDAO notifyDAO;
    private static NotifyDataSource mInstance;

    public NotifyDataSourceClass(NotifyDAO notifyDAO) {
        this.notifyDAO = notifyDAO;
    }
    public static NotifyDataSource getInstance(NotifyDAO notifyDAO){
        if(mInstance==null){
            mInstance=new NotifyDataSourceClass(notifyDAO);
        }
        return mInstance;
    }

    @Override
    public Flowable<CustomNotify> getOneNotify(int id) {
        return notifyDAO.getOneNotify(id);
    }

    @Override
    public Flowable<List<CustomNotify>> getAllNotifies() {
        return notifyDAO.getAllNotifies();
    }

    @Override
    public void insertNotify(CustomNotify... notifies) {
        notifyDAO.insertNotify(notifies);
    }

    @Override
    public void updateNotify(CustomNotify... notifies) {
        notifyDAO.updateNotify(notifies);
    }

    @Override
    public void deleteNotify(CustomNotify notify) {
        notifyDAO.deleteNotify(notify);
    }

    @Override
    public void deleteAllNotifies() {
        notifyDAO.deleteAllNotifies();
    }

}
