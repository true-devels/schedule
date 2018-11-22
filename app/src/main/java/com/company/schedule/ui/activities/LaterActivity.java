package com.company.schedule.ui.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.LaterInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presentation.later.LaterPresenter;
import com.company.schedule.presentation.later.LaterView;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.ui.main.adapters.NodeAdapter;
import com.company.schedule.utils.RecyclerViewItemTouchHelper;
import com.company.schedule.utils.RecyclerViewItemTouchHelperListener;

import java.util.List;

public class LaterActivity extends AppCompatActivity implements LaterView, RecyclerViewItemTouchHelperListener {
    TextView tv_title;
    RelativeLayout layout_day, layout_week, layout_month, mainlayout;
    RecyclerView rc_day, rc_week, rc_month;
    LaterPresenter presenter;
    NodeAdapter adapter_day, adapter_week, adapter_month;
    ImageButton btn_right, btn_left;
    ImageView backward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefsRepository sharedPrefs = new SharedPrefsRepository(this);
        if(sharedPrefs.isNightMode()) setTheme(R.style.darktheme);  //dark
        else setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_later);

        if (presenter == null)  // if presenter isn't created we create it
            presenter = new LaterPresenter(this,  // init view in presenter
                    new LaterInteractor(  // create interactor
                            new MainRepository(
                                    AppDatabase.getDatabase(this).noteDAO(),
                                    new AppSchedulers()  // for threads
                            )  // create repository and get DAO
                    )
            );
        mainlayout = findViewById(R.id.mainLayout);
        layout_day = findViewById(R.id.today_later_layout);
        layout_week = findViewById(R.id.week_later_layout);
        layout_month = findViewById(R.id.month_later_layout);
        tv_title = findViewById(R.id.tv_title);
        backward = findViewById(R.id.backward_btn);
        backward.setVisibility(View.VISIBLE);
        btn_right = findViewById(R.id.btnToolbarRight);
        btn_right.setVisibility(View.GONE);

        backward.setOnClickListener(v -> {
            Intent intent = new Intent(LaterActivity.this,MainActivity.class);
            startActivity(intent);
        });

        btn_left = findViewById(R.id.btnLeftToolbar);
        btn_left.setVisibility(View.GONE);

        rc_day = findViewById(R.id.rc_today);
        rc_week = findViewById(R.id.rc_week);
        rc_month = findViewById(R.id.rc_month);

        rc_day.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rc_day.setLayoutManager(mLayoutManager);
        rc_day.setItemAnimator(new DefaultItemAnimator());
        rc_day.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter_day = new NodeAdapter(this);
        rc_day.setAdapter(adapter_day);

        rc_week.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        rc_week.setLayoutManager(mLayoutManager2);
        rc_week.setItemAnimator(new DefaultItemAnimator());
        rc_week.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter_week = new NodeAdapter(this);
        rc_week.setAdapter(adapter_week);

        rc_month.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(this);
        rc_month.setLayoutManager(mLayoutManager3);
        rc_month.setItemAnimator(new DefaultItemAnimator());
        rc_month.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter_month = new NodeAdapter(this);
        rc_month.setAdapter(adapter_month);


        if(getIntent().getIntExtra("role",1)==1){
            presenter.loadDataLater();
            ItemTouchHelper.SimpleCallback callback_right = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
            new ItemTouchHelper(callback_right).attachToRecyclerView(rc_day);
            ItemTouchHelper.SimpleCallback callback_rightw = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
            new ItemTouchHelper(callback_rightw).attachToRecyclerView(rc_week);
            ItemTouchHelper.SimpleCallback callback_right2 = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
            new ItemTouchHelper(callback_right2).attachToRecyclerView(rc_month);
        }else{
            presenter.loadDataDone();
            tv_title.setText("Done");
        }


    }

    @Override
    public void setTodayNotes(List<Note> newNotes) {
        if(newNotes.size()!=0){
           adapter_day.setAllNotes(newNotes);
        }else{
            layout_day.setVisibility(View.GONE);
        }
    }

    @Override
    public void setWeekNotes(List<Note> newNotes) {
        if(newNotes.size()!=0){
            adapter_week.setAllNotes(newNotes);
        }else{
            layout_week.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMonthNotes(List<Note> newNotes) {
        if(newNotes.size()!=0){
            adapter_month.setAllNotes(newNotes);
        }else{
            layout_month.setVisibility(View.GONE);
        }
    }
    Note item = null;
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        int deleteIndex = viewHolder.getAdapterPosition ();

        switch (((NodeAdapter.MyViewHolder)viewHolder).freq){
            case 1:
                item = adapter_day.removeItem(deleteIndex);
                break;
            case 2:
                item = adapter_week.removeItem(deleteIndex);
                break;
            case 3:
                item = adapter_month.removeItem(deleteIndex);
                break;
        }


        Snackbar snackbar = Snackbar.make(mainlayout,"Done " + ((NodeAdapter.MyViewHolder) viewHolder).mTextView.getText(),Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setAction("UNDO", v -> {
            switch (((NodeAdapter.MyViewHolder)viewHolder).freq){
                case 1:
                     adapter_day.restoreItem(item,deleteIndex);
                    break;
                case 2:
                     adapter_week.restoreItem(item,deleteIndex);
                    break;
                case 3:
                     adapter_month.restoreItem(item,deleteIndex);
                    break;
            }
            presenter.restoreFromDone(item);
        });
        presenter.swipedToDone(item);
    }
}
