package com.company.schedule.presenter;

import android.util.Log;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;
import com.company.schedule.view.AddNoteView;

import java.util.GregorianCalendar;

import timber.log.Timber;

public class AddNotePresenter {

    private AddNoteView view;

    final private String TAG = "myLog AddNotePresenter";  // tag for log

    public void attachView(AddNoteView view) {
        this.view = view;
    }

    public void pressedToSubmitNote(Note note, boolean isReminded) {
        final String noteName = note.getName();

        if (!noteName.isEmpty()) {
            Timber.v("RESULT_OK, noteName: \"" + noteName + "\";");
            if (isReminded) {  // TODO make frequency do not depends on isReminded
                //if switch button 'remind' is on
                view.setResultOkWithDate(note);
                view.createNotification(note);  // create notification with data from our note
            } else {  // TODO make good default value
                //if switch button 'remind' is off
                // we don't create notification and and don't give time_in_millis to DB
                view.setResultOkWithoutDate(note);
            }
        } else { // if noteName is  empty
            Timber.v("RESULT_CANCELED, noteName: \"" + noteName + "\";");
            view.setResultCancel();
        }
        view.finish();  // finish activity and go to MainActivity
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
        if(isEdited) view.setResultOkDelete(id);
        view.finish();  // finish view in any case
    }

    public void changedRemindMe(boolean isChecked) {
        if (isChecked) view.remindMeIsChecked();  // if swtRemindMe.isChecked: show EditText for Date and for Time
        else view.remindMeIsNotChecked();
    }


    public void detachView() {
        this.view = null;
    }
}
