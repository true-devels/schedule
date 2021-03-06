package com.company.schedule.utils;

import android.graphics.Color;

public final class Constants {
    // for DB
    public static final int DATABASE_VERSION = 18;
    public static final String DATABASE_NAME = "Notifies-DB2";
    public static final String TABLE_NAME = "normal_notes";

    //frequency
    public static final byte FREQUENCY_ONCE = 0;
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


    //Notification CONSTANTS
    public static final String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION = "notification";


    // SharedPreferences CONSTANTS
    //mode
    public final static int PRIVATE_MODE = 0;
    // Shared preferences file name
    public static final String PREF_NAME = "scheduler_prefs";
    //preferences keys
    public static final String FIRST_TIME_LAUNCH_KEY = "FirstTimeLaunch";
    public static final String NIGHT_MODE = "NightMode";
    public static final String LOCALIZATION = "localization";
    public static final String LOCALIZATION_UA = "uk";
    public static final String LOCALIZATION_EN = "en";
    public static final String LOCALIZATION_RU = "ru";
    public static final String LAST_TIME_DAILY = "LastTimeDaily";
    public static final String LAST_TIME_WEEKLY = "LastTimeWeekly";
    public static final String LAST_TIME_MONTHLY = "LastTimeMonthly";
    public static final String STATISTIC_SET = "set";
    public static final String LAST_TIME_STAT = "LastTimeStat";

    // timer
    public static final String FINISH_TIME = "finishTime";
    public static final String PAUSE_TIME = "pauseTime";



    // TIMER
    public static final long TASK_TIME_SECONDS = 25*60;
    public static final String START_TIMER = "start";
    public static final String PAUSE_TIMER = "pause";
    public static final String RESUME_TIMER = "resume";
    public static final String STOP_TIMER = "finish";

}
