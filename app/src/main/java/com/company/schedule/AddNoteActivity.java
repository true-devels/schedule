package com.company.schedule;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.schedule.fragments.pickers.DatePickerFragment;
import com.company.schedule.fragments.pickers.TimePickerFragment;

import java.util.Date;
import java.util.GregorianCalendar;


//                                   for activity      for button method onClick(View v), for switch onCheckedChanged(CB cB, bool b),              for Date and Time picker

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener,   CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    final private String TAG = "myLog AddNoteActivity";  // tag for log

    EditText etNameNote;  // EditText for enter name of note
    LinearLayout llDateTime;  // LinearLayout which contain two object(id.editDate, id.editTime)
    TextView editDate, editTime;
    GregorianCalendar dateNotification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);


        etNameNote = (EditText) findViewById(R.id.etNameNote);  // to enter a note name

        editDate = (TextView) findViewById(R.id.editDate);  // to enter a date
        editDate.setOnClickListener(this);  // when we click, the calendar pops up to enter a date

        editTime = (TextView) findViewById(R.id.editTime);  // to enter a time
        editTime.setOnClickListener(this);  // when we click, the watch pops up to enter a time

        Button btnSubmitNote = (Button) findViewById(R.id.btnSubmitNote);  // when button click, sends result to MainActivity
        btnSubmitNote.setOnClickListener(this);  // set listener (View.OnClickListener, name @Override method is onClick)

        Switch swtRemindMe = (Switch) findViewById(R.id.swtRemindMe);  // access user, add date and time to note if isChecked == true
        swtRemindMe.setOnCheckedChangeListener(this);  // set listener (CompoundButton.OnCheckedChangeListener, name @Override method is onCheckedChanged)

        llDateTime = (LinearLayout) findViewById(R.id.llDateTime);  // by default visibility == gone
        llDateTime.setVisibility(View.GONE);  // TODO make it line in add_note.xml, and delete it

        dateNotification = new GregorianCalendar();// get settings for current time
        dateNotification.setTimeInMillis(System.currentTimeMillis());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btnSubmitNote:  // if button send note to DB already pressed
            final String noteName = etNameNote.getText().toString();

            if (!noteName.isEmpty()){

                Notification local = getNotification(noteName);
                scheduleNotification(local,dateNotification.getTimeInMillis());

                Intent intentReturnNoteData = new Intent();  // return ready note to MainActivity to DB
                intentReturnNoteData.putExtra("note_name", noteName);
                intentReturnNoteData.putExtra("year", dateNotification.get(GregorianCalendar.YEAR));
                intentReturnNoteData.putExtra("month", dateNotification.get(GregorianCalendar.MONTH));
                intentReturnNoteData.putExtra("day", dateNotification.get(GregorianCalendar.DAY_OF_MONTH));
                intentReturnNoteData.putExtra("hour", dateNotification.get(GregorianCalendar.HOUR));
                intentReturnNoteData.putExtra("minute", dateNotification.get(GregorianCalendar.MINUTE));
                //Toast.makeText(this,dateNotification.getMonth()+" "+dateNotification.getDay()+" "+dateNotification.getHour()+" "+dateNotification.getMinute(),Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, intentReturnNoteData);
                Log.d(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");
            } else { // if noteName is  empty
                setResult(RESULT_CANCELED);
                Log.d(TAG, "RESULT_CANCELED, noteName: \"" + noteName + "\";");
            }
            finish();
            break;

        case R.id.editDate:  // show dialog for pick date
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            break;
        case R.id.editTime:  // dialog for time picker
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
            break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // VISIBLE(0) - ViewGroup exist and it is visible
        // INVISIBLE(4) - ViewGroup exist but invisible. It TAKE place on the screej
        // GONE(8); - ViewGroup don't exist and invisible. It does NOT TAKE place on the screen
        if(isChecked) { // if swtRemindMe.isChecked: show EditText for Date and for Time
            dateNotification.setTimeInMillis(System.currentTimeMillis());  // update to current date/time
            dateNotification.set(GregorianCalendar.MINUTE, dateNotification.get(GregorianCalendar.MINUTE));  // increment on 1 minute
            //TODO test what be if minute == 60 and we +1
            editDate.setText(dateNotification.get(GregorianCalendar.DAY_OF_MONTH)+"."+dateNotification.get(GregorianCalendar.MONTH)+"."+dateNotification.get(GregorianCalendar.YEAR));
            editTime.setText(dateNotification.get(GregorianCalendar.HOUR)+":"+dateNotification.get(GregorianCalendar.MINUTE));
            llDateTime.setVisibility(View.VISIBLE);  // and all View in ViewGroup become visible and exist
            Log.d(TAG, "onCheckedChanged dateNotification.get(): " +editDate.getText().toString()+" "+editTime.getText().toString());
        } else {  // else gone  EditText for Date and for Time
            llDateTime.setVisibility(View.GONE);  // all View in ViewGroup become invisible and doesn't exist
        }

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        dateNotification.set(GregorianCalendar.YEAR,year);
        dateNotification.set(GregorianCalendar.MONTH,month);
        dateNotification.set(GregorianCalendar.DAY_OF_MONTH,dayOfMonth);
        editDate.setText(dayOfMonth + "." + month+1 + "." + year);  // when user chose a date we switch it in TV

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        dateNotification.set(GregorianCalendar.HOUR,hourOfDay);
        dateNotification.set(GregorianCalendar.MINUTE,minute);

        editTime.setText(hourOfDay + ":" + minute);  // this function called when user chose a time

    }

    private void scheduleNotification(Notification notification, long time) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
       // Toast.makeText(this,"Here" + Long.toString(time-System.currentTimeMillis()),Toast.LENGTH_LONG).show();
    }
    private Notification getNotification(String content) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Alarm!")
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setLights(Color.RED, 3000, 3000)
                        .setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                        .setContentText(content);
        return builder.build();
    }

}
