package com.company.schedule.ui.main.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.strictmode.ResourceMismatchViolation;
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
import com.company.schedule.utils.LocalFormat;
import com.company.schedule.utils.RecyclerViewItemTouchHelper;
import com.company.schedule.utils.RecyclerViewItemTouchHelperListener;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class DailyFragment extends Fragment implements MainView, RecyclerViewItemTouchHelperListener {
    MainActivity mainActivity;
    RecyclerView notes_rc;
    NodeAdapter adapter;
    MainPresenter presenter;
    RelativeLayout mainLayout;
    TextView tvDateToday, tvCompletedTasks, tvAllTasks;
    private RecyclerView.LayoutManager mLayoutManager;


    //  ================_LIFECYCLE_START_================
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
        View fragmentDaily = inflater.inflate(R.layout.fragment_daily, container, false);
        notes_rc= fragmentDaily.findViewById(R.id.my_recycler_view);
        mainLayout = fragmentDaily.findViewById(R.id.mainLayout);
        tvDateToday = fragmentDaily.findViewById(R.id.tvDateToday);
        tvCompletedTasks = fragmentDaily.findViewById(R.id.textViewCompleted);
        tvAllTasks = fragmentDaily.findViewById(R.id.textViewAll);
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
        //presenter.refreshDailyTasks(); load data was moved to onStart
        //presenter.getAllNotes();
        adapter = new NodeAdapter(getContext());

        notes_rc.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback callback_left = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(callback_left).attachToRecyclerView(notes_rc);

        ItemTouchHelper.SimpleCallback callback_right = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(callback_right).attachToRecyclerView(notes_rc);



        Calendar today = new GregorianCalendar();

        Resources res = getResources();
        String[] monthes = res.getStringArray(R.array.monthes_short);


        tvDateToday.setText( today.get(Calendar.DAY_OF_MONTH)+ " " +monthes[(today.get(Calendar.MONTH))]+" "+today.get(Calendar.YEAR));

    }

    @Override
    public void onStart() {
        presenter.refreshDailyData();
//        presenter.checkDoneNote();  // check done Note call in onComplete of refreshDaily
        super.onStart();
    }


    //  ================_VIEW_IMPLEMENTATION_================
    @Override
    public void setAllNotes(List<Note> newNotes) {
        adapter.setAllNotes(newNotes);
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
        Note item = adapter.removeItem(deleteIndex);
        if(direction == ItemTouchHelper.LEFT){
            Snackbar snackbar = Snackbar.make(mainLayout,getString(R.string.postponed_action) + ((NodeAdapter.MyViewHolder) viewHolder).getText(),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction(getString(R.string.undo_action), v -> {
                adapter.restoreItem(item,deleteIndex);
                presenter.restoreFromLater(item, 0);
            });
            int id = ((NodeAdapter.MyViewHolder) viewHolder).id;
            presenter.swipedToLater(item,0);
            Log.d("id_check ",Integer.toString(id));
        } else {
            Snackbar snackbar = Snackbar.make(mainLayout,getString(R.string.done_action)  + ((NodeAdapter.MyViewHolder) viewHolder).getText(),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction(getString(R.string.undo_action), v -> {
                adapter.restoreItem(item,deleteIndex);
                presenter.restoreFromDone(item, 0);
            });
            presenter.swipedToDone(item,0);

        }
    }

    public void checkDone(List<Note> notes){
        int done = 0, size=0;
        for(Note note: notes){
            if(note.getFrequency()==1){  // only for daily notes
                size++;
                if(note.isDone() && !note.isLater()){
                    done++;
                }
            }
        }
        tvCompletedTasks.setText(Integer.toString(done)+"/");
        tvAllTasks.setText(Integer.toString(size));
    }
}
