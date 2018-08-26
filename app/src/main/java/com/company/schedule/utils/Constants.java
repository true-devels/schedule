package com.company.schedule.utils;

import android.graphics.Color;

public final class Constants {
    // for DB
    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "Notifies-DB";
    public static final String TABLE_NAME = "normal_notes";

    //frequency
    public static final byte FREQUENCY_NEVER = 0;
    public static final byte FREQUENCY_DAILY = 1;
    public static final byte FREQUENCY_WEEKLY = 2;
    public static final byte FREQUENCY_MONTHLY = 3;
    public static final byte FREQUENCY_YEARLY = 4;

    public static final long MILLISECONDS_IN_DAY = 1000L * 60L * 60L * 24L;

    // for notification
    public static final String CHANEL_ID = "First";
    public static final String SOUND_URI = "uri://sadfasdfasdf.mp3";
    public static final int COLOR_ARGB_BACKLIGHTING = Color.BLUE;

    //  when we move to a new activity to get the result we need to be indexed by its number
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_EDIT_NOTE = 2;


    // SharedPreferences CONSTANTS
    //mode
    public final static int PRIVATE_MODE = 0;
    // Shared preferences file name
    public static final String PREF_NAME = "androidhive-welcome";
    //preferences keys
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String NIGHT_MODE = "NightMode";
}
