package com.company.schedule.ui.oneNote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.OneNoteInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presentation.oneNote.OneNotePresenter;
import com.company.schedule.presentation.oneNote.OneNoteView;
import com.company.schedule.obsolete.timerObsolete.TimerPresenterObsolete;
import com.company.schedule.ui.activities.AddNoteActivity;
import com.company.schedule.ui.main.MainActivity;

import java.util.Calendar;

import static com.company.schedule.utils.Constants.START_TIMER;

public class OneNoteActivity extends AppCompatActivity implements View.OnClickListener, OneNoteView {
    ImageButton btn_later, btn_done, btn_edit, btn_delete, btn_toolbar, btn_toolbarRight;
    TextView tv_name, tv_content, tv_datetime, tv_category, tv_status;
    ImageView img_prior, backward;
    Note noteToShow;
    RelativeLayout mainLayout;
    OneNotePresenter presenter;

    // timer
    TimerPresenterObsolete timerPresenterObsolete;
    Button btnTimer;
    TextView tvTimer;

//  ================_LIFECYCLE_START_================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_note);

        if (presenter == null)  // if presenter isn't created we create it
            presenter = new OneNotePresenter(this,  // init view in presenter
                    new OneNoteInteractor(  // create interactor
                            new MainRepository(
                                    AppDatabase.getDatabase(this).noteDAO(),
                                    new AppSchedulers()  // for threads
                            ),  // create repository and get DAO
                            new SharedPrefsRepository(this)
                    )
            );

        btn_toolbar = findViewById(R.id.btnLeftToolbar);
        btn_toolbar.setVisibility(View.GONE);
        btn_toolbarRight = findViewById(R.id.btnToolbarRight);
        btn_toolbarRight.setVisibility(View.GONE);
        backward = findViewById(R.id.backward_btn);
        backward.setOnClickListener(this);
        backward.setVisibility(View.VISIBLE);
        btn_later = findViewById(R.id.imageButtonLater);
        btn_done = findViewById(R.id.imageButtonDone);
        btn_edit = findViewById(R.id.imageButtonEdit);
        btn_delete = findViewById(R.id.imageButtonDelet);
        tv_name = findViewById(R.id.textViewName);
        tv_content = findViewById(R.id.textViewContent);
        tv_datetime = findViewById(R.id.textViewDateTime);
        tv_category = findViewById(R.id.textViewCategory);
        tv_status = findViewById(R.id.textViewStatus);
        mainLayout = findViewById(R.id.mainlayout);
        img_prior = findViewById(R.id.imageViewPriority);

        btn_later.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_toolbar.setOnClickListener(this);

        noteToShow =(Note) getIntent().getSerializableExtra("note");

        tv_name.setText(noteToShow.getName());
        tv_content.setText(noteToShow.getContent());
        tv_category.setText("Category: " + noteToShow.getCategory());
        String date = "";
        date+=Integer.toString(noteToShow.getDate().get(Calendar.DAY_OF_MONTH));  // TODO use Note.getDateTimeInFormat
        date+=" " +getMonthForInt(noteToShow.getDate().get(Calendar.MONTH));
        date+=" " + noteToShow.getDate().get(Calendar.YEAR);
        date+=" " + noteToShow.getTimeInFormat();
        tv_datetime.setText(date);

        switch (noteToShow.getPriority()){
            case 1:
                img_prior.setImageResource(R.drawable.blue_prior);
                break;
            case 2:
                img_prior.setImageResource(R.drawable.green_prior);
                break;
            case 3:
                img_prior.setImageResource(R.drawable.red_prior);
                break;
            case 4:
                img_prior.setImageResource(R.drawable.yellow_prior);
                break;
        }

        if(noteToShow.isLater()){
            tv_status.setTextColor(getResources().getColor(R.color.redText));
            tv_status.setText("Status: Later");
            btn_later.setVisibility(View.GONE);
        }else{
            if(noteToShow.isDone()){
                tv_status.setText("Status: Done");
                tv_status.setTextColor(getResources().getColor(R.color.greenText));
                btn_later.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
            }else{
                tv_status.setText("Status: To Be Done");
            }
        }

//      Timer

