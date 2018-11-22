package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class MainInteractor {

    private MainRepository repository;

    public MainInteractor(MainRepository repository) {
        this.repository = repository;
    }


    public Completable updateNote(Note noteToUpdate){
        return repository.updateNote(noteToUpdate);
    }

    public Observable<List<Note>> getAllNotes() {
        return repository.getAllNotes();
    }
    public Observable<List<Note>> getAllDailyNotes() {
        return repository.getAllDailyNotes();
    }
    public Observable<List<Note>> getAllWeeklyNotes() {
        return repository.getAllWeeklyNotes();
    }
    public Observable<List<Note>> getAllMonthlyNotes() {
        return repository.getAllMonthlyNotes();
    }



    public Completable deleteNoteById(int id) {
        return repository.deleteNoteById(id);
    }

    public Completable refreshDailyNotes(){
        return repository.refreshDailyNotes();
    }

    public Completable refreshWeeklyNotes(){
        return repository.refreshWeeklyNotes();
    }

    public Completable refreshMonthlyNotes(){
        return repository.refreshMonthlyNotes();
    }

}
