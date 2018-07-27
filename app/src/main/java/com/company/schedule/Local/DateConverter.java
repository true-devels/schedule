package com.company.schedule.Local;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.company.schedule.Date;

public class DateConverter {

    final private String TAG = "myLog DateConverter";  // tag for log

    @TypeConverter
    public String fromDates(Date date) {
        return date.getDate()+" "+date.getMinute();
    }

//    @TypeConverter
//    public Date toDates(String data) {
//        int day = Integer.parseInt(data.substring(0, data.indexOf(".")));
//        int month= Integer.parseInt(data.substring(data.indexOf("."), data.indexOf(".", data.indexOf("."))));
//        int year = Integer.parseInt(data.substring(data.indexOf(".", data.indexOf(".")), data.indexOf(" ")));
//        int hour = Integer.parseInt(data.substring(data.indexOf(" ")), data.indexOf(":"));
//        int minute = Integer.parseInt(data.substring(data.indexOf(":")));
//
//        Log.d(TAG+"str", data);
//        Log.d(TAG+"date", "year: " + year + "; month: " + month + "; day: " + day + "; hour: " + hour + "; minute: " + minute );
//        return new Date(year, month, day, hour, minute);
//
//    }
}
