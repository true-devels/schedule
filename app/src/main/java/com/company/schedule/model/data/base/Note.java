package com.company.schedule.model.data.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.company.schedule.utils.DateConverter;
import com.company.schedule.utils.Constants;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;


//class of notifications
@Entity(tableName = Constants.TABLE_NAME)
public class Note implements Serializable {  // TODO if we make Parcelable instead Serializable it will optimise time
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    //name of notification
    @ColumnInfo(name = "name")
    private String name;


    @ColumnInfo(name = "content")
    private String content;

    //calendarDate when it must appear
    @TypeConverters({DateConverter.class}) // converter, because of using our own custom class
    @ColumnInfo(name = "calendarDate")
    private GregorianCalendar calendarDate;


    //frequency (if 0 - never(once), 1 - daily, 2 - weekly, 3 - monthly, 4 - yearly)
    @ColumnInfo(name = "frequency")
    private byte frequency;

    //is note already done
    @ColumnInfo(name = "done")
    private boolean done;

    //user may postpone note
    @ColumnInfo(name = "later")
    private boolean later;

//     1 is blue, 2 is green, 3 is red, and 4 is yellow
    @ColumnInfo(name = "prioirity")
    private int priority;

    @ColumnInfo(name = "category")
    private String category;

    //Constructor
    public Note(int id, String name, String content, GregorianCalendar calendarDate, byte frequency, boolean done, boolean later, int priority, String category) {
        this.id = id;
        this.name = name;
        this.calendarDate = calendarDate;
        this.frequency = frequency;
        this.content = content;
        this.done = done;
        this.later = later;
        this.priority = priority;
        this.category = category;
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

    public GregorianCalendar getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(GregorianCalendar calendarDate) {
        this.calendarDate = calendarDate;
    }

    // use this instead note.getCalendar == null
    public boolean isDateNull(){
        return calendarDate == null;  // is calendarDate equals null
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

    // get good calendarDate/time format
    public String getDateTimeInFormat() {
        if (calendarDate != null)
            return java.text.DateFormat
                    .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)  // calendarDate and time local format
                    .format(
                            calendarDate.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                    );
        else return "";
    }

    public String getDateInFormat() {
        if (calendarDate != null)
            return java.text.DateFormat
                    .getDateInstance()  // calendarDate local format
                    .format(
                            calendarDate.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                    );
        else return "";
    }

    public String getTimeInFormat() {
        if (calendarDate != null) {
            //DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            return java.text.DateFormat
                    .getTimeInstance(DateFormat.SHORT)  //      * SHORT for "h:mm a" in the US locale. Example 1:00 pm
                    .format(
                            calendarDate.getTime()  // we need write .getTime to convert GregorianCalendar to Date
                    );
        }
        else return "";
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isLater() {
        return later;
    }

    public void setLater(boolean later) {
        this.later = later;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
