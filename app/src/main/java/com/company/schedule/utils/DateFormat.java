package com.company.schedule.utils;

import java.util.GregorianCalendar;

// class for output date/time format which depends on the user's local settings
public class DateFormat {

    public static String getDateTime(GregorianCalendar date) {
        return java.text.DateFormat
                .getDateTimeInstance()  // date and time local format
                .format(
                        date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                );
    }

    public static String getDate(GregorianCalendar date) {
        return java.text.DateFormat
                .getDateInstance()  // date local format
                .format(
                        date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                );
    }

    public static String getTime(GregorianCalendar date) {
        return java.text.DateFormat
                .getTimeInstance()  // time local format
                .format(
                        date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                );
    }
}
