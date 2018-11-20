package com.company.schedule.obsolete.updateNoteObsolete;

import android.support.v4.app.Fragment;

//import com.company.schedule.model.interactor.UpdateNoteInteractor;
//import com.company.schedule.presentation.updateNote.UpdateNotePresenter;
//import com.company.schedule.presentation.updateNote.UpdateNoteView;


public class UpdateNoteFragment extends Fragment {// implements UpdateNoteView, View.OnClickListener, CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
/* TODO obsolete

    private MainActivity mainActivity;
    private UpdateNotePresenter presenter;

    private EditText etNameNote, etContentNote;  // EditTexts for enter name and content of note
    private Switch swtRemindMe;
    private LinearLayout llDateTime, llSpinner;  // LinearLayout which contain two object(id.editDate, id.editTime)

    private TextView editDate, editTime;
    private Note noteInfo;
    private Spinner spinnerFreq;
    private boolean isEdited = false, isReminded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new UpdateNotePresenter(this,
                new UpdateNoteInteractor(  // create interactor
                        new MainRepository(
                                AppDatabase.getDatabase(getContext()).noteDAO(),
                                new AppSchedulers()  // for threads
                        )  // create repository and get DAO

                )
        );

        Bundle transmission = this.getArguments();

        if (transmission != null) {  // if we want to update note
            noteInfo = (Note) transmission.getSerializable("note");
            if (noteInfo == null) Log.e("myLog", "note info equals null");
            isEdited = true;
            // set names and content in edit text for edit
        } else {  // if we want create new note
            GregorianCalendar currentDate = new GregorianCalendar();// get settings for current time
            currentDate.setTimeInMillis(System.currentTimeMillis());
            currentDate.set(Calendar.SECOND,0);
            currentDate.set(Calendar.MILLISECOND,0);

            noteInfo = new Note(
                    0,  // important set id 0 instead -1
                    "",
                    "",
                    currentDate,
                    (byte) 0,
                    false
            );

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null)
            Timber.v("Saved instance state != null");

        View fragmentUpdateNote = inflater.inflate(R.layout.fragment_update_note, container, false);
        mainActivity = (MainActivity) getActivity();

        etNameNote =  fragmentUpdateNote.findViewById(R.id.etNameNote);  // to enter a note name
        etContentNote =  fragmentUpdateNote.findViewById(R.id.etNameContent);  // to enter a note content


        editDate =  fragmentUpdateNote.findViewById(R.id.editDate);  // to enter a date
        editDate.setOnClickListener(this);  // when we click, the calendar pops up to enter a date

        editTime = fragmentUpdateNote.findViewById(R.id.editTime);  // to enter a time
        editTime.setOnClickListener(this);  // when we click, the watch pops up to enter a time

        Button btnSubmitNote = fragmentUpdateNote.findViewById(R.id.btnSubmitNote);  // when button click, sends result to MainActivity
        btnSubmitNote.setOnClickListener(this);  // set listener (MainView.OnClickListener, name @Override method is onClick)

        swtRemindMe = fragmentUpdateNote.findViewById(R.id.swtRemindMe);
        swtRemindMe.setOnCheckedChangeListener(this);  // set listener (CompoundButton.OnCheckedChangeListener, name @Override method is onCheckedChanged)


        llDateTime = fragmentUpdateNote.findViewById(R.id.llDateTime);  // by default visibility == gone
        llSpinner = fragmentUpdateNote.findViewById(R.id.llSpinner);
        //llDateTime.setVisibility(MainView.GONE);


        FloatingActionButton fab_delete = fragmentUpdateNote.findViewById(R.id.fab_delete);  // button for jump to AddNoteActivity
        fab_delete.setOnClickListener(this);  // setting handle

        //setting frequency spinner
        spinnerFreq = fragmentUpdateNote.findViewById(R.id.spinnerFreq);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.types_of_frequency, R.layout.simple_spinner_item);  // TODO comment it
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFreq.setAdapter(adapter);

        return fragmentUpdateNote;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (isEdited) {
            Fragment childFragment = new TimerFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            Bundle transmission = new Bundle();
            transmission.putSerializable("NOTE_TO_DONE", noteInfo);
            childFragment.setArguments(transmission);

            transaction.replace(R.id.childTimerContainer, childFragment);
            transaction.commit();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isEdited) {
            etNameNote.setText(noteInfo.getName());
            etContentNote.setText(noteInfo.getContent());

            // output date in good format
            editDate.setText(noteInfo.getDateInFormat());  // Note: we don't need write checking for noteInfo.getDate() == null
            editTime.setText(noteInfo.getTimeInFormat());

            if (noteInfo.getFrequency() != -1)
                spinnerFreq.setSelection((int) noteInfo.getFrequency());

            //if field 'date' of CustomNotify object is null, so notify shouldn't be reminded
            if (noteInfo.isDateNull()) swtRemindMe.setChecked(false);
            else swtRemindMe.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.editDate:  //if clicking on TextView with date
            presenter.pressedToEditDate(isEdited, noteInfo.getDate());
            break;
        case R.id.editTime:   //if clicking on TextView with time
            presenter.pressedToEditTime(isEdited, noteInfo.getDate());
            break;
        case R.id.btnSubmitNote:  // if button send note to DB already pressed
            noteInfo.setName(etNameNote.getText().toString());
            noteInfo.setContent(etContentNote.getText().toString());
            noteInfo.setFrequency( (byte) spinnerFreq.getSelectedItemPosition());
            if(noteInfo.getName().trim().isEmpty()){
                toastLong("Note must have name");
            }else{
                GregorianCalendar check = new GregorianCalendar();
                if (!isReminded || noteInfo.getDate().getTimeInMillis() > check.getTimeInMillis()) {
                    presenter.pressedToSubmitNote(noteInfo, isEdited, isReminded);
                } else {
                    Timber.w("Date should be in future");
                    toastLong("Date should be in future");
                }
            }
            break;
        case R.id.fab_delete:
            presenter.pressedToFabDelete(isEdited, noteInfo.getId());
            break;
        }
    }

    //method for 'remind' button
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // VISIBLE(0) - ViewGroup exist and it is visible
        // INVISIBLE(4) - ViewGroup exist but invisible. It TAKE place on the screej
        // GONE(8); - ViewGroup don't exist and invisible. It does NOT TAKE place on the screen
        switch (compoundButton.getId()){
            case R.id.swtRemindMe:
                presenter.changedRemindMe(isChecked);
                break;
            default:
                Timber.e("onCheckedChanged default, compoundButton.getId: " + compoundButton.getId() + "; isChecked: " + isChecked);
        }

    }

    @Override
    public void remindMeIsChecked() {
        if (!isEdited || noteInfo.isDateNull()) { // if from add note we update date every time
            GregorianCalendar date = (GregorianCalendar) Calendar.getInstance(); // update to current date/time
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            noteInfo.setDate(date);
        }

        // output date depending on local settings
        editDate.setText(noteInfo.getDateInFormat());
        editTime.setText(noteInfo.getTimeInFormat());
        llSpinner.setVisibility(View.VISIBLE);
        llDateTime.setVisibility(View.VISIBLE);  // and all MainView in ViewGroup become visible and exist (date and time)
        isReminded = true;
        Timber.v("onCheckedChanged dateNotification.get(): " + editDate.getText().toString() + " " + editTime.getText().toString());
    }

    @Override
    public void remindMeIsNotChecked() {
//      gone  EditText for Date and for Time
        llSpinner.setVisibility(View.GONE);
        llDateTime.setVisibility(View.GONE);  // all MainView in ViewGroup become invisible and doesn't exist
        isReminded = false;
    }

    // picker for date
    @Override
    public void showDatePickerFragment(GregorianCalendar calendar) {
        new DatePickerFragment()
                .setGc(calendar)
                .setListener(this)
                .show(mainActivity.getSupportFragmentManager(), "date picker");  // show date picker dialog
    }

    @Override
    public void showDatePickerFragment() {
        new DatePickerFragment()
                .setListener(this)
                .show(mainActivity.getSupportFragmentManager(), "date picker");  // show date picker dialog
    }

    // picker for time
    @Override
    public void showTimePickerFragment(GregorianCalendar calendar) {
        new TimePickerFragment()
                .setGc(calendar)  // set gregorian calendar
                .setListener(this)  // set onTimeSet as listener
                .show(mainActivity.getSupportFragmentManager(), "time picker");  // show time picker dialog
    }

    @Override
    public void showTimePickerFragment() {
        new TimePickerFragment()
                .setListener(this)  // setListener return instance
                .show(mainActivity.getSupportFragmentManager(), "time picker");  // show time picker dialog
    }


    // getting result from pickers
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        //month++; // because computer start count month from 0 to 11
        noteInfo.getDate().set(GregorianCalendar.YEAR, year);
        noteInfo.getDate().set(GregorianCalendar.MONTH, month);
        noteInfo.getDate().set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);

        editDate.setText(noteInfo.getDateInFormat());  // when user chose a date we switch it in TV in good date format
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        noteInfo.getDate().set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
        noteInfo.getDate().set(GregorianCalendar.MINUTE,minute);

        editTime.setText(noteInfo.getTimeInFormat());  // when user chose a date we switch it in TV in good time format
    }

    @Override
    public void createNotification(Note note, int id) {
        MyNotification myNotification = new MyNotification(getContext());
        note.getDate().set(Calendar.SECOND,0);
        note.getDate().set(Calendar.MILLISECOND,0);
        Notification local = myNotification.getNotification(note.getName(), note.getContent());

        myNotification.scheduleNotification(local,
                id,
                note
        );

    }


    @Override
    public void toast(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastLong(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_LONG).show();
    }

    // TODO make fragment transition through fragment, but don't through activity
    @Override
    public void goToMainFragment() {
        Fragment fragment = new MainFragmentObsolete();
        FragmentTransaction fragmentTransaction = mainActivity
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);  // add fragment to screen
        fragmentTransaction.commit();
    }


    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
    */
}
