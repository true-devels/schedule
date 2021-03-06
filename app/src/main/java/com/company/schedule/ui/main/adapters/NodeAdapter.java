package com.company.schedule.ui.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.ui.oneNote.OneNoteActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.company.schedule.utils.Constants.FREQUENCY_DAILY;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.MyViewHolder> {
    private ArrayList<Note> mDataset = new ArrayList<>();
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener{
        // each data item is just a string in this case
        private TextView mTextView, tv_time, tv_date, tv_category;
        public RelativeLayout foreground, delete_layout, save_layout;
        private ItemClickListener itemClickListener;
        ImageButton img_priority;
        public int id, freq;
        MyViewHolder(View v) {
            super(v);
            foreground = v.findViewById(R.id.foreground);
            delete_layout = v.findViewById(R.id.delete_layout);
            save_layout = v.findViewById(R.id.save_layout);
            mTextView = v.findViewById(R.id.textView2);
            img_priority = v.findViewById(R.id.imageButton4);
            tv_time =  v.findViewById(R.id.textViewTime);
            tv_date = v.findViewById(R.id.textViewDateOneNode);
            tv_category = v.findViewById(R.id.tv_category);
           // mTextView = v;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
        private void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        public String getText(){
            return mTextView.getText().toString();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NodeAdapter(Context context
    ) {
        this.context =context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NodeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_node, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        //v.override
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.freq = (int) mDataset.get(position).getFrequency();
        holder.mTextView.setText(mDataset.get(position).getName());
        holder.id = mDataset.get(position).getId();
        holder.setItemClickListener((view, position1, isLongClick) -> {
            Intent intent = new Intent(context, OneNoteActivity.class);
            intent.putExtra("note", mDataset.get(position1));
            context.startActivity(intent);
        });
        switch (mDataset.get(position).getPriority()) {
            case 2:
                holder.img_priority.setImageResource(R.drawable.prior2_round);
                break;
            case 3:
                holder.img_priority.setImageResource(R.drawable.prior3_round);
                break;
            case 4:
                holder.img_priority.setImageResource(R.drawable.prior4_round);
                break;
            default:
                holder.img_priority.setImageResource(R.drawable.prior1_round);
                break;
        }
        String time = mDataset.get(position).getTimeInFormat();
        holder.tv_time.setText(time);
        Log.d("check dates", mDataset.get(position).getDateTimeInFormat());
        if (mDataset.get(position).getFrequency() != FREQUENCY_DAILY) {
            holder.tv_date.setText(mDataset.get(position).getCalendarDate().get(Calendar.DAY_OF_MONTH) + ", " + context.getResources().getStringArray(R.array.monthes_short)[mDataset.get(position).getCalendarDate().get(Calendar.MONTH)]);
        } else {
            //holder.tv_time.setGravity(View.TEXT_ALIGNMENT_CENTER);
            holder.tv_date.setText("");
            holder.tv_date.setVisibility(View.GONE);
        }
        if (mDataset.get(position).getCategory().isEmpty()) {
            holder.tv_category.setText("");
            holder.tv_category.setVisibility(View.GONE);
        } else holder.tv_category.setText(mDataset.get(position).getCategory());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Note removeItem(int position){
        Note nod = mDataset.get(position);
        mDataset.remove(position);
        notifyDataSetChanged();
        return nod;
    }

    public void restoreItem(Note not,int position){
        mDataset.add(position,not);
        notifyDataSetChanged();
    }

    public  void setAllNotes(List<Note> dataset) {
        mDataset.clear();
        mDataset.addAll(dataset);
        notifyDataSetChanged();
    }
}