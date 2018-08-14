package com.company.schedule.ui.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.company.schedule.presenter.AddNotePresenter;
import com.company.schedule.R;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;
import com.company.schedule.utils.DateFormat;
import com.company.schedule.view.AddNoteView;

import java.util.Calendar;
import java.util.GregorianCalendar;


//                                                                            for button method onClick(MainView v), for switch onCheckedChanged(CB cB, bool b),              for Date and Time picker

public class AddNoteActivity extends AppCompatActivity implements AddNoteView, View.OnClickListener, CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private AddNotePresenter presenter = new AddNotePresenter();

    final private String TAG = "myLog AddNoteActivity";  // tag for log

    EditText etNameNote, etContentNote;  // EditTexts for enter name and content of note
    LinearLayout llDateTime;  // LinearLayout which contain two object(id.editDate, id.editTime)
    TextView editDate, editTime;
    GregorianCalendar dateNotification;
    Spinner spinnerFreq;
    Switch swtRemindMe;
    boolean isEdited = false, isReminded = false;
    int id = -1;
    GregorianCalendar gcEditDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        presenter.attachView(this);


        etNameNote =  findViewById(R.id.etNameNote);  // to enter a note name
        etContentNote =  findViewById(R.id.etNameContent);  // to enter a note content


        editDate =  findViewById(R.id.editDate);  // to enter a date
        editDate.setOnClickListener(this);  // when we click, the calendar pops up to enter a date

        editTime =  findViewById(R.id.editTime);  // to enter a time
        editTime.setOnClickListener(this);  // when we click, the watch pops up to enter a time

        Button btnSubmitNote =  findViewById(R.id.btnSubmitNote);  // when button click, sends result to MainActivity
        btnSubmitNote.setOnClickListener(this);  // set listener (MainView.OnClickListener, name @Override method is onClick)

        swtRemindMe =  findViewById(R.id.swtRemindMe);  // access user, add date and time to note if isChecked == true
        swtRemindMe.setOnCheckedChangeListener(this);  // set listener (CompoundButton.OnCheckedChangeListener, name @Override method is onCheckedChanged)


        llDateTime = findViewById(R.id.llDateTime);  // by default visibility == gone
        //llDateTime.setVisibility(MainView.GONE);

        dateNotification = new GregorianCalendar();// get settings for current time
        dateNotification.setTimeInMillis(System.currentTimeMillis());

        FloatingActionButton fab_delete =  findViewById(R.id.fab_delete);  // button for jump to AddNoteActivity
        fab_delete.setOnClickListener(this);  // setting handle

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
            isEdited = true;
            id = extras.getInt("id", -1);
            etNameNote.setText(extras.getString("name"));
            etContentNote.setText(extras.getString("content"));

            gcEditDate = (GregorianCalendar) extras.get("date");

            if (gcEditDate == null) {
                //if field 'date' of CustomNotify object is null, so notify shouldn't be reminded
                swtRemindMe.setChecked(false);
            } else {
                swtRemindMe.setChecked(true);

                // output date in good format
                editDate.setText(DateFormat.getDate(gcEditDate));
                editTime.setText(DateFormat.getTime(gcEditDate));

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
            presenter.pressedToSubmitNote();
            break;
        case R.id.editDate:  //if clicking on TextView with date
            presenter.pressedToEditDate();
            break;
        case R.id.editTime:   //if clicking on TextView with time
            presenter.pressedToEditTime();
            break;
        case R.id.fab_delete:
            presenter.pressedToFabDelete();
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
                presenter.changedRemindMe(isChecked);
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

    @Override
    public void remindMeIsChecked() {
        dateNotification = (GregorianCalendar) Calendar.getInstance(); //.setTimeInMillis(System.currentTimeMillis());  // update to current date/time
        dateNotification.set(Calendar.SECOND, 0);
        // dateNotification.set(GregorianCalendar.MINUTE, dateNotification.get(GregorianCalendar.MINUTE));  // increment on 1 minute (this line was tested on bug, everything OK)

        // output date depending on local settings
        editDate.setText(DateFormat.getDate(dateNotification));
        editTime.setText(DateFormat.getTime(dateNotification));

        llDateTime.setVisibility(View.VISIBLE);  // and all MainView in ViewGroup become visible and exist (date and time)
        isReminded = true;
        Log.v(TAG, "onCheckedChanged dateNotification.get(): " + editDate.getText().toString() + " " + editTime.getText().toString());
    }

    @Override
    public void remindMeIsNotChecked() {
//      gone  EditText for Date and for Time
        llDateTime.setVisibility(View.GONE);  // all MainView in ViewGroup become invisible and doesn't exist
        isReminded = false;
    }

    // pickers
    @Override
    public void showDatePicker(DatePickerFragment datePickerFragment) {
        datePickerFragment.show(getSupportFragmentManager(), "date picker");  // show date picker dialog
    }

    @Override
    public void showTimePicker(TimePickerFragment timePickerFragment) {
        timePickerFragment.show(getSupportFragmentManager(), "time picker");  // show time picker dialog
    }
    // setters
    @Override
    public void setResultOK(Intent data) {
        setResult(RESULT_OK, data);
    }

    @Override
    public void setResultCancel() {
        setResult(RESULT_CANCELED);
    }

    //getters
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTextFromNameNote() {
        return etNameNote.getText().toString();
    }

    @Override
    public String getTextFromContentNote() {
        return etContentNote.getText().toString();
    }

    @Override
    public boolean getIsReminded() {
        return isReminded;
    }

    @Override
    public boolean getIsEdited() {
        return isEdited;
    }

    @Override
    public GregorianCalendar getDateNotification() {
        return dateNotification;
    }

    @Override
    public GregorianCalendar getGcEditDate() {
        return gcEditDate;
    }

    @Override
    public Spinner getSpinnerFreq() {
        return spinnerFreq;
    }

    @Override
    public Context getContext() {
        return AddNoteActivity.this;
    }

    @Override
    public AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}


