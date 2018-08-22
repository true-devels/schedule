package com.company.schedule.view;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;

public interface AddNoteView {
    //
    void remindMeIsChecked();
    void remindMeIsNotChecked();
    // pickers
    void showDatePicker(DatePickerFragment datePickerFragment);
    void showTimePicker(TimePickerFragment timePickerDialog);
    // setters result
    void setResultOkWithDate(Note noteWithDate);
    void setResultOkWithoutDate(Note noteWithoutDate);
    void setResultOkDelete(int id);
    void setResultCancel();
    // for notify
    void createNotification(Note note);
    // finish activity and going to MainActivity
    void finish();

}
