package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;

public class MainInteractor {

    private MainRepository repository;

    public MainInteractor(MainRepository repository) {
        this.repository = repository;
    }

    //method that inserts new note into DB
    public void insertNote(final Note note) {
        repository.insertNoteObservable(note);
    }

    public void updateNote(Note noteToUpdate){
        repository.updateNoteObservable(noteToUpdate);
    }

    //method that deletes note from DB
    public void deleteNote(Note noteToDel) {
        repository.deleteNoteObservable(noteToDel);
    }

    public void loadData() {
        repository.loadData();
    }

    public void getOneNoteById(int id) {
        repository.getOneNoteByIdObservable(id);
    }
}
