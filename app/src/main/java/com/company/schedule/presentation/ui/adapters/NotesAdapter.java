package com.company.schedule.presentation.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.model.data.base.Note;

import java.util.ArrayList;
import java.util.List;

//custom adapter class for recyclerview
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> listNotes = new ArrayList<>();

    private MyItemClickListener mClickListener;
    private MyOnCheckedChangeListener mCheckedChangeListener;

        //method that writes all data to recyclerview
    public void setAllNotes(List<Note> newNotes) {
        listNotes.clear();
        notifyItemRangeRemoved(0, getItemCount());
        listNotes.addAll(newNotes);
        notifyItemRangeInserted(0,newNotes.size());
    }
    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);  // find example item element
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note currentNote = listNotes.get(position);
        holder.tvItemNoteName.setText(currentNote.getName());  // output name
        holder.tvItemDate.setText(currentNote.getDateTimeInFormat());  // output date time depending on local settings.
        holder.checkBoxDone.setChecked(currentNote.isDone());
//        holder.checkBoxDone.setSelected(currentNote.isDone());  // set check box isDone
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return listNotes.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        CheckBox checkBoxDone;
        TextView tvItemNoteName, tvItemDate;

        ViewHolder(View itemView) {
            super(itemView);
            // initialize TextViews and checkbox
            itemView.findViewById(R.id.tvItemNoteName).setSelected(true);  // it is necessary for forever scroll text(when it is very long)
            tvItemNoteName = itemView.findViewById(R.id.tvItemNoteName);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);
            checkBoxDone = itemView.findViewById(R.id.checkBox_done);
            itemView.setOnClickListener(this);
            checkBoxDone.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(listNotes.get(getAdapterPosition()));
//            else
                // TODO throw error or make init listener in constructor
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mCheckedChangeListener != null)
                mCheckedChangeListener.onCheckedChange(listNotes.get(getAdapterPosition()), isChecked);
//            else
            //TODO throw error or make init listener in constructor
        }
    }

    // allows clicks events to be caught
    public void setClickListener(MyItemClickListener myItemClickListener) {
        this.mClickListener = myItemClickListener;
    }

    public void setOnCheckedChangeListener(MyOnCheckedChangeListener checkedChangeListener) {
        this.mCheckedChangeListener = checkedChangeListener;
    }

    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        void onItemClick(Note noteClickedOn);
    }

    public interface MyOnCheckedChangeListener {
        void onCheckedChange(Note noteCheckedOn, boolean isChecked);
    }
}