package com.company.schedule.fragments.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {


    public GregorianCalendar getGc() {
        return gc;
    }

    public void setGc(GregorianCalendar gc) {
        this.gc = gc;
    }

    GregorianCalendar gc;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if method is called from editnote activity,
        //then variable gc will be equal to existing field 'date' of editing CustomNotify object
        if(gc==null){
        gc =new GregorianCalendar();
        }

        //                          context,             context for listener
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),
                gc.get(Calendar.YEAR),
                gc.get(Calendar.MONTH),
                gc.get(Calendar.DAY_OF_MONTH)
                );

    }


}
