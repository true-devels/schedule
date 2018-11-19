package com.company.schedule.utils;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.company.schedule.ui.main.NodeAdapter;

public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private  RecyclerViewItemTouchHelperListener listener;
    private int direction;

    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, RecyclerViewItemTouchHelperListener listener){
        super(dragDirs,swipeDirs);
        this.listener = listener;
        this.direction = swipeDirs;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(listener!=null)listener.onSwiped(viewHolder,i,viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        View foreground = ((NodeAdapter.MyViewHolder)viewHolder).foreground;
        getDefaultUIUtil().clearView(foreground);
        if(direction==ItemTouchHelper.LEFT){
            View save_layout = ((NodeAdapter.MyViewHolder)viewHolder).save_layout;
            getDefaultUIUtil().clearView(save_layout);
        }else{
            View delete_layout = ((NodeAdapter.MyViewHolder)viewHolder).delete_layout;
            getDefaultUIUtil().clearView(delete_layout);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            if(viewHolder!=null){
                View foreground = ((NodeAdapter.MyViewHolder)viewHolder).foreground;
                getDefaultUIUtil().onSelected(foreground);
                if(direction==ItemTouchHelper.LEFT){
                    View save_layout = ((NodeAdapter.MyViewHolder)viewHolder).save_layout;
                    getDefaultUIUtil().onSelected(save_layout);
                }else{
                    View delete_layout = ((NodeAdapter.MyViewHolder)viewHolder).delete_layout;
                    getDefaultUIUtil().onSelected(delete_layout);
                }
            }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreground = ((NodeAdapter.MyViewHolder)viewHolder).foreground;
        getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
        if(direction==ItemTouchHelper.LEFT){
            View save_layout = ((NodeAdapter.MyViewHolder)viewHolder).save_layout;
            getDefaultUIUtil().onDraw(c,recyclerView,save_layout,dX,dY,actionState,isCurrentlyActive);
        }else{
            View delete_layout = ((NodeAdapter.MyViewHolder)viewHolder).delete_layout;
            getDefaultUIUtil().onDraw(c,recyclerView,delete_layout,dX,dY,actionState,isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreground = ((NodeAdapter.MyViewHolder)viewHolder).foreground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
        if(direction==ItemTouchHelper.LEFT){
            View save_layout = ((NodeAdapter.MyViewHolder)viewHolder).save_layout;
            getDefaultUIUtil().onDrawOver(c,recyclerView,save_layout,dX,dY,actionState,isCurrentlyActive);
        }else{
            View delete_layout = ((NodeAdapter.MyViewHolder)viewHolder).delete_layout;
            getDefaultUIUtil().onDrawOver(c,recyclerView,delete_layout,dX,dY,actionState,isCurrentlyActive);
        }

    }

}
