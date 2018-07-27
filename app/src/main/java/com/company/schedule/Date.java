package com.company.schedule;

import java.util.Calendar;

public class Date {
    int year, month, day, hour, minute;

    public Date() {
        update();  //make update date/time when create new object
    }
    public Date(int year, int month, int day, int hour, int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
    public Date(int year, int month, int day, int hour, int minute, int monthCoefficient){
        this.year = year;
        this.month = month + monthCoefficient;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
    public String getDate() {
        return day + "." + month + "." + year;
    }

    public String getTime() {
        return hour + ":" + minute;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public void update() {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH)+1;  // because computer start count month from 0
        this.day = c.get(Calendar.DAY_OF_MONTH);
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
    }

    public int[] getIntArray() {
        return new int[]{year, month, day, hour, minute};
    }

    public String getString() {
        return getDate() + " " + getTime();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
