package com.company.schedule.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presenter.MainPresenter;
import com.company.schedule.ui.adapters.CustomLayoutManager;
import com.company.schedule.ui.adapters.NotesAdapter;
import com.company.schedule.utils.SharedPrefs;
import com.company.schedule.view.MainView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static com.company.schedule.utils.Constants.REQUEST_CODE_ADD_NOTE;
import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;


public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private MainPresenter presenter;

    private NotesAdapter adapter;
    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        if(sharedPrefs.loadNightModeState()) {
            //dark
            setTheme(R.style.darktheme);
        }else {
            //white
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(MainActivity.this,  // init view in presenter
                new MainInteractor(  // create interactor
                        new MainRepository(
                                AppDatabase.getDatabase(this).noteDAO(),
                                new AppSchedulers()  // for threads
                                )  // create repository and get DAO
                )
        );

        // init adapter for notesList
        adapter = new NotesAdapter(this, notes);
        adapter.setClickListener((view, position) -> {
            Note noteToSend = notes.get(position);
            //if user clicks on item of recyclerview, app goes to AddNoteActivity but with requestCode (...)_EDIT_NOTE
            //sending all data, that is needed for editing note
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            // TODO replace on Serializable or Parcelable
            intent.putExtra("id", noteToSend.getId());
            intent.putExtra("name", noteToSend.getName());
            intent.putExtra("content", noteToSend.getContent());
            intent.putExtra("frequency", noteToSend.getFrequency());
            intent.putExtra("date", noteToSend.getDate());

            startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);  // going to .AddNoteActivity for EDIT note
        });

        //recyclerview that is displaying all notes
        RecyclerView notesList = findViewById(R.id.notesList);

        notesList.setLayoutManager(new CustomLayoutManager(this));
        notesList.setAdapter(adapter);

        FloatingActionButton fab =  findViewById(R.id.fab);  // button for jump to AddNoteActivity
        fab.setOnClickListener(this);  // setting handle

        final Toolbar toolbar =  findViewById(R.id.toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);


        // we say the MainPresenter that the MainView are almost created
        presenter.viewHasCreated();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.fab:
            //going to .AddNoteActivity for ADD new note
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);  // we indicate an explicit transition to AddNoteActivity to enter the data of a note
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
            break;
        }
    }

    //method that writes all data to recyclerview
    public void setAllNotes(List<Note> newNotes) {
        notes.clear();
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        notes.addAll(newNotes);
        adapter.notifyItemRangeInserted(0,newNotes.size());

    }

    public void setNote(Note newNote) {
        // TODO make something with new Note
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        on requestCode we determine from which subsidiary activity the result came
//        resultCode - return code. Determines whether the call has passed successfully or not.
//        data - Intent, in which the data is returned
//        if data was correct entered
        presenter.onActivityResult(requestCode,
                resultCode,
                getNoteFromIntent(data),
                data != null && data.getBooleanExtra("isDel",false)
        );
    }


    private Note getNoteFromIntent(Intent data) {
        if (data == null) return null;  // it's not a bug
        //getting all data
        final int id = data.getIntExtra("id", -1);
        final String name = data.getStringExtra("note_name");
        final String content = data.getStringExtra("note_content");
        byte freq = (byte) data.getIntExtra("freq",0);

        //creating calendar with data, that is got from editnote activity
        GregorianCalendar notify_date = new GregorianCalendar();
        final long timeInMillis = data.getLongExtra("time_in_millis", -1L);

        if (timeInMillis == -1L) notify_date = null;
        else notify_date.setTimeInMillis(timeInMillis);

        return new Note(id, name, content, notify_date, freq);
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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}

