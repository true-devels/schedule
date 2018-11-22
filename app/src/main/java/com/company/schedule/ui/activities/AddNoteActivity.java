package com.company.schedule.ui.activities;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.AddNoteInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.model.system.MyNotification;
import com.company.schedule.presentation.addNote.AddNotePresenter;
import com.company.schedule.presentation.addNote.AddNoteView;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.ui.pickers.DatePickerFragment;
import com.company.schedule.ui.pickers.TimePickerFragment;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddNoteActivity extends AppCompatActivity implements AddNoteView, View.OnClickListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private AddNotePresenter presenter;
    private Note noteInfo = null;
    private boolean isEdited = false;
    EditText et_name, et_description, et_category, et_date, et_time;
    ImageButton btnBlue, btnGreen,btnRed,btnYellow, btn_right, btn_left;
    TextInputLayout date_layout;
    MaterialBetterSpinner week_spinner;
    boolean isSelected = true;
    ImageView backward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsRepository sharedPrefs = new SharedPrefsRepository(this);
        if(sharedPrefs.isNightMode()) setTheme(R.style.darktheme);  //dark
        else setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_add_note);

        presenter = new AddNotePresenter(this,
                new AddNoteInteractor(  // create interactor
                        new MainRepository(
                                AppDatabase.getDatabase(this).noteDAO(),
                                new AppSchedulers()  // for threads
                        )  // create repository and get DAO
                )
        );



        et_name =  findViewById(R.id.name_et);  // to enter a note name
        et_description =  findViewById(R.id.description_et);  // to enter a note content
        et_category = findViewById(R.id.category_et); // to enter category
        date_layout = findViewById(R.id.date_layout);
        backward = findViewById(R.id.backward_btn);
        backward.setVisibility(View.VISIBLE);
        btn_right = findViewById(R.id.btnToolbarRight);
        btn_right.setVisibility(View.GONE);

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_left = findViewById(R.id.btnLeftToolbar);
        btn_left.setVisibility(View.GONE);

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


        String[] SPINNERLIST = {"Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday","Sunday"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        week_spinner = findViewById(R.id.week_spinner);
        week_spinner.setAdapter(arrayAdapter);
        week_spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int pos = Arrays.asList(SPINNERLIST).indexOf(s.toString());
                isSelected = true;
                switch (pos){
                    case 0:
                        noteInfo.getCalendarDate().set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        break;
                    case 1:
                        noteInfo.getCalendarDate().set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                        break;
                    case 2:
                        noteInfo.getCalendarDate().set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        break;
                    case 3:
                        noteInfo.getCalendarDate().set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                        break;
                    case 4:
                        noteInfo.getCalendarDate().set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        break;
                    case 5:
                        noteInfo.getCalendarDate().set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        break;
                    case 6:
                        noteInfo.getCalendarDate().set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        break;
                }
                if(noteInfo.getCalendarDate().getTimeInMillis()<new Date().getTime()){
                    noteInfo.getCalendarDate().set(Calendar.WEEK_OF_YEAR,noteInfo.getCalendarDate().get(Calendar.WEEK_OF_YEAR)+1);
                }
            }
        });

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
                et_date.setText(noteInfo.getDateInFormat());  // Note: we don't need write checking for noteInfo.getCalendarDate() == null
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
                    false,
                    false,
                    1,
                    ""
            );
            switch (tab){
                case 1:
                    btnSubmitNote.setText("ADD DAILY NOTE");
                    date_layout.setVisibility(View.GONE);
                    week_spinner.setVisibility(View.GONE);
                    break;
                case 2:
                    btnSubmitNote.setText("ADD WEEKLY NOTE");
                    date_layout.setVisibility(View.GONE);
                    isSelected = false;
                    break;
                case 3:
                    btnSubmitNote.setText("ADD MONTHLY NOTE");
                    week_spinner.setVisibility(View.GONE);
                    break;
            }
        }
    }


    @Override
    public void remindMeIsChecked() { }

    @Override
    public void remindMeIsNotChecked() { }

    // picker for date
    @Override
    public void showDatePickerFragment(GregorianCalendar calendar) {
        new DatePickerFragment(this)
                .setGc(calendar)
                .show(getSupportFragmentManager(), "date picker");  // show date picker dialog
    }

    @Override
    public void showDatePickerFragment() {
        new DatePickerFragment(this)
                .show(getSupportFragmentManager(), "date picker");  // show date picker dialog
    }

    // picker for time
    @Override
    public void showTimePickerFragment(GregorianCalendar calendar) {
        new TimePickerFragment(this)
                .setGc(calendar)  // set gregorian calendar
                .show(getSupportFragmentManager(), "time picker");  // show time picker dialog
    }

    @Override
    public void showTimePickerFragment() {
        new TimePickerFragment(this) // setListener return instance
                .show(getSupportFragmentManager(), "time picker");  // show time picker dialog
    }


    // getting result from pickers
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        //month++; // because computer start count month from 0 to 11
        noteInfo.getCalendarDate().set(GregorianCalendar.YEAR, year);
        noteInfo.getCalendarDate().set(GregorianCalendar.MONTH, month);
        noteInfo.getCalendarDate().set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);

        et_date.setText(noteInfo.getDateInFormat());  // when user chose a date we switch it in TV in good date format
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        noteInfo.getCalendarDate().set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
        noteInfo.getCalendarDate().set(GregorianCalendar.MINUTE,minute);

        et_time.setText(noteInfo.getTimeInFormat());  // when user chose a date we switch it in TV in good time format
    }

    @Override
    public void createNotification(Note note, int id) {
        MyNotification myNotification = new MyNotification(this);
        note.getCalendarDate().set(Calendar.SECOND,0);
        note.getCalendarDate().set(Calendar.MILLISECOND,0);
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

    @Override
    public void goToMainActivity() {
      /*  Fragment fragment = new MainFragmentObsolete();
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
                btnBlue.setBackgroundResource(R.color.colorBackground);
                btnYellow.setBackgroundResource(R.color.colorWhite);
                btnGreen.setBackgroundResource(R.color.colorWhite);
                btnRed.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.imageButtonGreen:
                noteInfo.setPriority(2);
                btnBlue.setBackgroundResource(R.color.colorWhite);
                btnYellow.setBackgroundResource(R.color.colorWhite);
                btnGreen.setBackgroundResource(R.color.colorBackground);
                btnRed.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.imageButtonRed:
                noteInfo.setPriority(3);
                btnBlue.setBackgroundResource(R.color.colorWhite);
                btnYellow.setBackgroundResource(R.color.colorWhite);
                btnGreen.setBackgroundResource(R.color.colorWhite);
                btnRed.setBackgroundResource(R.color.colorBackground);
                break;
            case R.id.imageButtonYellow:
                noteInfo.setPriority(4);
                btnBlue.setBackgroundResource(R.color.colorWhite);
                btnYellow.setBackgroundResource(R.color.colorBackground);
                btnGreen.setBackgroundResource(R.color.colorWhite);
                btnRed.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.date_et:  //if clicking on TextView with date
                presenter.pressedToEditDate(isEdited, noteInfo.getCalendarDate());
                break;
            case R.id.time_et:   //if clicking on TextView with time
                presenter.pressedToEditTime(isEdited, noteInfo.getCalendarDate());
                break;
            case R.id.buttonAdd:  // if button send note to DB already pressed
                noteInfo.setName(et_name.getText().toString());
                noteInfo.setContent(et_description.getText().toString());
                noteInfo.setCategory(et_category.getText().toString());
                if(noteInfo.getName().trim().isEmpty()){
                    toastLong("Note must have name");
                }else{
                    Log.d("da id ",week_spinner.getVerticalScrollbarPosition()+"");
                    week_spinner.setVerticalScrollbarPosition(3);
                   // week_spinner.setVisibility(View.GONE);
                    if(isSelected){
                        if(noteInfo.getCalendarDate().getTimeInMillis()<new Date().getTime()){
                            if(noteInfo.getFrequency()==1){
                                noteInfo.getCalendarDate().set(Calendar.DAY_OF_YEAR, noteInfo.getCalendarDate().get(Calendar.DAY_OF_YEAR)+1);
                            }
                            if(noteInfo.getFrequency()==3){
                                GregorianCalendar local = new GregorianCalendar();
                                local.set(Calendar.MONTH,local.get(Calendar.MONTH)+1);
                                if(noteInfo.getCalendarDate().get(Calendar.DAY_OF_MONTH)>local.getActualMaximum(Calendar.DAY_OF_MONTH)){
                                    noteInfo.getCalendarDate().set(Calendar.MONTH,local.get(Calendar.MONTH));
                                    noteInfo.getCalendarDate().set(Calendar.DAY_OF_MONTH, local.getActualMaximum(Calendar.DAY_OF_MONTH));
                                }else{
                                    noteInfo.getCalendarDate().set(Calendar.MONTH,Calendar.MONTH+1);
                                }
                            }
                        }

                        Log.d("datecheck",noteInfo.getDateTimeInFormat());

                        presenter.pressedToSubmitNote(noteInfo, isEdited);}
                        else{
                            toast("You should choose day of week");
                        }

                }
                break;
        }
    }
}


