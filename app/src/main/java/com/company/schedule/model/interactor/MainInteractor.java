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

    public Observable<List<Note>> loadData() {
        return repository.getAllNotes();
    }
    public Observable<List<Note>> loadDailyData() {
        return repository.loadDataDaily();
    }
    public Observable<List<Note>> loadWeeklyData() {
        return repository.loadWeeklyData();
    }
    public Observable<List<Note>> loadMonthlyData() {
        return repository.loadMonthlyData();
    }



    public Completable deleteNoteById(int id) {
        return repository.deleteNoteById(id);
    }

    public Completable refreshDailyData(){
        return repository.refreshDailyData();
    }

    public Completable refreshWeeklyData(){
        return repository.refreshWeeklyData();
    }

    public Completable refreshMonthlyData(){
        return repository.refreshMonthlyData();
    }

}