        tvTimer = findViewById(R.id.tvTimer);
        btnTimer = findViewById(R.id.btnTimer);

        presenter.onActivityCreated();

        btnTimer.setText(START_TIMER);
        btnTimer.setOnClickListener(v -> {  // to stop the timer
            presenter.timerAction(btnTimer.getText().toString());
        });

        /*  TODO clean up
        if (savedInstanceState == null) { // if haven't create fragment
            TimerFragmentObsolete timerFragment = new TimerFragmentObsolete();

            // give note for `done` status
            Bundle transmission = new Bundle();
            transmission.putSerializable("NOTE_TO_DONE", noteToShow);
            timerFragment.setArguments(transmission);

            // open fragment transaction
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.timerFragmentContainer, timerFragment);  // add fragment to screen
            fragmentTransaction.commit();
        }
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }


//  ================_OVERRIDES_================
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonDone:
                Snackbar snackbar = Snackbar.make(mainLayout,"Done " + noteToShow.getName(),Snackbar.LENGTH_LONG);
                snackbar.show();
                presenter.updateNoteDone(noteToShow);
                snackbar.setAction("UNDO", v -> {
                    presenter.updateNoteDoneCanceled(noteToShow);
                });

                break;
            case R.id.imageButtonDelet:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.deleteNote(noteToShow.getId());
                        Intent intent = new Intent(OneNoteActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.setMessage("Are you sure do you want to delete this note?");
                builder.setTitle("Confirm");
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.imageButtonLater:
                Snackbar snackbar2 = Snackbar.make(mainLayout,"Postponed " + noteToShow.getName(),Snackbar.LENGTH_LONG);
                snackbar2.show();
                presenter.updateNoteLater(noteToShow);
                snackbar2.setAction("UNDO", v -> {
                    presenter.updateNoteLaterCanceled(noteToShow);
                });
                break;
            case R.id.imageButtonEdit:
                Intent intent = new Intent(this, AddNoteActivity.class);
                intent.putExtra("note", noteToShow);
                startActivity(intent);
                break;
            case R.id.backward_btn:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }


//  ================_ONE_NOTE_VIEW_IMPLEMENTATION_================
    @Override
    public void goToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onLaterButtonClicked() {
        tv_status.setTextColor(getResources().getColor(R.color.redText));
        tv_status.setText("Status: Later");
        btn_later.setVisibility(View.GONE);
    }

    @Override
    public void onDoneButtonClicked() {
        tv_status.setText("Status: Done");
        tv_status.setTextColor(getResources().getColor(R.color.greenText));
        btn_later.setVisibility(View.GONE);
        btn_done.setVisibility(View.GONE);
    }

    @Override
    public void onDoneCanceled() {
        if(!noteToShow.isLater()){
            tv_status.setText("Status: To Be Done");
            tv_status.setTextColor(getResources().getColor(R.color.blackText));
            btn_later.setVisibility(View.VISIBLE);
        }else{
            tv_status.setTextColor(getResources().getColor(R.color.redText));
            tv_status.setText("Status: Later");
            btn_later.setVisibility(View.GONE);
        }
        btn_done.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLaterCanceled() {
        tv_status.setText("Status: To Be Done");
        tv_status.setTextColor(getResources().getColor(R.color.blackText));
        btn_later.setVisibility(View.VISIBLE);
    }


//  ================_TIMER_VIEW_IMPLEMENTATION_================
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
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();

    }

    @Override
    public Note getNote() {
        return noteToShow;
    }


    //  ================_PRIVATE_================
    private String getMonthForInt(int num) {
        String month = "wrong";
        String[] mon = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        if (num >= 0 && num <= 11 ) {
            month = mon[num];
        }
        return month;
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
