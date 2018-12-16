package com.company.schedule.ui.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presentation.main.MainPresenter;
import com.company.schedule.presentation.main.MainView;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.ui.main.adapters.NodeAdapter;
import com.company.schedule.utils.RecyclerViewItemTouchHelper;
import com.company.schedule.utils.RecyclerViewItemTouchHelperListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import timber.log.Timber;

public class WeeklyFragment extends Fragment implements MainView, RecyclerViewItemTouchHelperListener {

    MainActivity mainActivity;
    MainPresenter presenter;
    RelativeLayout mainLayout;
    NodeAdapter mAdapter;
    RecyclerView notes_rc;
    ArrayList<TextView> tv_numbers = new ArrayList<>(), tv_days = new ArrayList<>();
    ArrayList<LinearLayout> ll_numbers = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    TextView tv_monthyear;
    SharedPrefsRepository sharedPrefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("TAG").d("my message");
        if (presenter == null)  // if presenter isn't created we create it
            presenter = new MainPresenter(this,  // init view in presenter
                    new MainInteractor(  // create interactor
                            new MainRepository(
                                    AppDatabase.getDatabase(getContext()).noteDAO(),
                                    new AppSchedulers()  // for threads
                            )  // create repository and get DAO
                    ),
                    getContext()
            );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        sharedPrefs = new SharedPrefsRepository(getContext());
        if(sharedPrefs.isNightMode()) getActivity().setTheme(R.style.darktheme);  //dark
        else getActivity().setTheme(R.style.AppTheme);
        View fragmentDaily = inflater.inflate(R.layout.fragment_weekly, container, false);
     //   mCalendarView =  fragmentDaily.findViewById(R.id.calendarView);

        notes_rc= fragmentDaily.findViewById(R.id.my_recycler_view2);

        mainLayout = fragmentDaily.findViewById(R.id.mainLayout);
        tv_numbers.add(fragmentDaily.findViewById(R.id.textViewMONnumber));
        tv_numbers.add(fragmentDaily.findViewById(R.id.textViewTUEnumber));
        tv_numbers.add(fragmentDaily.findViewById(R.id.textViewWEDnumber));
        tv_numbers.add(fragmentDaily.findViewById(R.id.textViewTHUnumber));
        tv_numbers.add(fragmentDaily.findViewById(R.id.textViewFRInumber));
        tv_numbers.add(fragmentDaily.findViewById(R.id.textViewSATnumber));
        tv_numbers.add(fragmentDaily.findViewById(R.id.textViewSUNnumber));
        tv_days.add(fragmentDaily.findViewById(R.id.textViewMON));
        tv_days.add(fragmentDaily.findViewById(R.id.textViewTUE));
        tv_days.add(fragmentDaily.findViewById(R.id.textViewWED));
        tv_days.add(fragmentDaily.findViewById(R.id.textViewTHU));
        tv_days.add(fragmentDaily.findViewById(R.id.textViewFRI));
        tv_days.add(fragmentDaily.findViewById(R.id.textViewSAT));
        tv_days.add(fragmentDaily.findViewById(R.id.textViewSUN));
        ll_numbers.add(fragmentDaily.findViewById(R.id.ll_monday));
        ll_numbers.add(fragmentDaily.findViewById(R.id.ll_tuesday));
        ll_numbers.add(fragmentDaily.findViewById(R.id.ll_wednesday));
        ll_numbers.add(fragmentDaily.findViewById(R.id.ll_thursday));
        ll_numbers.add(fragmentDaily.findViewById(R.id.ll_friday));
        ll_numbers.add(fragmentDaily.findViewById(R.id.ll_saturday));
        ll_numbers.add(fragmentDaily.findViewById(R.id.ll_sunday));

        tv_monthyear = fragmentDaily.findViewById(R.id.month_year);
        return fragmentDaily;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notes_rc.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        notes_rc.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        notes_rc.setItemAnimator(new DefaultItemAnimator());
        notes_rc.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        //presenter.refreshWeeklyTasks();
        mAdapter = new NodeAdapter(getContext());

        notes_rc.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback callback_left = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(callback_left).attachToRecyclerView(notes_rc);

        ItemTouchHelper.SimpleCallback callback_right = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(callback_right).attachToRecyclerView(notes_rc);

        Calendar cal =Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(new Date());
        int day;
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            day=7;
        }else{
            day=cal.get(Calendar.DAY_OF_WEEK);
            day--;
        }
        ll_numbers.get(--day).setBackgroundResource(R.drawable.prior_white_now_week);
        if(sharedPrefs.isNightMode()){
            tv_numbers.get(day).setTextColor(R.attr.blackText);
            tv_days.get(day).setTextColor(R.attr.blackText);
        }

        for(int i=0; i<=6;i++){
            Calendar local = new GregorianCalendar();
            local.set(Calendar.DAY_OF_WEEK,i+2);

            tv_numbers.get(i).setText(Integer.toString(local.get(Calendar.DAY_OF_MONTH)));


            //tv_numbers.get(i).setTextColor();
        }
        Calendar now = new GregorianCalendar();
        tv_monthyear.setText(getResources().getStringArray(R.array.monthes_short)[now.get(Calendar.MONTH)]+", " + now.get(Calendar.YEAR)); // new Date() return current time
    }

    @Override
    public void onStart() {
        presenter.refreshWeeklyData();
        super.onStart();
    }

    @Override
    public void setAllNotes(List<Note> newNotes) {
        mAdapter.setAllNotes(newNotes);
    }

    @Override
    public void showMessage(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageLong(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        int deleteIndex = viewHolder.getAdapterPosition ();
        Note item = mAdapter.removeItem(deleteIndex);
        if(direction == ItemTouchHelper.LEFT){
            Snackbar snackbar = Snackbar.make(mainLayout,getString(R.string.postponed_action)  + ((NodeAdapter.MyViewHolder) viewHolder).getText(),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction(getString(R.string.undo_action), new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mAdapter.restoreItem(item,deleteIndex);
                    presenter.restoreFromLater(item, 1);
                }
            });
            int id = ((NodeAdapter.MyViewHolder) viewHolder).id;
            presenter.swipedToLater(item,1);
            Log.d("id_check ",Integer.toString(id));
        }else{
            Snackbar snackbar = Snackbar.make(mainLayout, getString(R.string.done_action) + ((NodeAdapter.MyViewHolder) viewHolder).getText(),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction(getString(R.string.undo_action), v -> {
                mAdapter.restoreItem(item,deleteIndex);
                presenter.restoreFromDone(item, 1);
            });
            presenter.swipedToDone(item,1);
        }

    }
}
