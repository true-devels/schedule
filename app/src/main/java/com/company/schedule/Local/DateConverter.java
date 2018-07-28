package com.company.schedule.Local;

import android.arch.persistence.room.TypeConverter;

import java.util.GregorianCalendar;


public class DateConverter {

    @TypeConverter
    public long fromDates(GregorianCalendar date) {
        return date.getTimeInMillis();
    }

    @TypeConverter
    public GregorianCalendar toDates(long date) {
        GregorianCalendar ret =  new GregorianCalendar();
        ret.setTimeInMillis(date);
        return ret;
    }
}
