package com.company.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView testOutputNoteName;// TODO delete the testOutputNoteName
//  when we move to a new activity to get the result we need to be indexed by its number
    final int REQUEST_CODE_ADD_NOTE = 1;
    final String TAG = "myLog MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        testOutputNoteName = (TextView) findViewById(R.id.testOutputNoteName);  // test TV for test result

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.fab:
            // we indicate an explicit transition to AddNoteActivity to enter the data of a note
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            // and getting this information back. (using REQUEST_CODE_ADD_NOTE (1) we can find out that the result came exactly with AddNoteActivity)
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        on requestCode we determine from which subsidiary activity the result came
//        resultCode - return code. Determines whether the call has passed successfully or not.
//        data - Intent, in which the data is returned

        // if data was correct entered
        if (resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "RESULT_OK");
            switch (requestCode) {  // check from which daughter object data come
            case REQUEST_CODE_ADD_NOTE:  // if from AddNoteActivity
                String noteName = data.getStringExtra("note_name");
                Log.d(TAG, "case REQUEST_CODE_ADD_NOTE, noteName: \"" + noteName + "\";");

                testOutputNoteName.setText(noteName);//TODO delete this line
                //TODO to DB element with name noteName
                break;
            default:
                Log.d(TAG, "onActivityResult in default");
            }
        } else if (requestCode == RESULT_CANCELED) {
            // TODO output error
            Log.d(TAG, "RESULT_CANCELED");
        }
        else {
            //TODO output error
            Log.d(TAG, "data == null or any different error, requestCode: \"" + requestCode + "\";");
        }
    }
}
