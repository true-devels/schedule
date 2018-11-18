package com.company.schedule.presentation.updateNote;

import android.util.Log;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.UpdateNoteInteractor;

import java.util.GregorianCalendar;

import static com.company.schedule.utils.Error.handleThrowable;

public class UpdateNotePresenter {

    private UpdateNoteView view;
    private UpdateNoteInteractor interactor;
    long id_toSent = -1;
    int for_loaddata;
    Note toSent;
    public UpdateNotePresenter(UpdateNoteView view, UpdateNoteInteractor interactor) {
        this.view = view;
        this.interactor = interactor;

    }

    public void pressedToSubmitNote(Note note, boolean isEdited) {


            toSent = note;
            if (isEdited) updateNote(note);
            else insertNewNote(note);
            view.goToMainFragment();  // finish fragment

    }

    public void pressedToEditDate(boolean isEdited, GregorianCalendar gregorianCalendar) {
        if (isEdited) view.showDatePickerFragment(gregorianCalendar);  // if note is editing, then sending existing date to date picker
        else view.showDatePickerFragment();
    }

    public void pressedToEditTime(boolean isEdited, GregorianCalendar gregorianCalendar) {
        if (isEdited) view.showTimePickerFragment(gregorianCalendar);  // if note is editing, then sending existing in note time to time picker
        else view.showTimePickerFragment();
    }

    public void pressedToFabDelete(boolean isEdited, int id) {
        if(isEdited) deleteNote(id);
        view.goToMainFragment();  // finish view in any case
    }

    public void changedRemindMe(boolean isChecked) {
        if (isChecked) view.remindMeIsChecked();  // if swtRemindMe.isChecked: show EditText for Date and for Time
        else view.remindMeIsNotChecked();
    }


    private void loadData(int isReminded) {  // this function do nothing, so we can just delete this
        interactor.loadData()
                .subscribe(
//                        (notes) -> view.setAllNotes(notes),
                        (notes) -> {
                            for (Note note:notes) {
                                Log.d("myLogUNP", "loadData:"+note.getId()+") "+note.getName());
                            }
                        },
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
        if(isReminded==1){
            view.createNotification(toSent, (int)id_toSent);
        }
    }


    private void insertNewNote(Note noteToInsert) {

        //creating and inserting to DB new note
//        final Note local = new Note(name, content, notify_date, freq);
        interactor.insertNote(noteToInsert)
                .subscribe(
                        (id) -> id_toSent = Long.valueOf(id.toString()),
                        e -> handleThrowable((Throwable) e),
                        () -> loadData(1)
                );

    }

    private void updateNote(Note noteToUpdate) {
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> loadData(1),
                        e -> handleThrowable(e)
                );
    }
    private void deleteNote(int id) {
        interactor.deleteNoteById(id)
                .subscribe(
                        () -> loadData(-1),
                        e -> handleThrowable(e)
                );
    }

    public void detachView() {
        this.view = null;
    }

    public void createNote(Note note){
        view.createNotification(note, (int)id_toSent);
    }

}
