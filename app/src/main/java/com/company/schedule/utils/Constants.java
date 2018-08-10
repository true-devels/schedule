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

    // for notification
    public static final int COLOR_ARGB_BACKLIGHTING = Color.BLUE;
    public static final String SOUND_URI = "uri://sadfasdfasdf.mp3";

    //  when we move to a new activity to get the result we need to be indexed by its number
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_EDIT_NOTE = 2;
}
