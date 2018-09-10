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

    private ItemClickListener mClickListener;
    private ChangeListener mChangeListener;
    //method that writes all data to recyclerview

    public void setAllNotes(List<Note> newNotes) {
        listNotes.clear();
        notifyItemRangeRemoved(0, getItemCount());
        listNotes.addAll(newNotes);
        notifyItemRangeInserted(0,newNotes.size());
    }
    // inflates the row layout from xml when needed

    public Note getNoteByPosition(int position) {
        return listNotes.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);  // find example item element
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {holder.checkBoxDone.setChecked(listNotes.get(position).isDone());
        holder.tvItemNoteName.setText(listNotes.get(position).getName());  // output name
        holder.tvItemDate.setText(listNotes.get(position).getDateTimeInFormat());  // output date time depending on local settings.
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return listNotes.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
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
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (mChangeListener != null) mChangeListener.onChangedBox(compoundButton, getAdapterPosition(), b);
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

    // allows clicks oncheckbox be caught
    public void setChangeListener(ChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ChangeListener {
        void onChangedBox(CompoundButton compoundButton, int position, boolean done);
    }


}