package com.company.schedule.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.OneNoteInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presentation.oneNote.OneNotePresenter;
import com.company.schedule.presentation.oneNote.OneNoteView;
import com.company.schedule.ui.main.MainActivity;

import java.util.Calendar;

public class OneNoteActivity extends AppCompatActivity implements View.OnClickListener, OneNoteView {
    ImageButton btn_later, btn_done, btn_edit, btn_delete, btn_toolbar, btn_toolbarRight;
    TextView tv_name, tv_content, tv_datetime, tv_category, tv_status;
    ImageView img_prior, backward;
    Note toshow;
    RelativeLayout mainLayout;
    OneNotePresenter presenter;
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
                            )  // create repository and get DAO
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

        toshow =(Note) getIntent().getSerializableExtra("note");

        tv_name.setText(toshow.getName());
        tv_content.setText(toshow.getContent());
        tv_category.setText("Category: " + toshow.getCategory());
        String date = "";
        date+=Integer.toString(toshow.getDate().get(Calendar.DAY_OF_MONTH));
        date+=" " +getMonthForInt(toshow.getDate().get(Calendar.MONTH));
        date+=" " +toshow.getDate().get(Calendar.YEAR);
        date+=" " +toshow.getTimeInFormat();
        tv_datetime.setText(date);

        switch (toshow.getPriority()){
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

        if(toshow.isLater()){
            tv_status.setTextColor(getResources().getColor(R.color.redText));
            tv_status.setText("Status: Later");
            btn_later.setVisibility(View.GONE);
        }else{
            if(toshow.isDone()){
                tv_status.setText("Status: Done");
                tv_status.setTextColor(getResources().getColor(R.color.greenText));
                btn_later.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
            }else{
                tv_status.setText("Status: To Be Done");
            }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonDone:
                Snackbar snackbar = Snackbar.make(mainLayout,"Done " + toshow.getName(),Snackbar.LENGTH_LONG);
                snackbar.show();
                presenter.updateNoteDone(toshow);
                snackbar.setAction("UNDO", v -> {
                    presenter.updateNoteDoneCanceled(toshow);
                });

                break;
            case R.id.imageButtonDelet:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.deleteNote(toshow.getId());
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
                Snackbar snackbar2 = Snackbar.make(mainLayout,"Postponed " + toshow.getName(),Snackbar.LENGTH_LONG);
                snackbar2.show();
                presenter.updateNoteLater(toshow);
                snackbar2.setAction("UNDO", v -> {
                    presenter.updateNoteLaterCanceled(toshow);
                });
                break;
            case R.id.imageButtonEdit:
                Intent intent = new Intent(this, AddNoteActivity.class);
                intent.putExtra("note",toshow);
                startActivity(intent);
                break;
            case R.id.backward_btn:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }

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
        if(!toshow.isLater()){
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

}
