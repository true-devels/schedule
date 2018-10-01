package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;

import io.reactivex.Completable;

public class MyNoteInteractor {
    private MainRepository repository;

    public MyNoteInteractor(MainRepository repository) {
        this.repository = repository;
    }


    public Completable updateNote(Note noteToUpdate){
        return repository.updateNote(noteToUpdate);
    }
    /*
    public Observable<List<Note>> loadData() {
        return repository.loadData();
    }*/
}
