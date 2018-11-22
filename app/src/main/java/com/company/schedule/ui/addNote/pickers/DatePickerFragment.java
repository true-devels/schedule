package com.company.schedule.ui.addNote.pickers;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment {

    GregorianCalendar gc;
    DatePickerDialog.OnDateSetListener listener;

    @SuppressLint("ValidFragment")
    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;  // we really need a constructor here
    }

    public DatePickerFragment setGc(GregorianCalendar gc) {
        this.gc = gc;
        return this;  // features
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if method is called from editnote activity,
        //then variable gc will be equal to existing field 'date' of editing CustomNotify object
        if (gc == null) {
            gc = new GregorianCalendar();
        }
        GregorianCalendar max = gc, min = gc;

        //                                                 context,   context for listener
        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener,
                gc.get(Calendar.YEAR),
                gc.get(Calendar.MONTH),
                gc.get(Calendar.DAY_OF_MONTH)
        );
        // set minimum possible date
        min.set(Calendar.DAY_OF_MONTH,1);
        dialog.getDatePicker().setMinDate(min.getTimeInMillis());

        // set maximum possible date
        max.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(max.getTimeInMillis());

        return dialog;

    }
}
