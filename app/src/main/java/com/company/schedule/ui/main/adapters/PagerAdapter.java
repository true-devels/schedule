package com.company.schedule.ui.main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.company.schedule.ui.main.fragments.DailyFragment;
import com.company.schedule.ui.main.fragments.MonthlyFragment;
import com.company.schedule.ui.main.fragments.WeeklyFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment ret = null;
        switch (position) {
            case 0:
               // DailyFragment tab1 = ;
                ret = new DailyFragment();
                break;
            case 1:
              //  TabFragment2 tab2 = new TabFragment2();
               // return tab2;
                ret = new WeeklyFragment();
                break;
            case 2:
                //TabFragment3 tab3 = new TabFragment3();
                //return tab3;
                ret = new MonthlyFragment();
                break;
        }
        return ret;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
