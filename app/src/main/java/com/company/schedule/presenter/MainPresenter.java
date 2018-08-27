package com.company.schedule.presenter;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.view.MainView;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.company.schedule.utils.Constants.REQUEST_CODE_ADD_NOTE;
import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;

public class MainPresenter {

    private MainView view;
    private MainInteractor interactor;
    // callback for Model, that call setAllNotes in MainView

    public MainPresenter(MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void viewHasCreated() {
        // we load data from DB in Model, and then set all notes in MainView
        loadData();
    }

    private void loadData() {
        interactor.loadData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }

    public void onActivityResult(int requestCode, int resultCode, Note note, boolean isDel) {
        if (resultCode == RESULT_OK || note != null) {  // move part code to different class
            Timber.i("RESULT_OK");

            switch (requestCode) {  // check from which object data come
                case REQUEST_CODE_ADD_NOTE:  // if data come from .AddNoteActivity
                    resultFromAddNote(note);  // result from Add note
                    break;
                case REQUEST_CODE_EDIT_NOTE:
                    if (isDel) resultFromDeleteNote(note.getId());  // result from Delete note
                    else resultFromEditNote(note);  // result from Edit note
                    break;
            }
        } else {
            Timber.v("resultCode != RESULT_OK, requestCode: \"" + requestCode + "\"; resultCode: \"" + resultCode + "\";"); //RESULT_OK: -1; RESULT_CANCELED: 0; RESULT_FIRST_USER(other user result): 1, 2, 3...
        }
    }

    private void resultFromAddNote(Note noteToInsert) {
        //creating and inserting to DB new note
//        final Note local = new Note(name, content, notify_date, freq);
        interactor.insertNote(noteToInsert)
                .subscribe(
                        () -> loadData(),
                        e -> handleThrowable(e)
                );
    }

    private void resultFromEditNote(Note noteToUpdate) {
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> loadData(),
                        e -> handleThrowable(e)
                );
    }
    private void resultFromDeleteNote(int id) {
        interactor.deleteNoteById(id)
                .subscribe(
                        () -> loadData(),
                        e -> handleThrowable(e)
                );
    }

    private void handleThrowable(Throwable throwable) {
        Timber.e(throwable, throwable.toString());
    }

    public void detachView() {
        this.view = null;
    }

    public void onChangedCheckBox(Note noteToUpdate) {
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> loadData(),
                        e -> handleThrowable(e)
                );
    }
}
