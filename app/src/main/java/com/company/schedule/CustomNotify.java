package com.company.schedule;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.company.schedule.Local.DateConverter;


import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;


//class of notifications
@Entity(tableName = "notifies")
public class CustomNotify {

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

    //Constructor
    public CustomNotify(String name, String content, GregorianCalendar date, byte frequency) {
        this.name = name;
        this.date = date;
        this.frequency = frequency;
        this.content = content;
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



}
