package com.company.schedule.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.company.schedule.CustomNotify;
import static com.company.schedule.local.AppDatabase.DATABASE_VERSION;

@Database(entities = CustomNotify.class, version = DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "Notifies-DB";

    public abstract NotifyDAO notifyDAO();
    private static AppDatabase mInstance;
    public static AppDatabase getInstance(Context context){
        if(mInstance==null){
            mInstance = Room.databaseBuilder(context,AppDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()  //  TODO explain it
                    .build();
        }
        return  mInstance;
    }
}
