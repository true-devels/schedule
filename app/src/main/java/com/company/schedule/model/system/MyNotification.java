package com.company.schedule.model.system;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.NotificationInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.utils.Constants;
import com.company.schedule.utils.NotificationPublisher;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.company.schedule.utils.Constants.CHANEL_ID;
import static com.company.schedule.utils.Constants.FREQUENCY_DAILY;
import static com.company.schedule.utils.Constants.FREQUENCY_MONTHLY;
import static com.company.schedule.utils.Constants.FREQUENCY_WEEKLY;
import static com.company.schedule.utils.Constants.MILLISECONDS_IN_DAY;

public class MyNotification {

    private Context context;
    private NotificationInteractor interactor;


    public MyNotification(Context context) {
        this.context = context;
        this.interactor = new NotificationInteractor(new MainRepository(
                AppDatabase.getDatabase(context).noteDAO(),
                new AppSchedulers()  // for threads
        ));  // create repository and get DAO);

    }

    public android.app.Notification getNotification(Note noteToNotif) {  // String title, String content) {
        NotificationCompat.Builder builder;
        Intent resultIntent = new Intent(context, MainActivity.class);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntentResult = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  // condition must be here to support all version of device
            createNotificationChannel();
            builder = new NotificationCompat.Builder(context,CHANEL_ID)
                    .setContentTitle(noteToNotif.getName())
                    .setContentText(noteToNotif.getContent())
                    .setSmallIcon(R.mipmap.ic_app_round)
                    .setColor(Color.GREEN)
                    .setSound(alarmSound)
                    .setLights(0xff00ff00, 3000, 100)
                    //.setLights(0xff0000ff, 5000, 3000)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntentResult);

        }
        else{
            builder = new NotificationCompat.Builder(context)  // NotificationCompat.Builder must be here to support old version
                    .setContentTitle(noteToNotif.getName())
                    .setContentText(noteToNotif.getContent())
                    .setSmallIcon(R.mipmap.ic_app_round)
                    .setColor(Color.GREEN)
                    .setLights(0xff00ff00, 3000, 3000)
                    //.setLights(Constants.COLOR_ARGB_BACKLIGHTING, 5000, 2000)
                    .setSound(alarmSound)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntentResult);
        }

        return builder.build();
    }


    public void scheduleNotification(Notification notification, int id, Note note) {
      //  mainActivity.registerReceiver( new NotificationPublisher(), IntentFilter.create("com.company.schedule.model.system.MyNotification$NotificationPublisher","text/plain"));

        Intent notification_Intent = new Intent(context, NotificationPublisher.class);

        notification_Intent.putExtra(Constants.NOTIFICATION_ID, id);
        notification_Intent.putExtra(Constants.NOTIFICATION, notification);

        GregorianCalendar next;
        PendingIntent pendingIntent;  // TODO explain it
        pendingIntent = PendingIntent.getBroadcast(context, id, notification_Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(note.getCalendarDate().getTimeInMillis()<= new GregorianCalendar().getTimeInMillis()){
            next = new GregorianCalendar();
            switch (note.getFrequency()){
                case FREQUENCY_DAILY:
                    next.set(Calendar.DAY_OF_YEAR,next.get(Calendar.DAY_OF_YEAR)+1);  // Daily
                    break;
                case FREQUENCY_WEEKLY:
                    next.setTimeInMillis(next.getTimeInMillis()+MILLISECONDS_IN_DAY*7); // Weekly
                    break;
                case FREQUENCY_MONTHLY: // Monthly

                    next.set(Calendar.MONTH,next.get(Calendar.MONTH)+1);
                   /* if(next.get(Calendar.DAY_OF_MONTH)>local.getActualMaximum(Calendar.DAY_OF_MONTH)){
                        next.set(Calendar.MONTH,local.get(Calendar.MONTH));
                        next.set(Calendar.DAY_OF_MONTH, local.getActualMaximum(Calendar.DAY_OF_MONTH));
                    }else{
                        next.set(Calendar.MONTH,Calendar.MONTH+1);
                    }*/
                    break;


            }
            note.setCalendarDate(next);
            interactor.updateNote(note);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, note.getCalendarDate().getTimeInMillis(), pendingIntent);
        Log.v("Final final check",""+id);
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





}
