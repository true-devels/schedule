package com.company.schedule.model.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.company.schedule.model.data.base.Note;

import static com.company.schedule.utils.Constants.FINISH_TIME;
import static com.company.schedule.utils.Constants.FIRST_TIME_LAUNCH_KEY;
import static com.company.schedule.utils.Constants.NIGHT_MODE;
import static com.company.schedule.utils.Constants.PAUSE_TIME;
import static com.company.schedule.utils.Constants.PREF_NAME;
import static com.company.schedule.utils.Constants.PRIVATE_MODE;

public class SharedPrefsRepository {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefsRepository(Context context) {
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



//  Timer
    public void saveFinishTime(int id, long finishTime) {
        editor.putLong(FINISH_TIME+id, finishTime);  // finish time depend on note, save with key FINISH_TIME+ id_note
        editor.apply();
    }

    public long getFinishTime(int id) {
        return pref.getLong(FINISH_TIME+id, 0L);
    }
/*
TODO deprecated
    public void saveTimerState(long finishTime, long pauseTime) {
        editor.putLong(FINISH_TIME, finishTime);
        editor.putLong(PAUSE_TIME, pauseTime);
        editor.apply();
    }

    public long getFinishTime() {
        return pref.getLong(FINISH_TIME, 0L);
    }

    public long getPauseTime() {
        return pref.getLong(PAUSE_TIME, 0L);
    }
*/
}
