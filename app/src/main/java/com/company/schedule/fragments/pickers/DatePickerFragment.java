package com.company.schedule.fragments.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();

        //                          context,             context for listener
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH)+1,  // because computer start count month from 0. It's not good for user.
                c.get(Calendar.DAY_OF_MONTH)
                );
    }
}
