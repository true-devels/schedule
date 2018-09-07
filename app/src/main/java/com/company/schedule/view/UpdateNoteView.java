package com.company.schedule.view;

import android.support.v4.app.Fragment;

import com.company.schedule.model.data.base.Note;

import java.util.GregorianCalendar;

public interface UpdateNoteView {
    //
    void remindMeIsChecked();
    void remindMeIsNotChecked();
    // pickers
    void showDatePickerFragment(GregorianCalendar calendar);  // Date picker fragment
    void showDatePickerFragment();
    void showTimePickerFragment(GregorianCalendar calendar);  // Time picker fragment
    void showTimePickerFragment();
    // for notify
    void createNotification(Note note);

    void toast(String toast_message);
    void toastLong(String toast_message);
    void goToMainFragment();
}
