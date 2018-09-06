package com.company.schedule.presentation.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.model.data.base.Note;

import java.util.ArrayList;
import java.util.List;

//custom adapter class for recyclerview
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    public List<Note> listNotes = new ArrayList<>();
    private ItemClickListener mClickListener;

    //method that writes all data to recyclerview
    public void setAllNotes(List<Note> newNotes) {
        listNotes.clear();
        notifyItemRangeRemoved(0, getItemCount());
        listNotes.addAll(newNotes);
        notifyItemRangeInserted(0,newNotes.size());
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);  // find example item element
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvItemNoteName.setText(listNotes.get(position).getName());  // output name
        holder.tvItemDate.setText(listNotes.get(position).getDateTimeInFormat());  // output date time depending on local settings.
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return listNotes.size();
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