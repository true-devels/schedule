package com.company.schedule.view;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Spinner;

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
    // getters
    int getId();
    String getTextFromNameNote();
    String getTextFromContentNote();
    boolean getIsReminded();
    boolean getIsEdited();
    GregorianCalendar getDateNotification();
    GregorianCalendar getGcEditDate();
    Spinner getSpinnerFreq();
    Context getContext();
    AlarmManager getAlarmManager();

    void finish();

}
