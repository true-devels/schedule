package com.company.schedule.Local;

import android.arch.persistence.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


public class DateConverter {

    @TypeConverter
    public long fromDates(GregorianCalendar date) {
        if(date!=null) {
            return date.getTimeInMillis();
        }else{
            return -1;
        }
    }

    @TypeConverter
    public GregorianCalendar toDates(long date) {
        if(date!=-1){
        GregorianCalendar ret =  new GregorianCalendar();
        ret.setTimeInMillis(date);
        return ret;
        }
        return null;
    }

    // TODO make it throught TypeConverter
    public static String toString(GregorianCalendar calendar){
        if(calendar != null) {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm EEEE, MMMM dd, yyyy");
            fmt.setCalendar(calendar);
            String dateFormatted = fmt.format(calendar.getTime());
            return dateFormatted;
        }
        else {
            return null;
        }
    }
}
