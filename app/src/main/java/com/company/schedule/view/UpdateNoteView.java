package com.company.schedule.view;

import com.company.schedule.model.data.base.Note;

import java.util.GregorianCalendar;

public interface UpdateNoteView {
    //
    void remindMeIsChecked();
    void remindMeIsNotChecked();
    // pickers
    void openDatePickerFragment(GregorianCalendar calendar);  // Date picker fragment
    void showEmptyDatePicker();
    void openTimePickerFragment(GregorianCalendar calendar);  // Time picker fragment
    void showEmptyTimePicker();
    // for notify
    void createNotification(Note note);
    // finish activity and going to MainActivity
    void finish();

}
