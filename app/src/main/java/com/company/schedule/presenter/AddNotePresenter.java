package com.company.schedule.presenter;

import android.content.Intent;
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
        final String noteContent = note.getContent();


        if (!noteName.isEmpty()) {
            Intent intentReturnNoteData = new Intent();  // return ready note to MainActivity to DB
            intentReturnNoteData.putExtra("id", note.getId());  // Output error toast when id == -1 or id == 0
            intentReturnNoteData.putExtra("note_name", noteName);
            intentReturnNoteData.putExtra("note_content", noteContent);
            intentReturnNoteData.putExtra("freq", note.getFrequency());

            if (isReminded) {  // TODO make frequency do not depends on isReminded
                //if switch button 'remind' is on
                view.createNotification(note);  // create notification with data from our note
                intentReturnNoteData.putExtra("time_in_millis", note.getDate().getTimeInMillis());
            } else {  // TODO make good default value
                //if switch button 'remind' is off
                // we don't create notification and and don't give time_in_millis to DB
                intentReturnNoteData.putExtra("time_in_millis", (long)-1);
            }

            Log.v(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");
            view.setResultOK(intentReturnNoteData);
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
        if(isEdited){
            Intent intent = new Intent();
            intent.putExtra("isDel",true);
            intent.putExtra("id", id);
            view.setResultOK(intent);
            view.finish();
        }else{
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
