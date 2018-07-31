package com.company.schedule.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.company.schedule.CustomNotify;
import static com.company.schedule.Local.NotifyDatabase.DATABASE_VERSION;

@Database(entities = CustomNotify.class, version = DATABASE_VERSION)
public abstract class NotifyDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "Notifies-DB";

    public abstract NotifyDAO notifyDAO();
    private static NotifyDatabase mInstance;
    public static NotifyDatabase getInstance(Context context){
        if(mInstance==null){
            mInstance = Room.databaseBuilder(context,NotifyDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return  mInstance;
    }
}
