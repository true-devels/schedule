package com.company.schedule.model.system;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.company.schedule.R;
import com.company.schedule.utils.Constants;
import com.company.schedule.utils.NotificationPublisher;

public class MyNotification {

    private Context context;

    public MyNotification(Context context) {
        this.context = context;
    }

    // TODO check if it can be static
    public android.app.Notification getNotification(String title, String content) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            builder = new NotificationCompat.Builder(context,"First")
                    .setSmallIcon(R.mipmap.ic_launcher)  // default icon TODO change to good icon
                    .setContentTitle(title)
                    .setContentText(content)
                    //TODO check these three lines work
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Constants.COLOR_ARGB_BACKLIGHTING, 3000, 5000)
                    .setSound(Uri.parse(Constants.SOUND_URI))
                    .setContentText(content); }
        else{
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)  // default icon TODO change to good icon
                    .setContentTitle(title)
                    .setContentText(content)
                    //TODO check these three lines work
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Constants.COLOR_ARGB_BACKLIGHTING, 3000, 5000)
                    .setSound(Uri.parse(Constants.SOUND_URI))
                    .setContentText(content);

        }
        return builder.build();
    }

    public void scheduleNotification(Notification notification, long time, int selectedItem, AlarmManager alarmManager) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);  // TODO comment it (why value == 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent;  // TODO comment it
        pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //setting frequency
        long frequencyInMillis = getFrequencyInMillis(selectedItem);

        if (frequencyInMillis == -1) {
            //if user switched button 'repeat' off
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            //if user switched button 'repeat' on, then we send also time of frequency
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, frequencyInMillis, pendingIntent);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notes";
            String description = "Notes_Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("First", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private long getFrequencyInMillis(int selectedItem){
        long day = 1000L * 60L * 60L * 24L; // 86 400 000 milliseconds in a day
        switch (selectedItem) {
            case 1:
                return day;  // Daily
            case 2:
                return day * 7L;  // Weekly
            case 3:
                return day * 30L;  // Monthly
            case 4:
                return day * 365L;  // Yearly
            default:  // Once(case 0)
                return  -1;
        }
    }
}
