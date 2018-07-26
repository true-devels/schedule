package com.company.schedule.Database;

import com.company.schedule.CustomNotify;

import java.util.List;

import io.reactivex.Flowable;

public interface NotifyDataSource {
    Flowable<CustomNotify> getOneNotify(int id);
    Flowable<List<CustomNotify>> getAllNotifies();
    void insertNotify(CustomNotify... notifies);
    void updateNotify(CustomNotify... notifies);
    void deleteNotify(CustomNotify notify);
    void deleteAllNotifies();
}
