package com.company.schedule.presentation.ui.fragments.pickers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import com.company.schedule.utils.Error;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.company.schedule.utils.Error.ERROR_LISTENER_DO_NOT_INITIALIZED;

public class TimePickerFragment extends DialogFragment {

    GregorianCalendar gc;
    TimePickerDialog.OnTimeSetListener listener;

    public TimePickerFragment setGc(GregorianCalendar gc) {
        this.gc = gc;
        return this;   // features
    }

    public TimePickerFragment setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
        return this;  // features for new TPF().setGc(gc).setListener(l).show() instead create new instance
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if method is called from editnote activity,
        //then variable gc will be equal to existing field 'date' of editing CustomNotify object
        if(gc == null){
            gc = (GregorianCalendar) Calendar.getInstance();
        }
        if (listener != null) {
            return new TimePickerDialog(getContext(), listener, // we should set context
                    gc.get(Calendar.HOUR_OF_DAY),
                    gc.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(getActivity())); // switched 12 or 24 hour format, depending on user settings
        } else {
            Error.throwNullPointerException(ERROR_LISTENER_DO_NOT_INITIALIZED);
            return null;
        }
    }
}
// gc means gregorian calendar