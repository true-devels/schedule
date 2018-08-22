package com.company.schedule.presenter;

import android.util.Log;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.view.MainView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainPresenter {

    private MainView view;
    private MainInteractor interactor;
    private CompositeDisposable compositeDisposable;
    // callback for Model, that call setAllNotes in MainView

    private final String TAG = "myLog MainMainPresenter";

    public MainPresenter(MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
        compositeDisposable = new CompositeDisposable();
    }

    public void viewHasCreated() {
        // we load data from DB in Model, and then set all notes in MainView
        Disposable disposable = interactor.loadData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> Log.e(TAG, "onError " + e.getMessage())
                );  // load data from DB
        compositeDisposable.add(disposable);
    }


    public void resultFromAddNote(Note noteToInsert) {
        //creating and inserting to DB new note
//        final Note local = new Note(name, content, notify_date, freq);
        interactor.insertNote(noteToInsert)
        .subscribe(() -> loadData());

    }
    public void resultFromEditNote(Note noteToUpdate) {
        interactor.updateNote(noteToUpdate)
                .subscribe(() -> loadData());
    }

    public void resultFromDeleteNote(int id) {
        interactor.deleteNoteById(id)
                .subscribe(() -> loadData());
    }
    private void loadData() {
        interactor.loadData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> Log.e(TAG, "onError " + e.getMessage())
                );  // load data from DB
    }

    public void detachView() {
        this.view = null;
    }
}
