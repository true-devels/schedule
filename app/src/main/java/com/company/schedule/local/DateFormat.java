package com.company.schedule.local;

import java.util.GregorianCalendar;

// class for output date/time format which depends on the user's local settings
public class DateFormat {

    public final static String getDateTime(GregorianCalendar date) {
        return java.text.DateFormat
                .getDateTimeInstance()  // date and time local format
                .format(
                        date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                );
    }

    public final static String getDate(GregorianCalendar date) {
        return java.text.DateFormat
                .getDateInstance()  // date local format
                .format(
                        date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                );
    }

    public final static String getTime(GregorianCalendar date) {
        return java.text.DateFormat
                .getTimeInstance()  // time local format
                .format(
                        date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                );
    }
}
