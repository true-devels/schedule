package com.company.schedule.model.data.base;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.company.schedule.utils.Constants;

import java.util.List;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE id=:id")
    Note getOneNote(int id);

    @Query("SELECT * FROM " + Constants.TABLE_NAME + "")  // TODO make good sort by date
    List<Note> getAllNotes();//            +
//            "UNION ALL " +
//            "SELECT * FROM " + Constants.TABLE_NAME + " WHERE date = NULL "

    @Insert
    long insertNote(Note note);

    @Update
    void updateNotes(Note... notes);

    @Query("DELETE FROM " + Constants.TABLE_NAME + " WHERE id=:id")
    void deleteNoteById(int id);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM " + Constants.TABLE_NAME)
    void deleteAllNotes();
    // for output notifies sorted by date

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY date;")
    List<Note> getNotesSortedByDate();
}
