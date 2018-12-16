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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
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
import java.util.GregorianCalendar;
import java.util.List;


import timber.log.Timber;

public class MonthlyFragment extends Fragment implements MainView, RecyclerViewItemTouchHelperListener {
    MainActivity mainActivity;
    MainPresenter presenter;
    RelativeLayout mainLayout;
    CalendarView mCalendarView;
    NodeAdapter mAdapter;
    RecyclerView notes_rc;

    private RecyclerView.LayoutManager mLayoutManager;

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

        SharedPrefsRepository sharedPrefs = new SharedPrefsRepository(getContext());
        if(sharedPrefs.isNightMode()) getActivity().setTheme(R.style.darktheme);  //dark
        else getActivity().setTheme(R.style.AppTheme);

        mainActivity = (MainActivity) getActivity();
        View fragmentDaily = inflater.inflate(R.layout.fragment_monthly, container, false);
        mCalendarView =  fragmentDaily.findViewById(R.id.calendarView);

        notes_rc= fragmentDaily.findViewById(R.id.my_recycler_view3);

        mainLayout = fragmentDaily.findViewById(R.id.mainLayout);
        return fragmentDaily;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notes_rc.setHasFixedSize(true);
        //mCalendarView.

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        notes_rc.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        notes_rc.setItemAnimator(new DefaultItemAnimator());
        notes_rc.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        //presenter.refreshMonthlyTasks();
        mAdapter = new NodeAdapter(getContext());
        //mAdapter.onAttachedToRecyclerView(notes_rc);
        //mAdapter.on
        notes_rc.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback callback_left = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(callback_left).attachToRecyclerView(notes_rc);

        ItemTouchHelper.SimpleCallback callback_right = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(callback_right).attachToRecyclerView(notes_rc);
    }

    @Override
    public void onStart() {
        presenter.refreshMonthlyData();
        super.onStart();
    }

    @Override
    public void setAllNotes(List<Note> newNotesList) {


        mAdapter.setAllNotes(newNotesList);

        onGetNotes(newNotesList);
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
            Snackbar snackbar = Snackbar.make(mainLayout,getString(R.string.postponed_action),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction(getString(R.string.undo_action), new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mAdapter.restoreItem(item,deleteIndex);
                    presenter.restoreFromLater(item, 2);
                }
            });
            presenter.swipedToLater(item,2);
        }else{
            Snackbar snackbar = Snackbar.make(mainLayout, getString(R.string.done_action),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction(getString(R.string.undo_action), v -> {
                mAdapter.restoreItem(item,deleteIndex);
                presenter.restoreFromDone(item, 2);
            });
            presenter.swipedToDone(item,2);
        }

    }

    public void onGetNotes(final List<Note> local){
//        setAllNotes(local);

        List<EventDay> events = new ArrayList<>();
        Calendar calendar2 = Calendar.getInstance();
        try {
            mCalendarView.setDate(calendar2);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<local.size();i++){
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(local.get(i).getCalendarDate().getTimeInMillis());
            switch(local.get(i).getPriority()){
                case 2:
                    events.add(
                            new EventDay(
                                    //local.get(i).getCalendarDate(),
                                    cal,
                                    R.drawable.prior2_round));
                    break;
                case 3:
                    events.add(new EventDay(
                            //local.get(i).getCalendarDate(),
                            cal,
                            R.drawable.prior3_round));
                    break;
                case 4:
                    events.add(new EventDay(
                            //local.get(i).getCalendarDate(),
                            cal,
                            R.drawable.prior4_round));
                    break;
                default:
                    events.add(new EventDay(
                            //local.get(i).getCalendarDate(),
                            cal,
                            R.drawable.prior1_round));
                    break;

            }
        }
        //EventDay eventDay = new EventDay().
        mCalendarView.setEvents(events);
        Log.d("check of events",events.size()+" ");
    }
}
