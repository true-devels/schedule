package com.company.schedule.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.company.schedule.R;
import com.company.schedule.contract.MainContract;
import com.company.schedule.database.Note;
import com.company.schedule.presenter.MainPresenter;
import com.company.schedule.ui.adapters.CustomLayoutManager;
import com.company.schedule.ui.adapters.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;


public class MainActivity extends AppCompatActivity implements MainContract.View, View.OnClickListener{

    private MainContract.Presenter presenter = new MainPresenter();
    final String TAG = "myLog MainActivity";


//    TODO decoment it
//    NotesAdapter adapter;
//    ArrayList<Note> notes = new ArrayList<>();
    RecyclerView notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter.onCreate(this);

        final Toolbar toolbar =  findViewById(R.id.toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);


        FloatingActionButton fab =  findViewById(R.id.fab);  // button for jump to AddNoteActivity
        fab.setOnClickListener(this);  // setting handle

        //recyclerview that is displaying all notes
        notesList = findViewById(R.id.notesList);

        notesList.setLayoutManager(new CustomLayoutManager(this));
        notesList.setAdapter(
                presenter.getAdapter(MainActivity.this)
        );

        //load data from DB
        presenter.loadData();
        presenter.attachView(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.fab:
            presenter.onFabAddClicked(MainActivity.this);
            break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {  // getting variables from presenter
        super.startActivityForResult(intent, requestCode); // and getting this information back. (using REQUEST_CODE_ADD_NOTE (1) we can find out that the result came exactly with AddNoteActivity)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        on requestCode we determine from which subsidiary activity the result came
//        resultCode - return code. Determines whether the call has passed successfully or not.
//        data - Intent, in which the data is returned
        presenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // for notesList.setAdapter we must init adapter
    @Override
    public NotesAdapter.ItemClickListener getItemClickListener(final ArrayList<Note> notes) {
        return new NotesAdapter.ItemClickListener() {  // empty constructor
            @Override
            public void onItemClick(View view, int position) {
                //if user clicks on item of recyclerview, app goes to editnote activity
                Note toSend = notes.get(position);
                Log.v(TAG,"pos" + Integer.toString(position) + Integer.toString(notes.get(position).getId()));
                //sending all data, that is needed for editing note
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("id",toSend.getId());
                intent.putExtra("name",toSend.getName());
                intent.putExtra("content",toSend.getContent());
                intent.putExtra("frequency",toSend.getFrequency());
                intent.putExtra("date",toSend.getDate());
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
            }
        };
    }


    // for output logs
    @Override
    public void log(String log_text) {
        Log.v(TAG, log_text);
    }

    @Override
    public void logV(String log_text) {
        Log.v(TAG, log_text);
    }

    @Override
    public void logD(String log_text) {
        Log.d(TAG, log_text);
    }

    @Override
    public void logI(String log_text) {
        Log.i(TAG, log_text);
    }

    @Override
    public void logW(String log_text) {
        Log.w(TAG, log_text);
    }

    @Override
    public void logE(String log_text) {
        Log.e(TAG, log_text);
    }


    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}

