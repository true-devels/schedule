package com.company.schedule.model.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.utils.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.company.schedule.utils.Constants.FINISH_TIME;
import static com.company.schedule.utils.Constants.FIRST_TIME_LAUNCH_KEY;
import static com.company.schedule.utils.Constants.LAST_TIME_DAILY;
import static com.company.schedule.utils.Constants.LAST_TIME_MONTHLY;
import static com.company.schedule.utils.Constants.LAST_TIME_STAT;
import static com.company.schedule.utils.Constants.LAST_TIME_WEEKLY;
import static com.company.schedule.utils.Constants.LOCALIZATION;
import static com.company.schedule.utils.Constants.NIGHT_MODE;
import static com.company.schedule.utils.Constants.PAUSE_TIME;
import static com.company.schedule.utils.Constants.PREF_NAME;
import static com.company.schedule.utils.Constants.PRIVATE_MODE;
import static com.company.schedule.utils.Constants.STATISTIC_SET;

public class SharedPrefsRepository {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefsRepository(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunchFalse() {
        if(isFirstTimeLaunch()){
        editor.putBoolean(FIRST_TIME_LAUNCH_KEY, false);
        editor.apply();
        ArrayList<String> list= new ArrayList<>();
        for(int i=0;i<7;i++){
            list.add("0");
        }
        try {
            editor.putString(STATISTIC_SET,ObjectSerializer.serialize(list));
        }catch (IOException ie){
            ie.printStackTrace();
        }
        setTimeLastUpdateStat(new Date().getTime());
        Log.d("tag",list.toString());
        editor.apply();
        }
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

//  Timer
    public void saveFinishTime(int id, long finishTime) {
        editor.putLong(FINISH_TIME+id, finishTime);  // finish time depend on note, save with key FINISH_TIME+ id_note
        editor.apply();
    }

    public long getFinishTime(int id) {
        return pref.getLong(FINISH_TIME+id, 0L);
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


    public void setLocalization(String localization){
        editor.putString(LOCALIZATION,localization);
        editor.apply();
    }

    public String getLocalization(){
        return pref.getString(LOCALIZATION,"en");
    }

    public void resetStatistics(int numberDayWeek){
        ArrayList<String> numbers = getStatistics();
        numbers.set(numberDayWeek-2,"0");
        try{
            editor.putString(STATISTIC_SET,ObjectSerializer.serialize(numbers));
        }catch (IOException ie){
            ie.printStackTrace();
        }
        editor.apply();
    }

    public void updateStatistics(int numberDayWeek, int done){
        ArrayList<String> numbers = getStatistics();
        numbers.set(numberDayWeek-2,  Integer.toString(done));
        try {
            editor.putString(STATISTIC_SET,ObjectSerializer.serialize(numbers));
        }catch (IOException io){
            io.printStackTrace();
        }
        editor.apply();
        //editor.put
    }

    public ArrayList<String> getStatistics(){
        try {
            return (ArrayList<String>) ObjectSerializer.deserialize(pref.getString(STATISTIC_SET,""));
        }catch (IOException | ClassNotFoundException io){
            io.printStackTrace();
            return null;
        }
    }

    public void setTimeLastUpdateStat(long time){
        editor.putLong(LAST_TIME_STAT,time);
        editor.apply();
    }

    public long getTimeLastUpdateStat(){
        return pref.getLong(LAST_TIME_STAT,0);
    }
}
