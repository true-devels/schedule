package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.callback.LoadNoteCallback;

public class MainInteractor {

    private MainRepository repository;

    public MainInteractor(MainRepository repository) {
        this.repository = repository;
    }

    //method that inserts new note into DB
    public void insertNote(final Note note, final LoadNoteCallback callback) {
        repository.insertNoteDisposable(note, callback);
    }

    public void updateNote(Note noteToUpdate, LoadNoteCallback callbackLoadDataFinish){
        repository.updateNoteDisposable(noteToUpdate, callbackLoadDataFinish);
    }

    //method that deletes note from DB
    public void deleteNote(Note noteToDel, final LoadNoteCallback callback) {
        repository.deleteNoteDisposable(noteToDel, callback);
    }

    public void loadData(final LoadNoteCallback callback) {
        repository.loadData(callback);
    }
}
