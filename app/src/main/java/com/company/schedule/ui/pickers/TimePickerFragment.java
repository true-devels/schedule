package com.company.schedule.ui.pickers;

import android.annotation.SuppressLint;
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

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment {

    GregorianCalendar gc;
    TimePickerDialog.OnTimeSetListener listener;

    @SuppressLint("ValidFragment")
    public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;  // we really need a constructor here
    }

    public TimePickerFragment setGc(GregorianCalendar gc) {
        this.gc = gc;
        return this;   // features
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if method is called from editnote activity,
        //then variable gc will be equal to existing field 'date' of editing CustomNotify object
        if(gc == null){
            gc = (GregorianCalendar) Calendar.getInstance();
        }
        return new TimePickerDialog(getContext(), listener, // we should set context
                gc.get(Calendar.HOUR_OF_DAY),
                gc.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity())); // switched 12 or 24 hour format, depending on user settings
    }
}
// gc means gregorian calendar