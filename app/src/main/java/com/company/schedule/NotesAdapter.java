package com.company.schedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.schedule.Local.DateConverter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<CustomNotify> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    final String TAG = "myLog MainActivity";

    // data is passed into the constructor
    NotesAdapter(Context context, List<CustomNotify> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mData.get(position).getDate();
        holder.tvItemNoteName.setText(mData.get(position).getName());
        String dateToShow = DateConverter.toString(mData.get(position).getDate());
        if (dateToShow != null) {
            holder.tvItemDate.setText(dateToShow);
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView tvItemNoteName, tvItemDate;

        ViewHolder(View itemView) {
            super(itemView);
            // initialize TextViews
            itemView.findViewById(R.id.tvItemNoteName).setSelected(true);  // it is necessary for forever scroll text(when it is very long)
            tvItemNoteName = itemView.findViewById(R.id.tvItemNoteName);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).getName();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}