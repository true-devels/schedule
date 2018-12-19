package com.company.schedule.ui.oneNote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.company.schedule.ui.addNote.AddNoteActivity;
import com.company.schedule.ui.later.LaterActivity;
import com.company.schedule.ui.main.MainActivity;
import com.company.schedule.ui.settings.SettingsActivity;
import com.company.schedule.ui.statistics.StatisticActivity;

import static com.company.schedule.utils.Constants.START_TIMER;

public class OneNoteActivity extends AppCompatActivity implements View.OnClickListener, OneNoteView {
    ImageButton btn_later, btn_done, btn_edit, btn_delete, btn_toolbar, btn_toolbarRight;
    TextView tv_name, tv_content, tv_datetime, tv_category, tv_status;
    ImageView img_prior;
    Note noteToShow;
    RelativeLayout mainLayout;
    OneNotePresenter presenter;
    DrawerLayout mDrawerLayout;
    // timer
    Button btnTimer;
    TextView tvTimer;

//  ================_LIFECYCLE_START_================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsRepository sharedPrefs = new SharedPrefsRepository(this);
        if(sharedPrefs.isNightMode()) setTheme(R.style.darktheme);  //dark
        else setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_one_note);

        if (presenter == null)  // if presenter isn't created we create it
            presenter = new OneNotePresenter(this,  // init view in presenter
                    new OneNoteInteractor(  // create interactor
                            new MainRepository(
                                    AppDatabase.getDatabase(this).noteDAO(),
                                    new AppSchedulers()  // for threads
                            ),  // create repository and get DAO
                            new SharedPrefsRepository(this)
                    ), this
            );


        // init view components
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    switch (menuItem.getItemId()){
                        case R.id.nav_later:
                            Intent intent= new Intent(this,LaterActivity.class);
                            intent.putExtra("role",1);
                            startActivity(intent);

                            break;
                        case R.id.nav_done:
                            Intent intent2 = new Intent(this,LaterActivity.class);
                            intent2.putExtra("role",2);
                            startActivity(intent2);
                            break;
                        case R.id.nav_settings:
                            Intent intent3 = new Intent(this,SettingsActivity.class);
                            startActivity(intent3);
                            break;
                        case R.id.nav_home:
                            goToMainActivity();
                            break;

                        case R.id.nav_statistic:
                            Intent intent4 = new Intent(this,StatisticActivity.class);
                            startActivity(intent4);
                    }
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    return true;
                });

        final Toolbar toolbar =  findViewById(R.id.my_toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);

        btn_toolbar = findViewById(R.id.btnLeftToolbar);
        btn_toolbar.setOnClickListener(this);

        btn_toolbarRight = findViewById(R.id.btnToolbarRight);
        btn_toolbarRight.setVisibility(View.GONE);


        btn_later = findViewById(R.id.imageButtonLater);
        btn_done = findViewById(R.id.imageButtonDone);
        btn_edit = findViewById(R.id.imageButtonEdit);
        btn_delete = findViewById(R.id.imageButtonDelet);
        tv_name = findViewById(R.id.textViewName);
        tv_content = findViewById(R.id.textViewContent);
        tv_datetime = findViewById(R.id.textViewDateTime);
        tv_category = findViewById(R.id.textViewCategory);
        tv_status = findViewById(R.id.textViewStatus);
        mainLayout = findViewById(R.id.mainLayout);
        img_prior = findViewById(R.id.imageViewPriority);

        btn_later.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_toolbar.setOnClickListener(this);

        noteToShow =(Note) getIntent().getSerializableExtra("note");

        tv_name.setText(noteToShow.getName());
        tv_content.setText(noteToShow.getContent());
        String category = getString(R.string.category);
        tv_category.setText(category+ noteToShow.getCategory());
        tv_datetime.setText(noteToShow.getDateTimeInFormat());

        switch (noteToShow.getPriority()){
            case 1:
                img_prior.setImageResource(R.drawable.prior1);
                break;
            case 2:
                img_prior.setImageResource(R.drawable.prior2);
                break;
            case 3:
                img_prior.setImageResource(R.drawable.prior3);
                break;
            case 4:
                img_prior.setImageResource(R.drawable.prior4);
                break;
        }

        if(noteToShow.isLater()){
            //tv_status.setTextColor(ContextCompat.getColor(this, R.color.colorTextStatus));
            tv_status.setText(R.string.laterStatus);
            btn_later.setVisibility(View.GONE);
        } else {
            if(noteToShow.isDone()){
                tv_status.setText(R.string.doneStatus);
                //tv_status.setTextColor(ContextCompat.getColor(this, R.color.colorTextStatus));
                btn_later.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
            }else{
                tv_status.setText(R.string.toBeDoneStatus);
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
                presenter.doneClicked(noteToShow);
                break;
            case R.id.imageButtonLater:
                presenter.laterClicked(noteToShow);
                break;

            case R.id.imageButtonEdit:
                Intent intent = new Intent(this, AddNoteActivity.class);
                intent.putExtra("note", noteToShow);
                startActivity(intent);
                break;

            case R.id.imageButtonDelet:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            presenter.deleteClicked(noteToShow.getId());
                            Intent intent = new Intent(OneNoteActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                builder.setMessage(R.string.question);
                builder.setTitle(R.string.confirm);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.btnLeftToolbar:
                mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }


//  ================_ONE_NOTE_VIEW_IMPLEMENTATION_================
    @Override
    public void goToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void setStatusDone() {
        tv_status.setText(R.string.doneStatus);
        //tv_status.setTextColor(R.attr.paleGreen);

        Snackbar snackbar = Snackbar.make(mainLayout,getString(R.string.done_action) + noteToShow.getName(),Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setAction(getString(R.string.undo_action), v -> presenter.doneCanceled(noteToShow));
    }

    @Override
    public void setStatusLater() {
        tv_status.setText(R.string.laterStatus);
        //tv_status.setTextColor(R.attr.paleRed);

        Snackbar snackbar2 = Snackbar.make(mainLayout, getString(R.string.postponed_action) + noteToShow.getName(),Snackbar.LENGTH_LONG);
        snackbar2.show();
        snackbar2.setAction(getString(R.string.undo_action), v -> presenter.laterCanceled(noteToShow));
    }

    @Override
    public void setStatusToBeDone() {
        tv_status.setText(R.string.toBeDoneStatus);
        //tv_status.setTextColor(R.attr.colorText);
    }

    @Override
    public void setBtnDoneVisible() {
        btn_done.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBtnDoneInvisible() {
        btn_done.setVisibility(View.GONE);
    }

    @Override
    public void setBtnLaterVisible() {
        btn_later.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBtnLaterInvisible() {
        btn_later.setVisibility(View.GONE);
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
