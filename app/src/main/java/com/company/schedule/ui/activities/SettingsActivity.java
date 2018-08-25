package com.company.schedule.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.company.schedule.R;
import com.company.schedule.utils.SharedPrefs;

public class SettingsActivity extends AppCompatActivity {
    private Switch mSwitch;
    private SharedPrefs sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
        sharedPrefs = new SharedPrefs(this);
        if(sharedPrefs.loadNightModeState()==true) {
            //dark
            setTheme(R.style.darktheme);
        }else {
            //white
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        //init
        mSwitch = findViewById(R.id.myswitch);
        if (sharedPrefs.loadNightModeState()==true) {
            mSwitch.setChecked(true);
        }

        //set mode with switch widget
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setNightModeState(true);
                    restartSettings();
                }
                else {
                    sharedPrefs.setNightModeState(false);
                    restartSettings();
                }
            }
        });
    }

    //restarts methods
    public void restartSettings() {
        Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
        finish();
    }
    public void restartMain() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
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

