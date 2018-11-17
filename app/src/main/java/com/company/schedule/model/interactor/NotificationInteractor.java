package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class NotificationInteractor {
    private MainRepository repository;

    public NotificationInteractor(MainRepository repository) {
        this.repository = repository;
    }


    public Completable updateNote(Note noteToUpdate){
        return repository.updateNote(noteToUpdate);
    }

    public Observable<Note> getOneNoteByIdObservable(final int id) {
        return repository.getOneNoteByIdObservable(id);
    }

    /*
    public Observable<List<Note>> getAllNotes() {
        return mainRepository.getAllNotes();
    }*/
}
