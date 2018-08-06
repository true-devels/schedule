package com.company.schedule.fragments.pickers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    GregorianCalendar gc;

    public GregorianCalendar getGc() {
        return gc;
    }

    public void setGc(GregorianCalendar gc) {
        this.gc = gc;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if method is called from editnote activity,
        //then variable gc will be equal to existing field 'date' of editing CustomNotify object
        if(gc==null){
            gc = (GregorianCalendar) Calendar.getInstance();
        }

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), // we should set context
                gc.get(Calendar.HOUR_OF_DAY),
                gc.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity())); // switched 12 or 24 hour format, depending on user settings
    }
}
