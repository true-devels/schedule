package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.repository.SharedPrefsRepository;

import io.reactivex.Completable;

public class TimerInteractor {

    private MainRepository repository;
    SharedPrefsRepository preferences;

    public TimerInteractor(MainRepository mainRepository, SharedPrefsRepository sharedPreferences) {
        this.repository = mainRepository;
        this.preferences = sharedPreferences;
    }

    public Completable updateNoteDone(Note note) {
        return repository.updateNote(note);
    }


    public void saveFinishTime(int idNote, long finishTime) {
        preferences.saveFinishTime(idNote, finishTime);
    }

    public long getFinishTime(int id) {
        return preferences.getFinishTime(id);
    }

/*
    public void saveTimerState(long finishTime, long pauseTime) {
        preferences.saveTimerState(finishTime, pauseTime);
    }
    public long getPauseTime() {
        return preferences.getFinishTime();
    }
*/
}
