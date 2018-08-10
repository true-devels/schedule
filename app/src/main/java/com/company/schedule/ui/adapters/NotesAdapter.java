package com.company.schedule.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.database.Note;
import com.company.schedule.utils.DateFormat;

import java.util.GregorianCalendar;
import java.util.List;

//custom adapter class for recyclerview
public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    final String TAG = "myLog MainActivity";

    // data is passed into the constructor
    public NotesAdapter(Context context, List<Note> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);  // find example item element
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvItemNoteName.setText(mData.get(position).getName());

        GregorianCalendar localDateOutput = mData.get(position).getDate();
        if (localDateOutput != null){
            holder.tvItemDate.setText(DateFormat.getDateTime(localDateOutput));  // output date time depending on local settings.
            // We need write .getTime() to convert GregorianCalendar to Date
        } else {
            holder.tvItemDate.setText("");  // don't delete it
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

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}