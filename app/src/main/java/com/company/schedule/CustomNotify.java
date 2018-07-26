package com.company.schedule;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.company.schedule.Local.DateConverter;

import java.util.Date;

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

    //date when it must appear
    @TypeConverters({DateConverter.class}) // converter, because of using our own custom class
    @ColumnInfo(name = "date")
    private com.company.schedule.Date date;


    //frequency (if 0 - once, 1 - daily, 2 - weekly, 3 - monthly, 4 - yearly)
    @ColumnInfo(name = "frequency")
    private byte frequency;

    //Constructor
    public CustomNotify(String name, com.company.schedule.Date date, byte frequency) {
        this.name = name;
        this.date = date;
        this.frequency = frequency;
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

    public com.company.schedule.Date getDate() {
        return date;
    }

    public void setDate(com.company.schedule.Date date) {
        this.date = date;
    }

    public byte getFrequency() {
        return frequency;
    }

    public void setFrequency(byte frequency) {
        this.frequency = frequency;
    }


}
