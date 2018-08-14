package com.company.schedule.presenter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.company.schedule.R;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;
import com.company.schedule.utils.Constants;
import com.company.schedule.utils.NotificationPublisher;
import com.company.schedule.view.AddNoteView;

import java.util.GregorianCalendar;

public class AddNotePresenter {

    private AddNoteView view;

    final private String TAG = "myLog AddNotePresenter";  // tag for log

    public void attachView(AddNoteView view) {
        this.view = view;
    }

    public void pressedToSubmitNote() {
        final String noteName = view.getTextFromNameNote();
        final String noteContent = view.getTextFromContentNote();


        if (!noteName.isEmpty()) {
            Intent intentReturnNoteData = new Intent();  // return ready note to MainActivity to DB
            intentReturnNoteData.putExtra("id", view.getId());
            intentReturnNoteData.putExtra("note_name", noteName);
            intentReturnNoteData.putExtra("note_content", noteContent);

            if (view.getIsReminded()) {
                //if switch button 'remind' is on
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
                Notification local = getNotification(noteName, noteContent);

                scheduleNotification(local, view.getDateNotification().getTimeInMillis(), view.getSpinnerFreq().getSelectedItemPosition());
                intentReturnNoteData.putExtra("time_in_millis", view.getDateNotification().getTimeInMillis());
            } else {  // TODO make good default value
                //if switch button 'remind' is off
                intentReturnNoteData.putExtra("time_in_millis", -1);
            }
            intentReturnNoteData.putExtra("freq", view.getSpinnerFreq().getSelectedItemPosition());

            Log.v(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");
            view.setResultOK(intentReturnNoteData);
        } else { // if noteName is  empty
            Log.v(TAG, "RESULT_CANCELED, noteName: \"" + noteName + "\";");
            view.setResultCancel();
        }
        view.finish();  // finish activity and go to MainActivity
    }

    public void pressedToEditDate() {
        DatePickerFragment datePicker = new DatePickerFragment(); // calls fragment with date picker dialog
        if (view.getIsEdited()) {
            //if note is editing, then sending existing date to date picker
            datePicker.setGc(view.getGcEditDate());
        }
        view.showDatePicker(datePicker);
    }

    public void pressedToEditTime() {
        TimePickerFragment timePicker = new TimePickerFragment();// calls fragment with time picker dialog
        if (view.getIsEdited()) {
            //if note is editing, then sending existing in note time to time picker
            timePicker.setGc(view.getGcEditDate());
        }
        view.showTimePicker(timePicker);
    }

    public void pressedToFabDelete() {
        if(view.getIsEdited()){
            Intent intent = new Intent();
            intent.putExtra("isDel",true);
            intent.putExtra("id", view.getId());
            view.setResultOK(intent);
            view.finish();
        }else{
            view.finish();
        }
    }

    public void changedRemindMe(boolean isChecked) {
        if (isChecked) { // if swtRemindMe.isChecked: show EditText for Date and for Time
            view.remindMeIsChecked();
        } else {
            view.remindMeIsNotChecked();
        }
    }


    private Notification getNotification(String title, String content) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel();
        builder =
                new NotificationCompat.Builder(view.getContext(),"First")
                        //default icon
                        .setSmallIcon(R.mipmap.ic_launcher)  // TODO change to good icon
                        .setContentTitle(title)
                        .setContentText(content)
                        //TODO check these three lines work
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Constants.COLOR_ARGB_BACKLIGHTING, 3000, 5000)
                        .setSound(Uri.parse(Constants.SOUND_URI))
                        .setContentText(content); }
        else{
            builder = new NotificationCompat.Builder(view.getContext())
                            //default icon
                            .setSmallIcon(R.mipmap.ic_launcher)  // TODO change to good icon
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
            NotificationManager notificationManager = view.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification(Notification notification, long time, int selectedItem) {
        Intent notificationIntent = new Intent(view.getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);  // TODO comment it (why value == 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent;  // TODO comment it
        pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = view.getAlarmManager();

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

    public void detachView() {
        this.view = null;
    }
}
