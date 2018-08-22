package com.company.schedule.presenter;

import android.util.Log;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;
import com.company.schedule.view.AddNoteView;

import java.util.GregorianCalendar;

public class AddNotePresenter {

    private AddNoteView view;

    final private String TAG = "myLog AddNotePresenter";  // tag for log

    public void attachView(AddNoteView view) {
        this.view = view;
    }

    public void pressedToSubmitNote(Note note, boolean isReminded) {
        final String noteName = note.getName();

        if (!noteName.isEmpty()) {
            Log.v(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");
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
            Log.v(TAG, "RESULT_CANCELED, noteName: \"" + noteName + "\";");
            view.setResultCancel();
        }
        view.finish();  // finish activity and go to MainActivity
    }

    public void pressedToEditDate(boolean isEdited, GregorianCalendar gregorianCalendar) {
        DatePickerFragment datePicker = new DatePickerFragment(); // calls fragment with date picker dialog
        if (isEdited) {
            //if note is editing, then sending existing date to date picker
            datePicker.setGc(gregorianCalendar);
        }
        view.showDatePicker(datePicker);
    }

    public void pressedToEditTime(boolean isEdited, GregorianCalendar gregorianCalendar) {
        TimePickerFragment timePicker = new TimePickerFragment();// calls fragment with time picker dialog
        if (isEdited) {
            //if note is editing, then sending existing in note time to time picker
            timePicker.setGc(gregorianCalendar);
        }
        view.showTimePicker(timePicker);
    }

    public void pressedToFabDelete(boolean isEdited, int id) {
        //
        if(isEdited){
            view.setResultOkDelete(id);
            view.finish();
        } else {  // if we in add note we just finish view when click on delete
            view.finish();
        }
    }

    public void changedRemindMe(boolean isChecked) {
        if (isChecked) { // if swtRemindMe.isChecked: show EditText for Date and for Time
            view.remindMeIsChecked();
        } else {
            view.remindMeIsNotChecked();
        }
    }


    public void detachView() {
        this.view = null;
    }
}
