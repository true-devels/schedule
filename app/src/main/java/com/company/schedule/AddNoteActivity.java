package com.company.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    EditText etNameNote;
    Button btnSubmitNote;
    final String TAG = "myLog AddNoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        etNameNote = (EditText) findViewById(R.id.etNameNote);
        btnSubmitNote = (Button) findViewById(R.id.btnSubmitNoteName);
        btnSubmitNote.setOnClickListener(this);

        Switch swtRemindMe = (Switch) findViewById(R.id.swtRemindMe);
        swtRemindMe.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitNoteName:
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Toast.makeText(this, (b ? "on" : "off"),
                Toast.LENGTH_SHORT).show();

    }
}
