package com.company.schedule.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
This class can cast Calendar, GregorianCalendar (or even java.util.Date if you need it)
to String. This String depends on local user preferences, it means that for different user String
will looks different. Like american men will see 1:00 pm but ukrainian men will see 13:00

So use this class to make different manipulations with date/time
*/

public class LocalFormat {

    public static String getTime(Calendar time){
        return java.text.DateFormat
                .getTimeInstance()  // get local preferences
                .format(
                        time.getTime()  // cast calendar to java.util.Date;

                );
    }

    public static String getDate(Calendar date){
        return java.text.DateFormat
                .getDateInstance()// get local preferences
                .format(
                        date.getTime()  // cast calendar to java.util.Date;
                );
    }

    public static String getDateTime(Calendar dateTime){
        return java.text.DateFormat
                .getDateInstance()// get local preferences
                .format(
                        dateTime.getTime()  // cast java.util.Calendar to Date;
                );
    }

    public static String getMonthYear(Date date) {
        return new SimpleDateFormat("MMMM, yyyy")  // "November, 2018"
                .format(date);
    }

    public static String getDayMonth(Calendar calendar) {
        return new SimpleDateFormat("dd, MMM")  // "26, Nov"(day 2 numbers, month 3 symbols)
                .format(
                        calendar.getTime()  // cast Calendar to java.util.Date;
                );
    }
}


/*
Usefull links
https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html - to know what letters in SimpleDateFormat means
https://www.javatpoint.com/java-simpledateformat - how to use SimpleFormat
https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/ - current time
 */