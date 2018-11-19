package com.company.schedule.ui.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.company.schedule.utils.Error;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.company.schedule.utils.Error.ERROR_LISTENER_DO_NOT_INITIALIZED;

public class DatePickerFragment extends DialogFragment {

    GregorianCalendar gc;
    DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment setGc(GregorianCalendar gc) {
        this.gc = gc;
        return this;  // features
    }

    public DatePickerFragment setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
        return this;  // features for return new DPF().setListener(l).show()
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if method is called from editnote activity,
        //then variable gc will be equal to existing field 'date' of editing CustomNotify object
        if (gc == null) {
            gc = new GregorianCalendar();
        }
        //                          context,             context for listener
        if (listener != null) {
            DatePickerDialog dialog = new DatePickerDialog(getContext(), listener,
                    gc.get(Calendar.YEAR),
                    gc.get(Calendar.MONTH),
                    gc.get(Calendar.DAY_OF_MONTH)
            );
            GregorianCalendar max = gc;
            max.set(Calendar.DAY_OF_MONTH,gc.getActualMaximum(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(max.getTimeInMillis());
            GregorianCalendar min = gc;
            max.set(Calendar.DAY_OF_MONTH,1);
            dialog.getDatePicker().setMinDate(min.getTimeInMillis());
            return dialog;

        } else {
            Error.throwNullPointerException(ERROR_LISTENER_DO_NOT_INITIALIZED);  // TODO just make listener init in constructor and delete error handle
            return null;
        }
    }
}
