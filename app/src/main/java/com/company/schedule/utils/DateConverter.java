package com.company.schedule.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.GregorianCalendar;

//converter class, SQLite DB doesn't allow fields of GregorianCalendar class
public class DateConverter {

    @TypeConverter
    public long fromDates(GregorianCalendar date) {
        // if user switched button 'remind me' off, than field 'date' = null
        if (date != null) {
            return date.getTimeInMillis();
        } else {
            return -1;
        }
    }

    @TypeConverter
    public GregorianCalendar toDates(long date) {
        if (date!=-1) {
            GregorianCalendar ret =  new GregorianCalendar();
            ret.setTimeInMillis(date);
            return ret;
        } else {
            return null;
        }
    }
}
