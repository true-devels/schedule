package com.company.schedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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


//                                   for activity      for button method onClick(View v), for switch onCheckedChanged(CB cB, bool b),              for Date and Time picker

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener,   CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    final private String TAG = "myLog AddNoteActivity";  // tag for log

    EditText etNameNote;  // EditText for enter name of note
    LinearLayout llDateTime;  // LinearLayout which contain two object(id.editDate, id.editTime)
    TextView editDate, editTime;
    Date dateNotification;



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

        dateNotification = new Date();  // get settings for current time
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btnSubmitNote:  // if button send note to DB already pressed
            final String noteName = etNameNote.getText().toString();

            if (!noteName.isEmpty()){

                Intent intentReturnNoteData = new Intent();  // return ready note to MainActivity to DB
                intentReturnNoteData.putExtra("note_name", noteName);
                intentReturnNoteData.putExtra("year", dateNotification.getYear());
                intentReturnNoteData.putExtra("month", dateNotification.getMonth());
                intentReturnNoteData.putExtra("day", dateNotification.getDay());
                intentReturnNoteData.putExtra("hour", dateNotification.getHour());
                intentReturnNoteData.putExtra("minute", dateNotification.getMinute());

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
            dateNotification.update();  // update to current date/time
            dateNotification.setMinute(dateNotification.getMinute()+1);  // increment on 1 minute
            //TODO test what be if minute == 60 and we +1
            editDate.setText(dateNotification.getDate());
            editTime.setText(dateNotification.getTime());
            llDateTime.setVisibility(View.VISIBLE);  // and all View in ViewGroup become visible and exist
            Log.d(TAG, "onCheckedChanged Calendar: " +dateNotification.getDate()+" "+dateNotification.getTime());
        } else {  // else gone  EditText for Date and for Time
            llDateTime.setVisibility(View.GONE);  // all View in ViewGroup become invisible and doesn't exist
        }

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        dateNotification.setDate(year, month, dayOfMonth);  // setting date
        editDate.setText(dayOfMonth + "." + month + "." + year);  // when user chose a date we switch it in TV

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        dateNotification.setTime(hourOfDay, minute);  // setting time
        editTime.setText(hourOfDay + ":" + minute);  // this function called when user chose a time

    }

}
