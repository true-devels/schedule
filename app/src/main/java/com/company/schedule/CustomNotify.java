package com.company.schedule;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
    @ColumnInfo(name = "date")
    private long date;


    //frequency (if 0 - once, 1 - daily, 2 - weekly, 3 - monthly, 4 - yearly)
    @ColumnInfo(name = "frequency")
    private byte frequency;

    //Constructor
    public CustomNotify(String name, long date, byte frequency) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public byte getFrequency() {
        return frequency;
    }

    public void setFrequency(byte frequency) {
        this.frequency = frequency;
    }


}
