package com.company.schedule.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presenter.MainPresenter;
import com.company.schedule.ui.adapters.CustomLayoutManager;
import com.company.schedule.ui.adapters.NotesAdapter;
import com.company.schedule.ui.fragments.MainFragment;
import com.company.schedule.ui.fragments.UpdateNoteFragment;
import com.company.schedule.utils.SharedPrefs;
import com.company.schedule.view.MainView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import timber.log.Timber;

import static com.company.schedule.utils.Constants.REQUEST_CODE_ADD_NOTE;
import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;


public class MainActivity extends AppCompatActivity {

    MainFragment mainFragment;
    UpdateNoteFragment updateNoteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
        SharedPrefs sharedPrefs = new SharedPrefs(this);

        if(sharedPrefs.loadNightModeState()) setTheme(R.style.darktheme);  //dark
        else setTheme(R.style.AppTheme);  //white

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar =  findViewById(R.id.toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);

        // init fragment
        mainFragment = new MainFragment();
        updateNoteFragment = new UpdateNoteFragment();
        // open fragment transaction
        showMainFragment();
    }


    public void showMainFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, mainFragment);
//        fragmentTransaction.addToBackStack(null); // feature
        fragmentTransaction.commit();
    }

    public void showUpdateNoteFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, updateNoteFragment);
        fragmentTransaction.addToBackStack(null); // feature
        fragmentTransaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}

