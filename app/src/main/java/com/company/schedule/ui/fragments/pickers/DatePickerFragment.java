package com.company.schedule.ui.fragments.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import timber.log.Timber;

public class DatePickerFragment extends DialogFragment {

    GregorianCalendar gc;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String TAG = "DatePickerFragment";
        //if method is called from editnote activity,
        //then variable gc will be equal to existing field 'date' of editing CustomNotify object
        if(gc==null){
            gc = new GregorianCalendar();
        }

        Timber.v("onCreateDialog: " + gc.get(Calendar.DAY_OF_MONTH) + "." + gc.get(Calendar.MONTH) + "." + gc.get(Calendar.YEAR));
        //                          context,             context for listener
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),
                gc.get(Calendar.YEAR),
                gc.get(Calendar.MONTH),
                gc.get(Calendar.DAY_OF_MONTH)
                );

    }

    public void setGc(GregorianCalendar gc) {
        this.gc = gc;
    }
}
