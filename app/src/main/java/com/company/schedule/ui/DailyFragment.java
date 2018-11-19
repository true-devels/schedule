package com.company.schedule.ui.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presentation.main.MainPresenter;
import com.company.schedule.presentation.main.MainView;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.ui.main.NodeAdapter;
import com.company.schedule.utils.RecyclerViewItemTouchHelper;
import com.company.schedule.utils.RecyclerViewItemTouchHelperListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import timber.log.Timber;

public class DailyFragment extends Fragment implements MainView, RecyclerViewItemTouchHelperListener {
    MainActivity mainActivity;
    RecyclerView notes_rc;
    NodeAdapter adapter;
    MainPresenter presenter;
    RelativeLayout mainLayout;
    TextView tv_date;
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
        mainActivity = (MainActivity) getActivity();
        View fragmentDaily = inflater.inflate(R.layout.fragment_daily, container, false);
        notes_rc= fragmentDaily.findViewById(R.id.my_recycler_view);
        mainLayout = fragmentDaily.findViewById(R.id.mainlayout);
        tv_date = fragmentDaily.findViewById(R.id.textView_date);
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
        presenter.refreshDailyData();
        adapter = new NodeAdapter(getContext());

        notes_rc.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback callback_left = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(callback_left).attachToRecyclerView(notes_rc);

        ItemTouchHelper.SimpleCallback callback_right = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(callback_right).attachToRecyclerView(notes_rc);

        Calendar now = new GregorianCalendar();
        String toshow = now.get(Calendar.DAY_OF_MONTH)+" ";
        toshow+= getMonthForInt(now.get(Calendar.MONTH))+", ";
        toshow+= now.get(Calendar.YEAR);

        tv_date.setText(toshow);

    }

    @Override
    public void setAllNotes(List<Note> newNotes) {
        adapter.setAllNotes(newNotes);
    }

    @Override
    public void toast(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastLong(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        int deleteIndex = viewHolder.getAdapterPosition ();
        Note item = adapter.removeItem(deleteIndex);
        if(direction == ItemTouchHelper.LEFT){
            Snackbar snackbar = Snackbar.make(mainLayout,"Postponed " + ((NodeAdapter.MyViewHolder) viewHolder).mTextView.getText(),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("UNDO", v -> {
                adapter.restoreItem(item,deleteIndex);
                presenter.restoreFromLater(item, 0);
            });
            int id = ((NodeAdapter.MyViewHolder) viewHolder).id;
            presenter.swipedToLater(item,0);
            Log.d("id_check ",Integer.toString(id));
        }else{
            Snackbar snackbar = Snackbar.make(mainLayout,"Done " + ((NodeAdapter.MyViewHolder) viewHolder).mTextView.getText(),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("UNDO", v -> {
                adapter.restoreItem(item,deleteIndex);
                presenter.restoreFromDone(item, 0);
            });
            presenter.swipedToDone(item,0);
        }
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        String[] mon = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        if (num >= 0 && num <= 11 ) {
            month = mon[num];
        }
        return month;
    }
}
