package com.company.schedule.ui.statistics;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.company.schedule.R;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.ui.later.LaterActivity;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.ui.settings.SettingsActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
        SharedPrefsRepository sharedPrefsRepository = new SharedPrefsRepository(this);

        //set theme
        if(sharedPrefsRepository.isNightMode()) setTheme(R.style.darktheme);
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);


        GraphView graph =  findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getPoints(sharedPrefsRepository.getStatistics()));


        graph.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return getWeek()[(int)value];
                } else {
                    return Double.toString(value);
                }
            }

            @Override
            public void setViewport(Viewport viewport) {
                viewport.setXAxisBoundsManual(true);
             viewport.setMinX(0);
             viewport.setMaxX(6);
            }
        });
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.addSeries(series);

        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
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
                        case R.id.nav_home:
                            Intent intent4 = new Intent(this,MainActivity.class);
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
        ImageButton btn_right = findViewById(R.id.btnToolbarRight);
        btn_right.setVisibility(View.GONE);
        ImageButton imgbtn = findViewById(R.id.btnLeftToolbar);
        imgbtn.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

    }

    private String[] getWeek(){
        GregorianCalendar cal = new GregorianCalendar();
        switch (cal.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                return new String[]{
                        getString(R.string.tuesdayShort),
                        getString(R.string.wednesdayShort),
                        getString(R.string.thursdayShort),
                        getString(R.string.fridayShort),
                        getString(R.string.saturdayShort),
                        getString(R.string.sundayShort),
                        getString(R.string.mondayShort),
                };

            case Calendar.TUESDAY:
                return new String[]{
                        getString(R.string.wednesdayShort),
                        getString(R.string.thursdayShort),
                        getString(R.string.fridayShort),
                        getString(R.string.saturdayShort),
                        getString(R.string.sundayShort),
                        getString(R.string.mondayShort),
                        getString(R.string.tuesdayShort),
                };
            case Calendar.WEDNESDAY:
                return new String[]{
                        getString(R.string.thursdayShort),
                        getString(R.string.fridayShort),
                        getString(R.string.saturdayShort),
                        getString(R.string.sundayShort),
                        getString(R.string.mondayShort),
                        getString(R.string.tuesdayShort),
                        getString(R.string.wednesdayShort),
                };
            case Calendar.THURSDAY:
                return new String[]{
                        getString(R.string.fridayShort),
                        getString(R.string.saturdayShort),
                        getString(R.string.sundayShort),
                        getString(R.string.mondayShort),
                        getString(R.string.tuesdayShort),
                        getString(R.string.wednesdayShort),
                        getString(R.string.thursdayShort),
                };
            case Calendar.FRIDAY:
                return new String[]{
                        getString(R.string.saturdayShort),
                        getString(R.string.sundayShort),
                        getString(R.string.mondayShort),
                        getString(R.string.tuesdayShort),
                        getString(R.string.wednesdayShort),
                        getString(R.string.thursdayShort),
                        getString(R.string.fridayShort),
                };
            case Calendar.SATURDAY:
                return new String[]{
                        getString(R.string.sundayShort),
                        getString(R.string.mondayShort),
                        getString(R.string.tuesdayShort),
                        getString(R.string.wednesdayShort),
                        getString(R.string.thursdayShort),
                        getString(R.string.fridayShort),
                        getString(R.string.saturdayShort),
                };
            case Calendar.SUNDAY:
                return new String[]{

                        getString(R.string.mondayShort),
                        getString(R.string.tuesdayShort),
                        getString(R.string.wednesdayShort),
                        getString(R.string.thursdayShort),
                        getString(R.string.fridayShort),
                        getString(R.string.saturdayShort),
                        getString(R.string.sundayShort),
                };
            default:
                return  null;
        }
    }
    private DataPoint[] getPoints(ArrayList<String> statistics){
        GregorianCalendar cal = new GregorianCalendar();

        switch (cal.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                return new DataPoint[]{
                        new DataPoint(0,Integer.valueOf(statistics.get(1))),
                        new DataPoint(1,Integer.valueOf(statistics.get(2))),
                        new DataPoint(2,Integer.valueOf(statistics.get(3))),
                        new DataPoint(3,Integer.valueOf(statistics.get(4))),
                        new DataPoint(4,Integer.valueOf(statistics.get(5))),
                        new DataPoint(5,Integer.valueOf(statistics.get(6))),
                        new DataPoint(6,Integer.valueOf(statistics.get(0))),
                };

            case Calendar.TUESDAY:
                return new DataPoint[]{
                        new DataPoint(0,Integer.valueOf(statistics.get(2))),
                        new DataPoint(1,Integer.valueOf(statistics.get(3))),
                        new DataPoint(2,Integer.valueOf(statistics.get(4))),
                        new DataPoint(3,Integer.valueOf(statistics.get(5))),
                        new DataPoint(4,Integer.valueOf(statistics.get(6))),
                        new DataPoint(5,Integer.valueOf(statistics.get(0))),
                        new DataPoint(6,Integer.valueOf(statistics.get(1))),
                };

            case Calendar.WEDNESDAY:
                return new DataPoint[]{
                        new DataPoint(0,Integer.valueOf(statistics.get(3))),
                        new DataPoint(1,Integer.valueOf(statistics.get(4))),
                        new DataPoint(2,Integer.valueOf(statistics.get(5))),
                        new DataPoint(3,Integer.valueOf(statistics.get(6))),
                        new DataPoint(4,Integer.valueOf(statistics.get(0))),
                        new DataPoint(5,Integer.valueOf(statistics.get(1))),
                        new DataPoint(6,Integer.valueOf(statistics.get(2))),
                };
            case Calendar.THURSDAY:
                return new DataPoint[]{
                        new DataPoint(0,Integer.valueOf(statistics.get(4))),
                        new DataPoint(1,Integer.valueOf(statistics.get(5))),
                        new DataPoint(2,Integer.valueOf(statistics.get(6))),
                        new DataPoint(3,Integer.valueOf(statistics.get(0))),
                        new DataPoint(4,Integer.valueOf(statistics.get(1))),
                        new DataPoint(5,Integer.valueOf(statistics.get(2))),
                        new DataPoint(6,Integer.valueOf(statistics.get(3))),
                };
            case Calendar.FRIDAY:
                return new DataPoint[]{
                        new DataPoint(0,Integer.valueOf(statistics.get(5))),
                        new DataPoint(1,Integer.valueOf(statistics.get(6))),
                        new DataPoint(2,Integer.valueOf(statistics.get(0))),
                        new DataPoint(3,Integer.valueOf(statistics.get(1))),
                        new DataPoint(4,Integer.valueOf(statistics.get(2))),
                        new DataPoint(5,Integer.valueOf(statistics.get(3))),
                        new DataPoint(6,Integer.valueOf(statistics.get(4))),
                };
            case Calendar.SATURDAY:
                return new DataPoint[]{
                        new DataPoint(0,Integer.valueOf(statistics.get(6))),
                        new DataPoint(1,Integer.valueOf(statistics.get(0))),
                        new DataPoint(2,Integer.valueOf(statistics.get(1))),
                        new DataPoint(3,Integer.valueOf(statistics.get(2))),
                        new DataPoint(4,Integer.valueOf(statistics.get(3))),
                        new DataPoint(5,Integer.valueOf(statistics.get(4))),
                        new DataPoint(6,Integer.valueOf(statistics.get(5))),
                };
            case Calendar.SUNDAY:
                return new DataPoint[]{
                        new DataPoint(0,Integer.valueOf(statistics.get(0))),
                        new DataPoint(1,Integer.valueOf(statistics.get(1))),
                        new DataPoint(2,Integer.valueOf(statistics.get(2))),
                        new DataPoint(3,Integer.valueOf(statistics.get(3))),
                        new DataPoint(4,Integer.valueOf(statistics.get(4))),
                        new DataPoint(5,Integer.valueOf(statistics.get(5))),
                        new DataPoint(6,Integer.valueOf(statistics.get(6))),
                };
            default:
                return  null;
        }
    }
}
