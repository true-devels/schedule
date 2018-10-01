package com.company.schedule.model.system;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.interactor.MyNoteInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.presentation.presenter.MainPresenter;
import com.company.schedule.presentation.ui.activities.MainActivity;
import com.company.schedule.presentation.ui.fragments.UpdateNoteFragment;
import com.company.schedule.utils.Constants;
import com.company.schedule.utils.DateConverter;
import com.company.schedule.view.MainView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.company.schedule.utils.Constants.CHANEL_ID;
import static com.company.schedule.utils.Constants.FREQUENCY_DAILY;
import static com.company.schedule.utils.Constants.FREQUENCY_MONTHLY;
import static com.company.schedule.utils.Constants.FREQUENCY_WEEKLY;
import static com.company.schedule.utils.Constants.FREQUENCY_YEARLY;
import static com.company.schedule.utils.Constants.MILLISECONDS_IN_DAY;

public class MyNotification {

    private Context context;
    private MyNoteInteractor interactor;
    private MainActivity mainActivity;

    public MyNotification(Context context, MainActivity mainActivity) {
        this.context = context;
        this.interactor = new MyNoteInteractor(new MainRepository(
                AppDatabase.getDatabase(context).noteDAO(),
                new AppSchedulers()  // for threads
        ));  // create repository and get DAO);
        this.mainActivity = mainActivity;
    }

    // TODO check if it can be static
    public android.app.Notification getNotification(String title, String content) {
        NotificationCompat.Builder builder;
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntentResult = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();

            builder = new NotificationCompat.Builder(context,CHANEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)  // default icon TODO change to good icon
                    .setContentTitle(title)
                    .setContentText(content)
                    //TODO check these three lines work
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Constants.COLOR_ARGB_BACKLIGHTING, 3000, 3000)
                    .setSound(Uri.parse(Constants.SOUND_URI))
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntentResult);
        }
        else{
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)  // default icon TODO change to good icon
                    .setContentTitle(title)
                    .setContentText(content)
                    //TODO check these three lines work
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Constants.COLOR_ARGB_BACKLIGHTING, 3000, 3000)
                    .setSound(Uri.parse(Constants.SOUND_URI))
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntentResult);

        }
        return builder.build();
    }

    public void scheduleNotification(Notification notification, AlarmManager alarmManager, int id, Note note) {


        //TODO make correct intentfilter

      //  mainActivity.registerReceiver( new NotificationPublisher(), IntentFilter.create("com.company.schedule.model.system.MyNotification$NotificationPublisher","text/plain"));

        Intent notification_Intent = new Intent(context, NotificationPublisher.class);

        notification_Intent.putExtra(Constants.NOTIFICATION_ID, id);
        notification_Intent.putExtra(Constants.NOTIFICATION, notification);
        //notification_Intent.putExtra(NotificationPublisher.MAIN_ACTIVITY_INSTANCE, context);

        GregorianCalendar next;

        PendingIntent pendingIntent;  // TODO comment it
        pendingIntent = PendingIntent.getBroadcast(context, id, notification_Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(note.getDate().getTimeInMillis()<= new GregorianCalendar().getTimeInMillis() & note.getFrequency()!=-1){
            next = new GregorianCalendar();
            switch (note.getFrequency()){
                case FREQUENCY_DAILY:
                    next.setTimeInMillis(next.getTimeInMillis()+MILLISECONDS_IN_DAY);  // Daily
                    break;
                case FREQUENCY_WEEKLY:
                    next.setTimeInMillis(next.getTimeInMillis()+MILLISECONDS_IN_DAY*7); // Weekly
                    break;
                case FREQUENCY_MONTHLY: // Monthly
                    GregorianCalendar local = new GregorianCalendar();
                    local.set(Calendar.MONTH,local.get(Calendar.MONTH)+1);
                    if(next.get(Calendar.DAY_OF_MONTH)>local.getActualMaximum(Calendar.DAY_OF_MONTH)){
                        next.set(Calendar.MONTH,local.get(Calendar.MONTH));
                        next.set(Calendar.DAY_OF_MONTH, local.getActualMaximum(Calendar.DAY_OF_MONTH));
                    }else{
                        next.set(Calendar.MONTH,Calendar.MONTH+1);
                    }
                    break;
                case FREQUENCY_YEARLY:
                    GregorianCalendar local2 = new GregorianCalendar();  // Yearly
                    local2.set(Calendar.YEAR,local2.get(Calendar.YEAR)+1);
                     if(next.getActualMaximum(Calendar.DAY_OF_YEAR)!=local2.getActualMaximum(Calendar.DAY_OF_YEAR) && next.get(Calendar.DAY_OF_YEAR)>59)
                     {
                        if(next.getActualMaximum(Calendar.DAY_OF_YEAR)>local2.getActualMaximum(Calendar.DAY_OF_YEAR)){
                            next.set(Calendar.DAY_OF_YEAR,next.get(Calendar.DAY_OF_YEAR)-1);
                            next.set(Calendar.YEAR,next.get(Calendar.YEAR)+1);
                        }else{
                            next.set(Calendar.DAY_OF_YEAR,next.get(Calendar.DAY_OF_YEAR)+1);
                            next.set(Calendar.YEAR,next.get(Calendar.YEAR)-1);
                        }

                     }else{
                         next.set(Calendar.YEAR,next.get(Calendar.YEAR)+1);
                     }
                     break;


            }
            note.setDate(next);
            interactor.updateNote(note);
        }
        notification_Intent.putExtra(Constants.NOTE, note);

        AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, note.getDate().getTimeInMillis(), pendingIntent);
        //setting frequency
        /*long frequencyInMillis = getFrequencyInMillis(selectedItem);

        if (frequencyInMillis == -1) {
            //if user switched button 'repeat' off
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            //if user switched button 'repeat' on, then we send also time of frequency
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, frequencyInMillis, pendingIntent);
        }*/
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

    private long getFrequencyInMillis(int selectedItem){
        long day = MILLISECONDS_IN_DAY; // 86 400 000 milliseconds in a day
        switch (selectedItem) {
            case FREQUENCY_DAILY:
                return day;  // Daily
            case FREQUENCY_WEEKLY:
                return day * 7L;  // Weekly
            case FREQUENCY_MONTHLY:
                return day * 30L;  // Monthly
            case FREQUENCY_YEARLY:
                return day * 365L;  // Yearly
            default:  // FREQUENCY_NEVER(case 0)
                return -1;
        }
    }

   public  class NotificationPublisher extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = intent.getParcelableExtra(Constants.NOTIFICATION);
            int id = intent.getIntExtra(Constants.NOTIFICATION_ID, 0);

            Log.v("Publ",""+id);
            notificationManager.notify(id, notification);
            Note note = intent.getParcelableExtra("note");


            MyNotification myNotification = new MyNotification(context, mainActivity);
            note.getDate().set(Calendar.SECOND,0);
            note.getDate().set(Calendar.MILLISECOND,0);
            Notification local = myNotification.getNotification(note.getName(), note.getContent());

            myNotification.scheduleNotification(local,
                    (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE),
                    id,
                    note
            );
        }
    }

}
