package com.company.schedule.obsolete.timerObsolete;

import android.support.v4.app.Fragment;

import com.company.schedule.obsolete.timerObsolete.TimerViewObsolete;

public class TimerFragmentObsolete extends Fragment implements TimerViewObsolete {
/*
    TimerPresenterObsolete presenter;

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
            presenter = new TimerPresenterObsolete(this,
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
*/
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