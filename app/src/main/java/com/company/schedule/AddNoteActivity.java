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
import android.support.design.widget.FloatingActionButton;
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
import com.company.schedule.local.DateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;


//                                   for activity      for button method onClick(View v), for switch onCheckedChanged(CB cB, bool b),              for Date and Time picker

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener,   CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    final private String TAG = "myLog AddNoteActivity";  // tag for log

    EditText etNameNote, etContentNote;  // EditTexts for enter name and content of note
    LinearLayout llDateTime;  // LinearLayout which contain two object(id.editDate, id.editTime)
    TextView editDate, editTime;
    GregorianCalendar dateNotification;
    Spinner spinnerFreq;
    Switch swtRemindMe, swtRepeat;
    boolean isEdited = false, isReminded = false, isRepeated=false;
    int id = -1;
    GregorianCalendar edit_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);



        etNameNote =  findViewById(R.id.etNameNote);  // to enter a note name
        etContentNote =  findViewById(R.id.etNameContent);  // to enter a note content


        editDate =  findViewById(R.id.editDate);  // to enter a date
        editDate.setOnClickListener(this);  // when we click, the calendar pops up to enter a date

        editTime =  findViewById(R.id.editTime);  // to enter a time
        editTime.setOnClickListener(this);  // when we click, the watch pops up to enter a time

        Button btnSubmitNote =  findViewById(R.id.btnSubmitNote);  // when button click, sends result to MainActivity
        btnSubmitNote.setOnClickListener(this);  // set listener (View.OnClickListener, name @Override method is onClick)

        swtRemindMe =  findViewById(R.id.swtRemindMe);  // access user, add date and time to note if isChecked == true
        swtRemindMe.setOnCheckedChangeListener(this);  // set listener (CompoundButton.OnCheckedChangeListener, name @Override method is onCheckedChanged)



        llDateTime = findViewById(R.id.llDateTime);  // by default visibility == gone
        //llDateTime.setVisibility(View.GONE);

        dateNotification = new GregorianCalendar();// get settings for current time
        dateNotification.setTimeInMillis(System.currentTimeMillis());

        FloatingActionButton fab =  findViewById(R.id.fab_delete);  // button for jump to AddNoteActivity
        fab.setOnClickListener(this);  // setting handle

        //setting frequency spinner
        spinnerFreq = findViewById(R.id.spinnerFreq);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_of_frequency, android.R.layout.simple_spinner_item);  // TODO comment it
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // TODO comment it
        spinnerFreq.setAdapter(adapter);

        //getting intent from MainActivity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //if note is editing, there is some data, that should be read from intent
        if (extras != null) {
            isEdited=true;
            id = extras.getInt("id", -1);
            etNameNote.setText(extras.getString("name"));
            etContentNote.setText(extras.getString("content"));
            edit_date = (GregorianCalendar) extras.get("date");

            if (edit_date == null) {
                //if field 'date' of CustomNotify object is null, so notify shouldn't be reminded
                swtRemindMe.setChecked(false);
            }else{
                swtRemindMe.setChecked(true);

                // output date in good format
                editDate.setText(DateFormat.getDate(edit_date));
                editTime.setText(DateFormat.getTime(edit_date));

                if (extras.getByte("frequency") != -1) {
                    spinnerFreq.setSelection((int) extras.getByte("frequency"));
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
                    //if switch button 'remind' is on
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
                    Notification local = getNotification(noteName, noteContent);
                    Log.d(TAG, "onClick, btnSubmitNote, isReminder: "+noteName+noteContent+DateFormat.getTime(dateNotification)+"; current time: "+DateFormat.getTime(gregorianCalendar));
                    scheduleNotification(local, dateNotification.getTimeInMillis(), spinnerFreq.getSelectedItemPosition());
                    intentReturnNoteData.putExtra("time_in_millis", dateNotification.getTimeInMillis());
                } else {  // TODO make good default value
                    //if switch button 'remind' is off
                    intentReturnNoteData.putExtra("time_in_millis", -1);
                }
                intentReturnNoteData.putExtra("freq", spinnerFreq.getSelectedItemPosition());

                setResult(RESULT_OK, intentReturnNoteData);
                Log.v(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");
            } else { // if noteName is  empty
                setResult(RESULT_CANCELED);
                Log.v(TAG, "RESULT_CANCELED, noteName: \"" + noteName + "\";");
            }
            finish();
            break;
        case R.id.editDate:  //if clicking on TextView with date
            DatePickerFragment datePicker = new DatePickerFragment(); // calls fragment with date picker dialog
            if (isEdited) {
                //if note is editing, then sending existing date to date picker
                datePicker.setGc(edit_date);
            }
            datePicker.show(getSupportFragmentManager(), "date picker");
            break;
        case R.id.editTime:   //if clicking on TextView with time
            TimePickerFragment timePicker = new TimePickerFragment();// calls fragment with time picker dialog
            if (isEdited) {
                //if note is editing, then sending existing in note time to time picker
                timePicker.setGc(edit_date);
            }
            timePicker.show(getSupportFragmentManager(), "time picker");
            break;
        case R.id.fab_delete:
            if(isEdited){
                Intent intent = new Intent();
                intent.putExtra("isDel",true);
                intent.putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                finish();
            }
            break;
        }
    }


    //method for 'remind' button
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // VISIBLE(0) - ViewGroup exist and it is visible
        // INVISIBLE(4) - ViewGroup exist but invisible. It TAKE place on the screej
        // GONE(8); - ViewGroup don't exist and invisible. It does NOT TAKE place on the screen
        switch (compoundButton.getId()){
        case R.id.swtRemindMe:
            if (isChecked) { // if swtRemindMe.isChecked: show EditText for Date and for Time
                dateNotification = (GregorianCalendar) Calendar.getInstance(); //.setTimeInMillis(System.currentTimeMillis());  // update to current date/time
                dateNotification.set(Calendar.SECOND, 0);
                // dateNotification.set(GregorianCalendar.MINUTE, dateNotification.get(GregorianCalendar.MINUTE));  // increment on 1 minute (this line was tested on bug, everything OK)

                // output date depending on local settings
                editDate.setText(DateFormat.getDate(dateNotification));
                editTime.setText(DateFormat.getTime(dateNotification));

                llDateTime.setVisibility(View.VISIBLE);  // and all View in ViewGroup become visible and exist (date and time)
                isReminded = true;
//            swtRepeat.setVisibility(View.VISIBLE); // switch button 'repeat' appears
                Log.v(TAG, "onCheckedChanged dateNotification.get(): " + editDate.getText().toString() + " " + editTime.getText().toString());
            } else {
            //if switch button 'remind' is switched off
//            swtRepeat.setVisibility(View.GONE);
                // else gone  EditText for Date and for Time
                llDateTime.setVisibility(View.GONE);  // all View in ViewGroup become invisible and doesn't exist
                isReminded = false;
            }
            break;
        default:
            Log.e(TAG, "onCheckedChanged default, compoundButton.getId: " + compoundButton.getId() + "; isChecked: " + isChecked);
        }

    }



    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        //month++; // because computer start count month from 0 to 11
        dateNotification.set(GregorianCalendar.YEAR, year);
        dateNotification.set(GregorianCalendar.MONTH, month);
        dateNotification.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);

        editDate.setText(DateFormat.getDate(dateNotification));  // when user chose a date we switch it in TV in good date format
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        dateNotification.set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
        dateNotification.set(GregorianCalendar.MINUTE,minute);
        dateNotification.set(GregorianCalendar.SECOND, 0);

        editTime.setText(DateFormat.getTime(dateNotification));  // when user chose a date we switch it in TV in good time format
    }

    private void scheduleNotification(Notification notification, long time, int freq) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);  // TODO comment it (why value == 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent;  // TODO comment it
        pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //setting frequency
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

        if (frequency == -1) {
            //if user switched button 'repeat' off
          alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            //if user switched button 'repeat' on, then we send also time of frequency
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, frequency, pendingIntent);
        }
    }

    private Notification getNotification(String title, String content) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)  // TODO Builder is deprecated
                        //default icon
                        .setSmallIcon(R.mipmap.ic_launcher)  // TODO change to good icon
                        .setContentTitle(title)
                        .setContentText(content)
                        //TODO check these three lines work
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.RED, 3000, 3000)
                        .setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                        .setContentText(content);
        return builder.build();
    }




}


