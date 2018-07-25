package com.company.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

//                                   for activity      for button(method onClick(View v), for switch onCheckedChanged(CompoundButton cB, boolean b)
public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    final private String TAG = "myLog AddNoteActivity";  // tag for log

    EditText etNameNote;  // EditText for enter name of note
    LinearLayout llDateTime;  // LinearLayout which contain two object(id.etDate, id.etTime)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        etNameNote = (EditText) findViewById(R.id.etNameNote);
        Button btnSubmitNote = (Button) findViewById(R.id.btnSubmitNote);
        btnSubmitNote.setOnClickListener(this);  // set listener (View.OnClickListener, name @Override method is onClick)

        Switch swtRemindMe = (Switch) findViewById(R.id.swtRemindMe);  // access user, add date and time to note if isChecked == true
        swtRemindMe.setOnCheckedChangeListener(this);  // set listener (CompoundButton.OnCheckedChangeListener, name @Override method is onCheckedChanged)

        llDateTime = (LinearLayout) findViewById(R.id.llDateTime);  // by default visibility == gone
        llDateTime.setVisibility(View.GONE);  // TODO make it line in add_note.xml, and delete it
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btnSubmitNote:
            Intent intent = new Intent();
            String noteName = etNameNote.getText().toString();

            if (noteName.isEmpty()){
                setResult(RESULT_CANCELED);
                Log.d(TAG, "RESULT_CANCELED, noteName: \"" + noteName + "\";");
            }
            else { // if noteName is not empty
                intent.putExtra("note_name", noteName);

                setResult(RESULT_OK, intent);
                Log.d(TAG, "RESULT_OK, noteName: \"" + noteName + "\";");
            }
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // VISIBLE(0) - ViewGroup exist and it is visible
        // INVISIBLE(4) - ViewGroup exist but invisible. It TAKE place on the screej
        // GONE(8); - ViewGroup don't exist and invisible. It does NOT TAKE place on the screen
        if(isChecked) { // if swtRemindMe.isChecked: show EditText for Date and for Time
            llDateTime.setVisibility(View.VISIBLE);  // and all View in ViewGroup become visible and exist
        } else {  // else gone  EditText for Date and for Time
            llDateTime.setVisibility(View.GONE);  // all View in ViewGroup become invisible and doesn't exist
        }

    }
}
