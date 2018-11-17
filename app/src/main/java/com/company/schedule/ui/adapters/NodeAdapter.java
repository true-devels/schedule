package com.company.schedule.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.ui.activities.AddNoteActivity;
import com.company.schedule.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.MyViewHolder> {
    private ArrayList<Note> mDataset = new ArrayList<>();
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener{
        // each data item is just a string in this case
        public TextView mTextView;
        public RelativeLayout foreground, delete_layout, save_layout;
        private ItemClickListener itemClickListener;
        ImageButton img_priority;
        public int id;
        public MyViewHolder(View v) {
            super(v);
            foreground = v.findViewById(R.id.foreground);
            delete_layout = v.findViewById(R.id.delete_layout);
            save_layout = v.findViewById(R.id.save_layout);
            mTextView = v.findViewById(R.id.textView2);
            img_priority = v.findViewById(R.id.imageButton4);
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
        holder.mTextView.setText(mDataset.get(position).getName());
        holder.id = mDataset.get(position).getId();
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context,AddNoteActivity.class);
                intent.putExtra("note",mDataset.get(position));
                context.startActivity(intent);
            }
        });
        switch (mDataset.get(position).getPriority()){
            case 2:
                holder.img_priority.setImageResource(R.drawable.button_bg_round_green);
                break;
            case 3:
                holder.img_priority.setImageResource(R.drawable.button_bg_round_red);
                break;
            case 4:
                holder.img_priority.setImageResource(R.drawable.button_bg_round_yellow);
                break;
            default:
                holder.img_priority.setImageResource(R.drawable.button_bg_round);
                break;
        }

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

    public  void setAllNotes(List<Note> dataset){
        mDataset.clear();
        mDataset.addAll(dataset);
        notifyDataSetChanged();
    }

    public ArrayList<Note> getNotes(){
        return mDataset;
    }


}