package com.company.schedule.ui.settings;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;

import com.company.schedule.R;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.ui.welcome.SpinnerAdapter;
import com.company.schedule.utils.Constants;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private SharedPrefsRepository sharedPrefs;
    SpinnerAdapter spinnerAdapter;
    Spinner localization;
    boolean isUserInteracting;
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
        localization = findViewById(R.id.spinnerLang);

        int[] icons = { R.drawable.ua, R.drawable.ru, R.drawable.en };
        String[] names = { "UA", "RU", "EN"};
        spinnerAdapter = new SpinnerAdapter(this, names, icons);
        localization.setAdapter(spinnerAdapter);

        switch (sharedPrefs.getLocalization()){
            case Constants.LOCALIZATION_EN:
                localization.setSelection(2);
                break;
            case Constants.LOCALIZATION_RU:
                localization.setSelection(1);
                break;
            case Constants.LOCALIZATION_UA:
                localization.setSelection(0);
                break;
        }


        localization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                if (isUserInteracting) {
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    android.content.res.Configuration conf = res.getConfiguration();
                    switch (i){
                        case 0:
                            conf.setLocale(new Locale("uk"));
                            res.updateConfiguration(conf, dm);
                            sharedPrefs.setLocalization(Constants.LOCALIZATION_UA);
                            recreate();
                            break;
                        case 1:
                            conf.setLocale(new Locale("ru"));
                            res.updateConfiguration(conf, dm);
                            sharedPrefs.setLocalization(Constants.LOCALIZATION_RU);
                            recreate();
                            break;
                        case 2:
                            conf.setLocale(new Locale("en"));
                            sharedPrefs.setLocalization(Constants.LOCALIZATION_EN);
                            res.updateConfiguration(conf, dm);
                            recreate();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }

        });

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

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracting = true;
    }
}

