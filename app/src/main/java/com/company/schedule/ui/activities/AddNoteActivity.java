package com.company.schedule.ui.activities;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.UpdateNoteInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.model.system.MyNotification;
import com.company.schedule.presentation.updateNote.UpdateNotePresenter;
import com.company.schedule.presentation.updateNote.UpdateNoteView;
import com.company.schedule.ui.fragments.MainFragment;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;

import org.w3c.dom.Node;

import java.util.Calendar;
import java.util.GregorianCalendar;

import timber.log.Timber;

public class AddNoteActivity extends AppCompatActivity implements UpdateNoteView, View.OnClickListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private UpdateNotePresenter presenter;
    private Note noteInfo = null;
    private boolean isEdited = false;
    EditText et_name, et_description, et_category, et_date, et_time;
    boolean isReminded = true;
    ImageButton btnBlue, btnGreen,btnRed,btnYellow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        presenter = new UpdateNotePresenter(this,
                new UpdateNoteInteractor(  // create interactor
                        new MainRepository(
                                AppDatabase.getDatabase(this).noteDAO(),
                                new AppSchedulers()  // for threads
                        )  // create repository and get DAO
                )
        );



        et_name =  findViewById(R.id.name_et);  // to enter a note name
        et_description =  findViewById(R.id.description_et);  // to enter a note content
        et_category = findViewById(R.id.category_et); // to enter category

        et_date = findViewById(R.id.date_et);// to enter a date
        et_date.setKeyListener(null);
        et_date.setOnClickListener(this); // when we click, the calendar pops up to enter a date


        et_time = findViewById(R.id.time_et);  // to enter a time
        et_time.setKeyListener(null);
        et_time.setOnClickListener(this);  // when we click, the watch pops up to enter a time

        Button btnSubmitNote = findViewById(R.id.buttonAdd);  // when button click, sends result to MainActivity
        btnSubmitNote.setOnClickListener(this);  // set listener (MainView.OnClickListener, name @Override method is onClick)



        btnBlue = findViewById(R.id.imageButtonBlue);
        btnGreen = findViewById(R.id.imageButtonGreen);
        btnRed = findViewById(R.id.imageButtonRed);
        btnYellow = findViewById(R.id.imageButtonYellow);
        btnBlue.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnRed.setOnClickListener(this);



        Bundle transmission = getIntent().getExtras();

        try{
            noteInfo = (Note) transmission.getSerializable("note");
        }catch (NullPointerException ne){
            ne.printStackTrace();
        }
        if (noteInfo != null) {  // if we want to update note

            isEdited = true;

                et_name.setText(noteInfo.getName());
                et_description.setText(noteInfo.getContent());
                et_category.setText(noteInfo.getCategory());

                // output date in good format
                et_date.setText(noteInfo.getDateInFormat());  // Note: we don't need write checking for noteInfo.getDate() == null
                et_time.setText(noteInfo.getTimeInFormat());


                btnSubmitNote.setText("SAVE");

        } else {  // if we want create new note
            GregorianCalendar currentDate = new GregorianCalendar();// get settings for current time
            currentDate.setTimeInMillis(System.currentTimeMillis());
            currentDate.set(Calendar.SECOND,0);
            currentDate.set(Calendar.MILLISECOND,0);
            int tab = transmission.getInt("tab");
            tab++;
            noteInfo = new Note(
                    0,  // important set id 0 instead -1
                    "",
                    "",
                    currentDate,
                    (byte) tab,
                    0,
                    0,
                    1,
                    ""
            );

        }
    }


    @Override
    public void remindMeIsChecked() { }

    @Override
    public void remindMeIsNotChecked() { }

    // picker for date
    @Override
    public void showDatePickerFragment(GregorianCalendar calendar) {
        new DatePickerFragment()
                .setGc(calendar)
                .setListener(this)
                .show(getSupportFragmentManager(), "date picker");  // show date picker dialog
    }

    @Override
    public void showDatePickerFragment() {
        new DatePickerFragment()
                .setListener(this)
                .show(getSupportFragmentManager(), "date picker");  // show date picker dialog
    }

    // picker for time
    @Override
    public void showTimePickerFragment(GregorianCalendar calendar) {
        new TimePickerFragment()
                .setGc(calendar)  // set gregorian calendar
                .setListener(this)  // set onTimeSet as listener
                .show(getSupportFragmentManager(), "time picker");  // show time picker dialog
    }

    @Override
    public void showTimePickerFragment() {
        new TimePickerFragment()
                .setListener(this)  // setListener return instance
                .show(getSupportFragmentManager(), "time picker");  // show time picker dialog
    }


    // getting result from pickers
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        //month++; // because computer start count month from 0 to 11
        noteInfo.getDate().set(GregorianCalendar.YEAR, year);
        noteInfo.getDate().set(GregorianCalendar.MONTH, month);
        noteInfo.getDate().set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);

        et_date.setText(noteInfo.getDateInFormat());  // when user chose a date we switch it in TV in good date format
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        noteInfo.getDate().set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
        noteInfo.getDate().set(GregorianCalendar.MINUTE,minute);

        et_time.setText(noteInfo.getTimeInFormat());  // when user chose a date we switch it in TV in good time format
    }

    @Override
    public void createNotification(Note note, int id) {
        MyNotification myNotification = new MyNotification(this);
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
        Toast.makeText(this, toast_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastLong(String toast_message) {
        Toast.makeText(this, toast_message, Toast.LENGTH_LONG).show();
    }

    // TODO make fragment transition through fragment, but don't through activity
    @Override
    public void goToMainFragment() {
      /*  Fragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        // fragmentTransaction.replace(R.id.fragmentContainer, fragment);  // add fragment to screen
        fragmentTransaction.commit();*/
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonBlue:
                noteInfo.setPriority(1);
                btnBlue.setBackgroundResource(R.color.specialGrey);
                btnYellow.setBackgroundResource(R.color.colorWhite);
                btnGreen.setBackgroundResource(R.color.colorWhite);
                btnRed.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.imageButtonGreen:
                noteInfo.setPriority(2);
                btnBlue.setBackgroundResource(R.color.colorWhite);
                btnYellow.setBackgroundResource(R.color.colorWhite);
                btnGreen.setBackgroundResource(R.color.specialGrey);
                btnRed.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.imageButtonRed:
                noteInfo.setPriority(3);
                btnBlue.setBackgroundResource(R.color.colorWhite);
                btnYellow.setBackgroundResource(R.color.colorWhite);
                btnGreen.setBackgroundResource(R.color.colorWhite);
                btnRed.setBackgroundResource(R.color.specialGrey);
                break;
            case R.id.imageButtonYellow:
                noteInfo.setPriority(4);
                btnBlue.setBackgroundResource(R.color.colorWhite);
                btnYellow.setBackgroundResource(R.color.specialGrey);
                btnGreen.setBackgroundResource(R.color.colorWhite);
                btnRed.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.date_et:  //if clicking on TextView with date
                presenter.pressedToEditDate(isEdited, noteInfo.getDate());
                break;
            case R.id.time_et:   //if clicking on TextView with time
                presenter.pressedToEditTime(isEdited, noteInfo.getDate());
                break;
            case R.id.buttonAdd:  // if button send note to DB already pressed
                noteInfo.setName(et_name.getText().toString());
                noteInfo.setContent(et_description.getText().toString());
                noteInfo.setCategory(et_category.getText().toString());
                if(noteInfo.getName().trim().isEmpty()){
                    toastLong("Note must have name");
                }else{
                    GregorianCalendar check = new GregorianCalendar();
                    if (noteInfo.getDate().getTimeInMillis() > check.getTimeInMillis()) {
                        Log.d("datecheck",noteInfo.getDate().get(Calendar.MINUTE)+"");
                        presenter.pressedToSubmitNote(noteInfo, isEdited);

                    } else {
                        Timber.w("Date should be in future");
                        toastLong("Date should be in future");
                    }
                }
                break;
        }
    }
}
