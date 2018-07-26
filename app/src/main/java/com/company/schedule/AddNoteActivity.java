package com.company.schedule;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//                                   for activity      for button(method onClick(View v), for switch onCheckedChanged(CompoundButton cB, boolean b)
public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener{

    final private String TAG = "myLog AddNoteActivity";  // tag for log
    final int REQUEST_CODE_ENTER_DATE = 2;  // indexing EnterDateActivity for startActivityForResult
    final int REQUEST_CODE_ENTER_TIME = 3;  // indexing EnterTimeActivity

    EditText etNameNote;  // EditText for enter name of note
    LinearLayout llDateTime;  // LinearLayout which contain two object(id.etDate, id.etTime)
    TextView tvPreviousShowDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        etNameNote = (EditText) findViewById(R.id.etNameNote);  // to enter a note name

        TextView editDate = (TextView) findViewById(R.id.editDate);  // to enter a date
        editDate.setOnClickListener(this);  // when we click, the calendar pops up to enter a date

        TextView editTime = (TextView) findViewById(R.id.editTime);  // to enter a time
        editTime.setOnClickListener(this);  // when we click, the watch pops up to enter a time

        Button btnSubmitNote = (Button) findViewById(R.id.btnSubmitNote);  // when button click, sends result to MainActivity
        btnSubmitNote.setOnClickListener(this);  // set listener (View.OnClickListener, name @Override method is onClick)

        Switch swtRemindMe = (Switch) findViewById(R.id.swtRemindMe);  // access user, add date and time to note if isChecked == true
        swtRemindMe.setOnCheckedChangeListener(this);  // set listener (CompoundButton.OnCheckedChangeListener, name @Override method is onCheckedChanged)

        llDateTime = (LinearLayout) findViewById(R.id.llDateTime);  // by default visibility == gone
        llDateTime.setVisibility(View.GONE);  // TODO make it line in add_note.xml, and delete it

        tvPreviousShowDateTime = (TextView) findViewById(R.id.tvPreviousShowDateTime);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btnSubmitNote:  // if button send note to DB already pressed
            String noteName = etNameNote.getText().toString();

            if (!noteName.isEmpty()){
                Intent intentReturnNoteData = new Intent();  // return ready note to MainActivity to DB
                intentReturnNoteData.putExtra("note_name", noteName);
                setResult(RESULT_OK, intentReturnNoteData);
                Log.d(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");           }
            else { // if noteName is  empty
                setResult(RESULT_CANCELED);
                Log.d(TAG, "RESULT_CANCELED, noteName: \"" + noteName + "\";");
            }
            finish();
            break;
        case R.id.editDate:  // when we click to EditTextDate
            Intent intentEnterDate = new Intent(AddNoteActivity.this, EnterDateActivity.class);  // intent going to EnterDateActivity
            startActivityForResult(intentEnterDate, REQUEST_CODE_ENTER_DATE);  // for enter date and get back it
            //TODO enter date with dialog window
            break;
        case R.id.editTime:
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
//            Intent intentEnterTime = new Intent(AddNoteActivity.this, EnterTimeActivity.class);  // intent going to EnterTimeActivity
//            startActivityForResult(intentEnterTime, REQUEST_CODE_ENTER_TIME);  // for enter time and get back it
            //TODO enter time
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if data was correct entered
        if (resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "RESULT_OK");
            switch (requestCode) {  // check from which daughter object data come
            case REQUEST_CODE_ENTER_DATE:  // if from EnterDateActivity
                tvPreviousShowDateTime.setText("Reminder set for date "
                        + String.valueOf(data.getIntExtra("day_of_month", 1)) + "."
                        + String.valueOf(data.getIntExtra("month", 1)) + "."
                        + String.valueOf(data.getIntExtra("year", 1900))
                );  // TODO add normal defaultValue
                break;
            case REQUEST_CODE_ENTER_TIME:
                //TODO output result data
                break;
            default:
                Log.d(TAG, "onActivityResult in default");
            }
        } else {
            Log.d(TAG, "RESULT_CANCELED,  requestCode: \""+requestCode+ "\"; resultCode: \""+resultCode+"\";");
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // VISIBLE(0) - ViewGroup exist and it is visible
        // INVISIBLE(4) - ViewGroup exist but invisible. It TAKE place on the screej
        // GONE(8); - ViewGroup don't exist and invisible. It does NOT TAKE place on the screen
        if(isChecked) { // if swtRemindMe.isChecked: show EditText for Date and for Time
            llDateTime.setVisibility(View.VISIBLE);  // and all View in ViewGroup become visible and exist
            //TODO switch current date and time
        } else {  // else gone  EditText for Date and for Time
            llDateTime.setVisibility(View.GONE);  // all View in ViewGroup become invisible and doesn't exist
        }

    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        tvPreviousShowDateTime.setText("Hour: " + hourOfDay + " Minute: " + minute);
    }
}
