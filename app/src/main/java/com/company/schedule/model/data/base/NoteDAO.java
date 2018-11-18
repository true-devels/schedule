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


    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE frequency=2 AND later = 0 AND done = 0 ")
    List<Note> getAllWeeklyNotes();

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE frequency=3 AND later=0 AND done = 0")
    List<Note> getAllMonthlyNotes();

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE frequency=0 AND later = 0 AND done = 0")
    List<Note> getAllOnceNotes();

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE frequency=1 AND later = 0 AND done = 0")
    List<Note> getAllDailyNotes();

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE later = 1")
    List<Note> getAllLaterNotes();

    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE done = 1")
    List<Note> getAllDoneNotes();

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

    @Query("UPDATE " + Constants.TABLE_NAME + " SET done = 0, later=0 WHERE frequency=1")
    void refreshDailyNotes();

    @Query("UPDATE " + Constants.TABLE_NAME + " SET done = 0, later=0 WHERE frequency=2")
    void refreshWeeklyNotes();

    @Query("UPDATE " + Constants.TABLE_NAME + " SET done = 0, later=0 WHERE frequency=3")
    void refreshMonthlyNotes();
}
