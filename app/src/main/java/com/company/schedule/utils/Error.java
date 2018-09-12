package com.company.schedule.utils;

import android.util.Log;

import timber.log.Timber;

public class Error {

    public static String ERROR_LISTENER_DO_NOT_INITIALIZED = "Error: listener must be initialized.\nUse \"new PF().setListener(class which implements PD.OnTimeSetListener)\"";

    public static void handleThrowable(Throwable throwable) {
        Timber.e(throwable, throwable.toString());
        Log.e("myLog", throwable.toString());
        try {
            throw throwable;
        } catch (Throwable throwable1) {
            throwable1.printStackTrace();
        }
    }

    public static void throwException(String errorMessage) throws Exception {
        Exception exception = new Exception(errorMessage);
        handleThrowable(exception);
        throw exception;
    }

    public static void throwNullPointerException(String errorMessage) {
        NullPointerException exception = new NullPointerException(errorMessage);
        handleThrowable(exception);
        throw exception;
    }
}


// PF means Date or Time PickerFragment
// PD means Date or Time PickerDialog