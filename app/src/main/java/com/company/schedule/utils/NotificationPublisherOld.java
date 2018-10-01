package com.company.schedule.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.system.MyNotification;
import com.company.schedule.presentation.ui.activities.MainActivity;
import com.company.schedule.presentation.ui.fragments.UpdateNoteFragment;

import java.util.Calendar;

//class that show notifications
public class NotificationPublisherOld //extends BroadcastReceiver
{
/*
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String NOTE = "note";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        Log.v("Publ",""+id);
        notificationManager.notify(id, notification);
        Note note = intent.getParcelableExtra("note");

        //TODO remove this with more optimal code
        new UpdateNoteFragment().createNotification(note, id);
    }*/
}