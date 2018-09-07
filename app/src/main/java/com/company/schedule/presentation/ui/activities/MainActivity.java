package com.company.schedule.presentation.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.company.schedule.R;
import com.company.schedule.presentation.ui.fragments.MainFragment;
import com.company.schedule.presentation.ui.fragments.UpdateNoteFragment;
import com.company.schedule.utils.SharedPrefs;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
        SharedPrefs sharedPrefs = new SharedPrefs(this);

        if(sharedPrefs.isNightMode()) setTheme(R.style.darktheme);  //dark
        else setTheme(R.style.AppTheme);  //white

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar =  findViewById(R.id.toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);

        // init fragment
        MainFragment mainFragment = new MainFragment();
//        UpdateNoteFragment updateNoteFragment = new UpdateNoteFragment();
        // open fragment transaction
        addFragment(mainFragment);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment);  // add fragment to screen
//        if (useBackStack) fragmentTransaction.addToBackStack(null); // feature
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, boolean useBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);  // add fragment to screen
        useBackStack = false; //TODO delete it in future
        if (useBackStack) fragmentTransaction.addToBackStack(null); // feature
        fragmentTransaction.commit();
    }

    private void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);  // add fragment to screen
//        if (useBackStack) fragmentTransaction.addToBackStack(null); // feature
        fragmentTransaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}

