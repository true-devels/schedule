package com.company.schedule.ui.welcome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.schedule.R;

public class SpinnerAdapter extends ArrayAdapter {
    private String[] spinnerTitles;
    private int[] spinnerImages;
    private Context mContext;

    public SpinnerAdapter( Context context, String[] titles, int[] images) {
        super(context, R.layout.one_row_spinner);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.one_row_spinner, parent, false);
            mViewHolder.mFlag =  convertView.findViewById(R.id.imageViewFlag);
            mViewHolder.mName =  convertView.findViewById(R.id.textViewTitle);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerTitles[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    
    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
    }


}
