package com.company.schedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.company.schedule.utils.Constants.IS_FIRST_TIME_LAUNCH;
import static com.company.schedule.utils.Constants.NIGHT_MODE;
import static com.company.schedule.utils.Constants.PREF_NAME;
import static com.company.schedule.utils.Constants.PRIVATE_MODE;

/**
 * Created by lincollincol
 */
public class SharedPrefs {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public SharedPrefs(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    //method will save the nightMode State : True or False
    public void setNightModeState(Boolean state) {
        editor.putBoolean(NIGHT_MODE,state);
        editor.commit();
    }
    //method will load the Night Mode State
    public Boolean loadNightModeState (){
        Boolean state = pref.getBoolean(NIGHT_MODE,false);
        return  state;
    }
}
