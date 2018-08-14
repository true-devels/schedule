package com.company.schedule.model.data.base;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.utils.Constants;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM " + Constants.TABLE_NAME + " WHERE id=:id")
    Flowable<Note> getOneNote(int id);

    @Query("SELECT * FROM " + Constants.TABLE_NAME)
    Flowable<List<Note>> getAllNotes();

    @Insert
    void insertNotes(Note... notes);

    @Update
    void updateNotes(Note... notes);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM " + Constants.TABLE_NAME)
    void deleteAllNotes();

    // for output notifies sorted by date
    @Query("SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY date")
    Flowable<List<Note>> getNotesSortedByDate();

    @Query("DELETE FROM " + Constants.TABLE_NAME + " WHERE id=:id")
    void deleteNoteById(int id);
}
