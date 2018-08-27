package com.company.schedule.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presenter.MainPresenter;
import com.company.schedule.ui.activities.MainActivity;

import java.util.GregorianCalendar;
import java.util.List;

//custom adapter class for recyclerview
public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ChangeListener mChangeListener;

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
        holder.checkBoxDone.setChecked(mData.get(position).isDone());
        holder.tvItemNoteName.setText(mData.get(position).getName());  // output name
        holder.tvItemDate.setText(mData.get(position).getDateTimeInFormat());  // output date time depending on local settings.


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
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