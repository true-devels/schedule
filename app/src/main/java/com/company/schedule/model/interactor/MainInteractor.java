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

    public Observable<List<Note>> loadData() {
        return repository.loadData();
    }

    public void getOneNoteById(int id) {
        repository.getOneNoteByIdObservable(id);
    }

    //method that inserts new note into DB
    public Completable insertNote(final Note note) {
        return repository.insertNote(note);
    }

    public Completable updateNote(Note noteToUpdate){
        return repository.updateNote(noteToUpdate);
    }

    public Completable deleteNoteById(int id) {
        return repository.deleteNoteById(id);
    }
}
