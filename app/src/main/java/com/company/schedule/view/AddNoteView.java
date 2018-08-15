package com.company.schedule.view;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Spinner;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;

import java.util.GregorianCalendar;

public interface AddNoteView {
    //
    void remindMeIsChecked();
    void remindMeIsNotChecked();
    // pickers
    void showDatePicker(DatePickerFragment datePickerFragment);
    void showTimePicker(TimePickerFragment timePickerDialog);
    // setters
    void setResultOK(Intent data);
    void setResultCancel();
    // for notify
    void createNotification(Note note);
    // finish activity and going to MainActivity
    void finish();

}
