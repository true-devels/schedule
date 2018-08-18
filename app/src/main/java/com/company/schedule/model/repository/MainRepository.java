package com.company.schedule.model.repository;

import android.util.Log;

import com.company.schedule.model.data.base.NoteDAO;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.system.SchedulersProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainRepository {

    private NoteDAO noteDAO;

    private Observer<List<Note>> observerNotes;
    private Observer<Note> observerNote;  // observerNotes which get list notes
    private Observer<Object> observerLoadData;

    private SchedulersProvider provider;  // provider for threads

    private static final String TAG = "myLog MainRepository";


    public MainRepository(NoteDAO noteDAO, Observer<List<Note>> observerList, Observer<Note> observerNote , SchedulersProvider provider) {
        this.noteDAO = noteDAO;

        this.observerNotes = observerList;
        this.observerNote = observerNote;
        this.observerLoadData = new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError "+e.getMessage());
            }

            @Override
            public void onComplete() {
                loadData();  // when we make changes we load data to screen
            }

        };

        this.provider = provider;
    }



    //method that inserts new note into DB
    public void insertNoteObservable(final Note note) {
        Observable.create(emitter -> {
            noteDAO.insertNotes(note);
            emitter.onComplete();
        })
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(observerLoadData);
    }


    public void updateNoteObservable(final Note note) {
        Observable.create(emitter -> {
            noteDAO.updateNotes(note);
            emitter.onComplete();
        })
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(observerLoadData);
    }


    //method that deletes note from DB
    public void deleteNoteObservable(final Note noteToDel) {
        Observable.create(emitter -> {
            noteDAO.deleteNote(noteToDel);
            emitter.onComplete();
        })
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(observerLoadData);
    }


    //method that gets all data from DB and update Recycler view
    public void loadData() {

        Observable.create((ObservableOnSubscribe<List<Note>>) emitter -> {
            try {
                List<Note> notes = noteDAO.getAllNotes();  // get notes
                emitter.onNext(notes);  // send notes
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(observerNotes);  // set observer which will listen for changes
    }


    public void getOneNoteByIdObservable(final int id) {

        Observable.create((ObservableOnSubscribe<Note>) emitter -> {
            try {
                Note note = noteDAO.getOneNote(id);
                emitter.onNext(note);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(observerNote);
    }
}
