package com.company.schedule.model.data.base;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.company.schedule.utils.Constants;

@Database(entities = Note.class, version = Constants.DATABASE_VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDAO noteDAO();
    private static AppDatabase database;
    public static AppDatabase getDatabase(Context context){
        if(database ==null){
            database = Room.databaseBuilder(context, AppDatabase.class, Constants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()  //  TODO explain it
                    .build();
        }
        return database;
    }
}
