package com.company.schedule.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.company.schedule.utils.Constants.FIRST_TIME_LAUNCH_KEY;
import static com.company.schedule.utils.Constants.LAST_TIME_DAILY;
import static com.company.schedule.utils.Constants.LAST_TIME_MONTHLY;
import static com.company.schedule.utils.Constants.LAST_TIME_WEEKLY;
import static com.company.schedule.utils.Constants.NIGHT_MODE;
import static com.company.schedule.utils.Constants.PREF_NAME;
import static com.company.schedule.utils.Constants.PRIVATE_MODE;

public class SharedPrefs {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefs(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunchFalse() {
        editor.putBoolean(FIRST_TIME_LAUNCH_KEY, false);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(FIRST_TIME_LAUNCH_KEY, true);
    }


    //method will save the nightMode State : True or False
    public void setNightModeState(Boolean state) {
        editor.putBoolean(NIGHT_MODE,state);
        editor.apply();
    }
    //method will load the Night Mode State
    public boolean isNightMode(){
        return pref.getBoolean(NIGHT_MODE,false);
    }

    public void setTimeLastUpdateDaily(long time){
        editor.putLong(LAST_TIME_DAILY,time);
        editor.apply();
    }

    public long getTimeLastUpdateDaily(){
        return pref.getLong(LAST_TIME_DAILY,0);
    }

    public void setTimeLastUpdateWeekly(long time){
        editor.putLong(LAST_TIME_WEEKLY,time);
        editor.apply();
    }

    public long getTimeLastUpdateWeekly(){
        return pref.getLong(LAST_TIME_WEEKLY,0);
    }

    public void setTimeLastUpdateMonthly(long time){
        editor.putLong(LAST_TIME_MONTHLY,time);
        editor.apply();
    }

    public long getTimeLastUpdateMonthly(){
        return pref.getLong(LAST_TIME_MONTHLY,0);
    }

}
