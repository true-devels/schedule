package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;

import io.reactivex.Completable;

public class OneNoteInteractor {
    MainRepository repository;

    public OneNoteInteractor(MainRepository repository) {
        this.repository = repository;
    }


    public Completable updateNote(Note noteToUpdate){
        return repository.updateNote(noteToUpdate);
    }

    public Completable deleteNoteById(int id) {
        return repository.deleteNoteById(id);
    }
}
