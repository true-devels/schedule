package com.company.schedule.Local;

import android.arch.persistence.room.TypeConverter;

import com.company.schedule.Date;

public class DateConverter {
    @TypeConverter
    public String fromDates(Date date) {
        return date.getDate()+" "+date.getMinute();
    }

    @TypeConverter
    public Date toDates(String data) {
        int day = Integer.parseInt(data.substring(0,data.indexOf(".")));
        int month= Integer.parseInt(data.substring(data.indexOf("."),data.indexOf(".",data.indexOf("."))));
        int year = Integer.parseInt(data.substring(data.indexOf(".",data.indexOf(".")),data.indexOf(" ")));
        int hour = Integer.parseInt(data.substring(data.indexOf(" ")),data.indexOf(":"));
        int minute = Integer.parseInt(data.substring(data.indexOf(":")));
        return new Date(year,month,day,hour,minute);
    }
}
