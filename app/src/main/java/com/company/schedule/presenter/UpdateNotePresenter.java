package com.company.schedule.presenter;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.view.UpdateNoteView;

import java.util.GregorianCalendar;

import timber.log.Timber;

public class UpdateNotePresenter {

    private UpdateNoteView view;
    private MainInteractor interactor;

    public UpdateNotePresenter(UpdateNoteView view, MainInteractor mainInteractor) {
        this.view = view;
        this.interactor = mainInteractor;
    }

    public void pressedToSubmitNote(Note note, boolean isReminded) {
        final String noteName = note.getName();

        // if noteName is  empty
        if (!noteName.isEmpty()) {
            Timber.v("RESULT_OK, noteName: \"" + noteName + "\";");
            resultFromAddNote(note);

            if (isReminded) view.createNotification(note);  // create notification
        }
        else Timber.v("RESULT_CANCELED, noteName: \"" + noteName + "\";");

        view.finish();  // finish fragment
    }

    public void pressedToEditDate(boolean isEdited, GregorianCalendar gregorianCalendar) {
        if (isEdited) view.openDatePickerFragment(gregorianCalendar);  // if note is editing, then sending existing date to date picker
        else view.showEmptyDatePicker();
    }

    public void pressedToEditTime(boolean isEdited, GregorianCalendar gregorianCalendar) {
        if (isEdited) view.openTimePickerFragment(gregorianCalendar);  // if note is editing, then sending existing in note time to time picker
        else view.showEmptyTimePicker();
    }

    public void pressedToFabDelete(boolean isEdited, int id) {
        if(isEdited) resultFromDeleteNote(id);
        view.finish();  // finish view in any case
    }

    public void changedRemindMe(boolean isChecked) {
        if (isChecked) view.remindMeIsChecked();  // if swtRemindMe.isChecked: show EditText for Date and for Time
        else view.remindMeIsNotChecked();
    }


    private void loadData() {
        interactor.loadData()
                .subscribe(
//                        (notes) -> view.setAllNotes(notes),
                        (notes) -> Timber.d("setAllNotes"),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
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
}
