package com.company.schedule.presentation.ui.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.interactor.UpdateNoteInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.model.system.MyNotification;
import com.company.schedule.presentation.presenter.UpdateNotePresenter;
import com.company.schedule.presentation.ui.activities.MainActivity;
import com.company.schedule.presentation.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.presentation.ui.fragments.pickers.TimePickerFragment;
import com.company.schedule.view.UpdateNoteView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import timber.log.Timber;

public class UpdateNoteFragment extends Fragment  implements UpdateNoteView, View.OnClickListener, CompoundButton.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private MainActivity mainActivity;
    private UpdateNotePresenter presenter;

    private EditText etNameNote, etContentNote;  // EditTexts for enter name and content of note

    private LinearLayout llDateTime, llSpinner;  // LinearLayout which contain two object(id.editDate, id.editTime)

    private TextView editDate, editTime;
    private Note noteInfo;
    private Spinner spinnerFreq;
    private Switch swtRemindMe;
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

        //if note is editing, there is some data, that should be read from intent
        if (savedInstanceState != null) {
            isEdited = true;

            noteInfo = new Note(
//<<<<<<< HEAD:app/src/main/java/com/company/schedule/presentation/ui/fragments/UpdateNoteFragment.java
                    savedInstanceState.getInt("id", 0),  // important 0 instead -1
                    savedInstanceState.getString("name"),
                    savedInstanceState.getString("content"),
                    (GregorianCalendar) savedInstanceState.get("date"),
                    savedInstanceState.getByte("frequency"),
                    savedInstanceState.getBoolean("done")
//=======
//                    extras.getString("name"),
//                    extras.getString("content"),
//                    (GregorianCalendar) extras.get("date"),
//                    extras.getByte("frequency"),
//                    extras.getBoolean("done")
//>>>>>>> master:app/src/main/java/com/company/schedule/ui/activities/AddNoteActivity.java
            );
//            noteInfo.setId( extras.getInt("id", -1));
            etNameNote.setText(noteInfo.getName());
            etContentNote.setText(noteInfo.getContent());

            // output date in good format
            editDate.setText(noteInfo.getDateInFormat());  // Note: we don't need write checking for noteInfo.getDate() == null
            editTime.setText(noteInfo.getTimeInFormat());

            if (noteInfo.getFrequency() != -1)
                spinnerFreq.setSelection((int) noteInfo.getFrequency());

            //if field 'date' of CustomNotify object is null, so notify shouldn't be reminded
            if (noteInfo.getDate() == null) swtRemindMe.setChecked(false);
            else swtRemindMe.setChecked(true);

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
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // TODO comment it
        spinnerFreq.setAdapter(adapter);

        return fragmentUpdateNote;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

            GregorianCalendar check = new GregorianCalendar();
            if(isReminded && check.getTimeInMillis() > noteInfo.getDate().getTimeInMillis()) {
                Timber.w("Date should be in future");
                toastLong("Date should be in future");
            } else presenter.pressedToSubmitNote(noteInfo, isReminded);
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
        if (!isEdited || noteInfo.getDate() == null) { // if from add note we update date every time
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
    public void createNotification(Note note) {
        MyNotification myNotification = new MyNotification(mainActivity);
        Notification local = myNotification.getNotification(note.getName(), note.getContent());

        myNotification.scheduleNotification(local,
                note.getDate().getTimeInMillis(),
                note.getFrequency(),
                (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE),
                note.getId()
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

    @Override
    public void goToMainFragment() {
        mainActivity.replaceFragment(new MainFragment(), false);
    }


    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
