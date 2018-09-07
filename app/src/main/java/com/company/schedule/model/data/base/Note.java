package com.company.schedule.model.data.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.company.schedule.utils.DateConverter;
import com.company.schedule.utils.Constants;

import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;


//class of notifications
@Entity(tableName = Constants.TABLE_NAME)
public class Note {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    //name of notification
    @ColumnInfo(name = "name")
    private String name;


    @ColumnInfo(name = "content")
    private String content;

    //date when it must appear
    @TypeConverters({DateConverter.class}) // converter, because of using our own custom class
    @ColumnInfo(name = "date")
    private GregorianCalendar date;


    //frequency (if 0 - never(once), 1 - daily, 2 - weekly, 3 - monthly, 4 - yearly)
    @ColumnInfo(name = "frequency")
    private byte frequency;

    //is note already done
    @ColumnInfo(name = "done")
    private boolean done;

    //Constructor
    public Note(int id, String name, String content, GregorianCalendar date, byte frequency, boolean done) {
        this.name = name;
        this.date = date;
        this.frequency = frequency;
        this.content = content;
        this.done = done;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public byte getFrequency() {
        return frequency;
    }

    public void setFrequency(byte frequency) {
        this.frequency = frequency;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // get good date/time format
    public String getDateTimeInFormat() {
        if (date != null)
            return java.text.DateFormat
                    .getDateTimeInstance()  // date and time local format
                    .format(
                            date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                    );
        else return "";
    }

    public String getDateInFormat() {
        if (date != null)
            return java.text.DateFormat
                    .getDateInstance()  // date local format
                    .format(
                            date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                    );
        else return "";
    }

    public String getTimeInFormat() {
        if (date != null)
            return java.text.DateFormat
                    .getTimeInstance()  // time local format
                    .format(
                            date.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                    );
        else return "";
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
