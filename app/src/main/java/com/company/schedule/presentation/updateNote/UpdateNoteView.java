package com.company.schedule.presentation.updateNote;

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
    void createNotification(Note note, int id);

    void toast(String toast_message);
    void toastLong(String toast_message);
    void goToMainFragment();
}
