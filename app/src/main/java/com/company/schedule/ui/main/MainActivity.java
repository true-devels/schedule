package com.company.schedule.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;

import com.company.schedule.R;
import com.company.schedule.ui.addNote.AddNoteActivity;
import com.company.schedule.ui.later.LaterActivity;
import com.company.schedule.ui.main.adapters.PagerAdapter;
import com.company.schedule.ui.main.fragments.DailyFragment;
import com.company.schedule.ui.settings.SettingsActivity;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.ui.statistics.StatisticActivity;
import com.company.schedule.utils.Constants;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
        SharedPrefsRepository sharedPrefs = new SharedPrefsRepository(this);
       // SharedPrefs sharedPrefs = new SharedPrefs(this);

        if(sharedPrefs.isNightMode()) setTheme(R.style.darktheme);  //dark
        else setTheme(R.style.AppTheme);  //white*/

        GregorianCalendar gc_now = new GregorianCalendar();
        GregorianCalendar gc_last = new GregorianCalendar();
        gc_last.setTimeInMillis(sharedPrefs.getTimeLastUpdateStat());
        if(gc_now.get(Calendar.DAY_OF_YEAR)!=gc_last.get(Calendar.DAY_OF_YEAR)){
            sharedPrefs.setTimeLastUpdateStat(gc_now.getTimeInMillis());
            try{
            sharedPrefs.resetStatistics(gc_now.get(Calendar.DAY_OF_WEEK));
            }catch (IndexOutOfBoundsException ie){
                Log.e("test",sharedPrefs.getStatistics().toString());
            }
        }

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        /*
        Bundle bundle = new Bundle();
        bundle.putPa("edttext", "From Activity");
        DailyFragment fragobj = new DailyFragment();
        fragobj.setArguments(bundle);*/

        switch (sharedPrefs.getLocalization()){

            case Constants.LOCALIZATION_EN:
                conf.setLocale(new Locale("en"));
                res.updateConfiguration(conf, dm);
                break;
            case Constants.LOCALIZATION_RU:
                conf.setLocale(new Locale("ru"));
                res.updateConfiguration(conf, dm);
                break;
            case Constants.LOCALIZATION_UA:
                conf.setLocale(new Locale("uk"));
                res.updateConfiguration(conf, dm);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init view components
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    switch (menuItem.getItemId()){
                        case R.id.nav_later:
                            Intent intent= new Intent(this,LaterActivity.class);
                            intent.putExtra("role",1);
                            startActivity(intent);

                            break;
                        case R.id.nav_done:
                            Intent intent2 = new Intent(this,LaterActivity.class);
                            intent2.putExtra("role",2);
                            startActivity(intent2);
                            break;
                        case R.id.nav_settings:
                            Intent intent3 = new Intent(this,SettingsActivity.class);
                            startActivity(intent3);
                            break;
                        case R.id.nav_statistic:
                            Intent intent4 = new Intent(this,StatisticActivity.class);
                            startActivity(intent4);
                    }
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    return true;
                });

        final Toolbar toolbar =  findViewById(R.id.my_toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);

        TabLayout tb  = findViewById(R.id.tabs);
        final ViewPager viewPager =  findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tb.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));
        tb.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ImageButton imgbtn2 = findViewById(R.id.btnToolbarRight);
        imgbtn2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
            intent.putExtra("tab",tb.getSelectedTabPosition());
            startActivity(intent);
        });

        ImageButton imgbtn = findViewById(R.id.btnLeftToolbar);
        imgbtn.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

    }
}

