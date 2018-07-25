package com.company.schedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EnterTimeActivity extends AppCompatActivity {

    final private String TAG = "myLog EnterTimeActivity";  // tag for log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker);
    }
}
