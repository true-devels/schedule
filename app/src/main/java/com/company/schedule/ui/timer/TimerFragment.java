package com.company.schedule.ui.timer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.TimerInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presentation.timer.TimerPresenter;
import com.company.schedule.presentation.timer.TimerView;

import static com.company.schedule.utils.Constants.START_TIMER;

public class TimerFragment extends Fragment implements TimerView {

    TimerPresenter presenter;

    Button btnTimer;
    TextView tvTimer;

//  ================_LIFECYCLE_START_================
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle transmission = this.getArguments();
        Note noteToDone = null;  // note to update `done` when timer will end

        if (transmission != null) {  // if we want to update note
            noteToDone = (Note) transmission.getSerializable("NOTE_TO_DONE");
        } else {
            showErrorMessage("transmission got lost");
        }


        if (presenter == null)  // if presenter isn't created we create it
            presenter = new TimerPresenter(this,
                    noteToDone,
                    new TimerInteractor(
                            new MainRepository(
                                    AppDatabase.getDatabase(getContext()).noteDAO(), // table Notes
                                    new AppSchedulers()  // threads
                            ),
                            new SharedPrefsRepository(getContext())
                    )
            );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentTimer = inflater.inflate(R.layout.fragment_timer, container, false);

        tvTimer = (TextView) fragmentTimer.findViewById(R.id.tvTimer);

        btnTimer = (Button) fragmentTimer.findViewById(R.id.btnTimer);

        return fragmentTimer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        presenter.onActivityCreated();

        btnTimer.setText(START_TIMER);
        btnTimer.setOnClickListener(v -> {  // to stop the timer
            presenter.timerAction(btnTimer.getText().toString());
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }


//  ================_VIEW_IMPLEMENTATION_================
    @Override
    public void setTimerText(String timeInFormat) {
        tvTimer.setText(timeInFormat);
    }

    @Override
    public void setBtnTimerText(String btnAction) {
        btnTimer.setText(btnAction);
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();

    }


//  ================_LIFECYCLE_FINISH_================
    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }



    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

}
/*
Fragment lifecycle
onAttach
-onCreate
-onCreateView
-onActivityCreated
-onStart
onResume

onPause
-onStop
onDestroyView
-onDestroy
onDetach
 */