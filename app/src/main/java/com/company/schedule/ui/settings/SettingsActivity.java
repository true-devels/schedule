package com.company.schedule.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

import com.company.schedule.R;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.model.repository.SharedPrefsRepository;

public class SettingsActivity extends AppCompatActivity {
    private SharedPrefsRepository sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
        sharedPrefs = new SharedPrefsRepository(this);

        //set theme
        if(sharedPrefs.isNightMode()) setTheme(R.style.darktheme);
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        //init
        Switch mSwitch = findViewById(R.id.myswitch);
        if (sharedPrefs.isNightMode()) {
            mSwitch.setChecked(true);
        }

        //set mode with switch widget
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) sharedPrefs.setNightModeState(true);
            else sharedPrefs.setNightModeState(false);
            restartSettings();
        });
    }

    //restarts methods
    public void restartSettings() {
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
        finish();
    }
    public void restartMain() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    //restart main activity for set theme color
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        restartMain();
    }
}

