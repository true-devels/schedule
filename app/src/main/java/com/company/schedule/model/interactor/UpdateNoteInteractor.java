package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class UpdateNoteInteractor {

    private MainRepository mainRepository;

    public UpdateNoteInteractor(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }


    //method that inserts new note into DB
    public Completable insertNote(final Note note) {
        return mainRepository.insertNote(note);
    }

    public Completable updateNote(Note noteToUpdate){
        return mainRepository.updateNote(noteToUpdate);
    }

    public Observable<Note> getOneNoteByIdObservable(final int id) {
        return mainRepository.getOneNoteByIdObservable(id);
    }

    public Completable deleteNoteById(int id) {
        return mainRepository.deleteNoteById(id);
    }


    public Observable<List<Note>> loadData() {
        return mainRepository.loadData();
    }

}
