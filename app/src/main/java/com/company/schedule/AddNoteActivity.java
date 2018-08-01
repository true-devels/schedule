package com.company.schedule;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.company.schedule.fragments.pickers.DatePickerFragment;
import com.company.schedule.fragments.pickers.TimePickerFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;


//                                   for activity      for button method onClick(View v), for switch onCheckedChanged(CB cB, bool b),              for Date and Time picker

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    final private String TAG = "myLog AddNoteActivity";  // tag for log

    EditText etNameNote, etContentNote;  // EditTexts for enter name and content of note
    LinearLayout llDateTime;  // LinearLayout which contain two object(id.editDate, id.editTime)
    TextView editDate, editTime;
    GregorianCalendar dateNotification;
    Spinner spinner_freq;
    Switch swtRemindMe, swtRepeat;
    boolean isEdited = false, isReminded = false, isRepeated = false;
    int id = -1;
    GregorianCalendar edit_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);


        etNameNote = findViewById(R.id.etNameNote);  // to enter a note name
        etContentNote = findViewById(R.id.etNameContent);  // to enter a note content


        editDate = findViewById(R.id.editDate);  // to enter a date
        editDate.setOnClickListener(this);  // when we click, the calendar pops up to enter a date

        editTime = findViewById(R.id.editTime);  // to enter a time
        editTime.setOnClickListener(this);  // when we click, the watch pops up to enter a time

        Button btnSubmitNote = findViewById(R.id.btnSubmitNote);  // when button click, sends result to MainActivity
        btnSubmitNote.setOnClickListener(this);  // set listener (View.OnClickListener, name @Override method is onClick)

        swtRemindMe = findViewById(R.id.swtRemindMe);  // access user, add date and time to note if isChecked == true
        swtRemindMe.setOnCheckedChangeListener(this);  // set listener (CompoundButton.OnCheckedChangeListener, name @Override method is onCheckedChanged)

        swtRepeat = findViewById(R.id.swtRepeat);  // access user, add date and time to note if isChecked == true
        swtRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    spinner_freq.setVisibility(View.VISIBLE);
                    isRepeated = true;
                } else {
                    spinner_freq.setVisibility(View.GONE);
                    isRepeated = false;
                }

            }
        });

        llDateTime = findViewById(R.id.llDateTime);  // by default visibility == gone
        llDateTime.setVisibility(View.GONE);  // TODO make it line in add_note.xml, and delete it

        dateNotification = new GregorianCalendar();// get settings for current time
        dateNotification.setTimeInMillis(System.currentTimeMillis());

        spinner_freq = findViewById(R.id.types_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_of_frequency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_freq.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            id = extras.getInt("id", -1);
            etNameNote.setText(extras.getString("name"));
            etContentNote.setText(extras.getString("content"));
            edit_date = (GregorianCalendar) extras.get("date");

            if (edit_date == null) {
                swtRemindMe.setChecked(false);
            } else {
                isEdited = true;
                swtRemindMe.setChecked(true);

                editDate.setText(edit_date.get(GregorianCalendar.DAY_OF_MONTH) + "." + edit_date.get(GregorianCalendar.MONTH) + "." + edit_date.get(GregorianCalendar.YEAR));
                editTime.setText(edit_date.get(GregorianCalendar.HOUR) + ":" + edit_date.get(GregorianCalendar.MINUTE));


                if (extras.getByte("frequency") != -1) {
                    swtRepeat.setChecked(true);
                    spinner_freq.setSelection((int) extras.getByte("frequency"));
                } else {
                    swtRepeat.setChecked(false);
                }

            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitNote:  // if button send note to DB already pressed
                final String noteName = etNameNote.getText().toString();
                final String noteContent = etContentNote.getText().toString();


                if (!noteName.isEmpty()) {
                    Intent intentReturnNoteData = new Intent();  // return ready note to MainActivity to DB
                    intentReturnNoteData.putExtra("id", id);
                    intentReturnNoteData.putExtra("note_name", noteName);
                    intentReturnNoteData.putExtra("note_content", noteContent);
                    if (isReminded) {
                        Notification local = getNotification(noteName, noteContent);
                        scheduleNotification(local, dateNotification.getTimeInMillis(), spinner_freq.getSelectedItemPosition());
                        intentReturnNoteData.putExtra("year", dateNotification.get(GregorianCalendar.YEAR));
                        intentReturnNoteData.putExtra("month", dateNotification.get(GregorianCalendar.MONTH));
                        intentReturnNoteData.putExtra("day", dateNotification.get(GregorianCalendar.DAY_OF_MONTH));
                        intentReturnNoteData.putExtra("hour", dateNotification.get(GregorianCalendar.HOUR));
                        intentReturnNoteData.putExtra("minute", dateNotification.get(GregorianCalendar.MINUTE));
                    } else {
                        intentReturnNoteData.putExtra("year", -1);
                        intentReturnNoteData.putExtra("month", -1);
                        intentReturnNoteData.putExtra("day", -1);
                        intentReturnNoteData.putExtra("hour", -1);
                        intentReturnNoteData.putExtra("minute", -1);
                    }
                    if (isRepeated) {
                        int frequency = spinner_freq.getSelectedItemPosition();
                        intentReturnNoteData.putExtra("freq", frequency);
                    } else {
                        intentReturnNoteData.putExtra("freq", -1);
                    }

                    //Toast.makeText(this,dateNotification.getMonth()+" "+dateNotification.getDay()+" "+dateNotification.getHour()+" "+dateNotification.getMinute(),Toast.LENGTH_LONG).show();
                    if (isEdited) {

                    } else {

                    }
                    setResult(RESULT_OK, intentReturnNoteData);
                    Log.v(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");
                } else { // if noteName is  empty
                    setResult(RESULT_CANCELED);
                    Log.v(TAG, "RESULT_CANCELED, noteName: \"" + noteName + "\";");
                }
                finish();
                break;

            case R.id.editDate:

                DatePickerFragment datePicker = new DatePickerFragment();
                if (isEdited) {
                    datePicker.setGc(edit_date);
                }
                datePicker.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.editTime:  // dialog for time picker
                TimePickerFragment timePicker = new TimePickerFragment();
                if (isEdited) {
                    timePicker.setGc(edit_date);
                }
                timePicker.show(getSupportFragmentManager(), "time picker");
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // VISIBLE(0) - ViewGroup exist and it is visible
        // INVISIBLE(4) - ViewGroup exist but invisible. It TAKE place on the screej
        // GONE(8); - ViewGroup don't exist and invisible. It does NOT TAKE place on the screen
        if (isChecked) { // if swtRemindMe.isChecked: show EditText for Date and for Time
            dateNotification.setTimeInMillis(System.currentTimeMillis());  // update to current date/time
            dateNotification.set(GregorianCalendar.MINUTE, dateNotification.get(GregorianCalendar.MINUTE) + 1);  // increment on 1 minute (this line was tested on bug, everything OK)
            editDate.setText(dateNotification.get(GregorianCalendar.DAY_OF_MONTH) + "." + dateNotification.get(GregorianCalendar.MONTH) + "." + dateNotification.get(GregorianCalendar.YEAR));
            editTime.setText(dateNotification.get(GregorianCalendar.HOUR) + ":" + dateNotification.get(GregorianCalendar.MINUTE));
            llDateTime.setVisibility(View.VISIBLE);  // and all View in ViewGroup become visible and exist
            isReminded = true;
            Log.v(TAG, "onCheckedChanged dateNotification.get(): " + editDate.getText().toString() + " " + editTime.getText().toString());
        } else {
            // else gone  EditText for Date and for Time
            spinner_freq.setVisibility(View.GONE);
            llDateTime.setVisibility(View.GONE);  // all View in ViewGroup become invisible and doesn't exist
            isReminded = false;
        }

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        dateNotification.set(GregorianCalendar.YEAR, year);
        dateNotification.set(GregorianCalendar.MONTH, month);
        dateNotification.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);
        editDate.setText(dayOfMonth + "." + month + 1 + "." + year);  // when user chose a date we switch it in TV

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        dateNotification.set(GregorianCalendar.HOUR, hourOfDay);
        dateNotification.set(GregorianCalendar.MINUTE, minute);

        editTime.setText(hourOfDay + ":" + minute);  // this function is called when user chose a time

    }

    private void scheduleNotification(Notification notification, long time, int freq) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        long frequency, day = 1000L * 60L * 60L * 24L; // 86 400 000 milliseconds in a day
        switch (freq) {
            case 1:
                frequency = day;  // Daily
                break;
            case 2:
                frequency = day * 7L;  // Weekly
                break;
            case 3:
                frequency = day * 30L;  // Monthly
                break;
            case 4:
                frequency = day * 365L;  // Yearly
                break;
            default:  // Once(case 0)
                frequency = -1;
        }
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (frequency == -1) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, frequency, pendingIntent);
        }


        // Toast.makeText(this,"Here" + Long.toString(time-System.currentTimeMillis()),Toast.LENGTH_LONG).show();
    }

    private Notification getNotification(String title, String content) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)  // TODO Builder is deprecated
                        .setSmallIcon(R.mipmap.ic_launcher)  // TODO change to good icon
                        .setContentTitle(title)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.RED, 3000, 3000)
                        .setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                        .setContentText(content);
        return builder.build();
    }


}


