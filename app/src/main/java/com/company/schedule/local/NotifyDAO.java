package com.company.schedule.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.company.schedule.CustomNotify;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface NotifyDAO {
    @Query("SELECT * FROM notifies WHERE id=:id")
    Flowable<CustomNotify> getOneNotify(int id);

    @Query("SELECT * FROM notifies")
    Flowable<List<CustomNotify>> getAllNotifies();

    @Insert
    void insertNotify(CustomNotify... notifies);

    @Update
    void updateNotify(CustomNotify... notifies);

    @Delete
    void deleteNotify(CustomNotify notify);

    @Query("DELETE FROM notifies")
    void deleteAllNotifies();

    // for output notifies sorted by date
    @Query("SELECT * FROM notifies ORDER BY date")
    Flowable<List<CustomNotify>> getNotifiesSortedByDate();
}
