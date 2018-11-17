package com.company.schedule.model.data.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.company.schedule.utils.DateConverter;
import com.company.schedule.utils.Constants;

import java.io.Serializable;
import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;


//class of notifications
@Entity(tableName = Constants.TABLE_NAME)
public class Note implements Serializable {  // TODO make Parcelable instead Serializable. it is optimise time
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
    private int done;

    //user may postpone note
    @ColumnInfo(name = "later")
    private int later;

    @ColumnInfo(name = "prioirity")
    private int priority;

    @ColumnInfo(name = "category")
    private String category;

    //Constructor
    public Note(int id, String name, String content, GregorianCalendar date, byte frequency, int done, int later, int priority, String category) {
        this.id = id;
        this.name = name;
        this.date = date;
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

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    // use this instead note.getDate == null
    public boolean isDateNull(){
        return date == null;  // is date equals null
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

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getLater() {
        return later;
    }

    public void setLater(int later) {
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
