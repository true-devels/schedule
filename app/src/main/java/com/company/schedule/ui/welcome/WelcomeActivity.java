package com.company.schedule.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private SharedPrefsRepository prefManager;
    private Spinner localiztion;
    private SpinnerAdapter spinnerAdapter;
    private boolean isUserInteracting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch
        prefManager = new SharedPrefsRepository(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }




        setContentView(R.layout.activity_welcome);

        //init xml elements
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);
        localiztion = findViewById(R.id.spinnerLang);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);



        int[] icons = { R.drawable.ua, R.drawable.ru, R.drawable.en };
        String[] names = { "UA", "RU", "EN"};
        spinnerAdapter = new SpinnerAdapter(this, names, icons);
        localiztion.setAdapter(spinnerAdapter);



        localiztion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            prefManager.setLocalization(Constants.LOCALIZATION_UA);
                            recreate();
                            break;
                        case 1:
                            conf.setLocale(new Locale("ru"));
                            res.updateConfiguration(conf, dm);
                            prefManager.setLocalization(Constants.LOCALIZATION_RU);
                            recreate();
                            break;
                        case 2:
                            conf.setLocale(new Locale("en"));
                            prefManager.setLocalization(Constants.LOCALIZATION_EN);
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

        //init VP adapter
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        // change listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.length - 1) {
                    // last page. make button text to GOT IT
                    btnNext.setText(getString(R.string.start));
                    btnSkip.setVisibility(View.GONE);
                } else {
                    // still pages are left
                    btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });

        //set onClick
        btnSkip.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                // checking for last page
                // if last page home screen will be launched
                int current = getItem();
                if (current  < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
                break;

            case R.id.btn_skip:
                //launch MainActivity
                launchHomeScreen();
                break;
        }
    }




    //circles in the bottom
    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem() {
        return viewPager.getCurrentItem() + 1;
    }

    //launch MainActivity
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunchFalse();
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracting = true;
    }

    //View pager adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }




}