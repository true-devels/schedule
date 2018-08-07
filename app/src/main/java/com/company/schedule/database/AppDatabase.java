package com.company.schedule.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.company.schedule.utils.Constants;

@Database(entities = Note.class, version = Constants.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDAO notifyDAO();
    private static AppDatabase mInstance;
    public static AppDatabase getInstance(Context context){
        if(mInstance==null){
            mInstance = Room.databaseBuilder(context, AppDatabase.class, Constants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()  //  TODO explain it
                    .build();
        }
        return  mInstance;
    }
}
