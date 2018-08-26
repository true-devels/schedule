package com.company.schedule.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import timber.log.Timber;

//class that catches bugs of recyclerview
//these bugs really can`t be fixed in another way
public class CustomLayoutManager extends LinearLayoutManager {
    public CustomLayoutManager(Context context){
        super(context);
    }
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {

            Timber.e(e);  // TODO resolve bugs
        }
    }
}
