package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.repository.SharedPrefsRepository;

import io.reactivex.Completable;

public class OneNoteInteractor {

    MainRepository repository;
    SharedPrefsRepository preferences;

    public OneNoteInteractor(MainRepository mainRepository, SharedPrefsRepository sharedPreferences) {
        this.repository = mainRepository;
        this.preferences = sharedPreferences;
    }


    public Completable updateNote(Note noteToUpdate){
        return repository.updateNote(noteToUpdate);
    }

    public Completable deleteNoteById(int id) {
        return repository.deleteNoteById(id);
    }
    //Timer
    public Completable updateNoteDone(Note note) {
        return repository.updateNote(note);
    }

    public void saveFinishTime(int idNote, long finishTime) {
        preferences.saveFinishTime(idNote, finishTime);
    }

    public long getFinishTime(int id) {
        return preferences.getFinishTime(id);
    }
}
