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
import com.company.schedule.view.MainView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static com.company.schedule.utils.Constants.REQUEST_CODE_ADD_NOTE;
import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;


public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private MainPresenter presenter;
    final String TAG = "myLog MainActivity";

    private NotesAdapter adapter;
    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        if (resultCode == RESULT_OK || data != null) {  // move part code to different class
            Log.i(TAG, "RESULT_OK");
            switch (requestCode) {  // check from which object data come
                case REQUEST_CODE_ADD_NOTE:  // if data come from .AddNoteActivity
                    presenter.resultFromAddNote(getNoteFromAddData(data));  // result from Add note
                    Log.v(TAG, "case REQUEST_CODE_ADD_NOTE, noteName: \"" + data.getStringExtra("note_name") + "\";");
                    break;
                case REQUEST_CODE_EDIT_NOTE:
                    //checking, if button 'delete' was pressed
                    boolean isDel = data.getBooleanExtra("isDel",false);
                    if(!isDel) {  // if note has not deleted
                        presenter.resultFromEditNote(getNoteFromEditData(data));  // we say presenter that result from Edit note
                    }else{  // if note has deleted
                        presenter.resultFromDeleteNote(getIdFromDeleteData(data));  // we say presenter that result from Delete note
                    }
                    break;
                default:
                    Log.v(TAG, "onActivityResult in default");
            }
        } else {
            Log.v(TAG, "resultCode != RESULT_OK, requestCode: \"" + requestCode + "\"; resultCode: \"" + resultCode + "\";"); //RESULT_OK: -1; RESULT_CANCELED: 0; RESULT_FIRST_USER(other user result): 1, 2, 3...
        }    }

    private Note getNoteFromAddData(Intent data) {
        //TODO make good default value
        //getting all data
        String name = data.getStringExtra("note_name");
        final String content = data.getStringExtra("note_content");
        byte freq = (byte) data.getIntExtra("freq",0);

        //creating calendar with data, that is got from addnote activity
        GregorianCalendar notify_date =  new GregorianCalendar();
        final long timeInMillis = data.getLongExtra("time_in_millis", -1L);  // TODO make it better
        if(timeInMillis == -1L) notify_date = null;
        else notify_date.setTimeInMillis(timeInMillis);
        return new Note(name, content, notify_date, freq);
    }

    private Note getNoteFromEditData(Intent data) {
        // TODO move repeated cod from REQUEST_CODE_EDIT_NOTE and REQUEST_CODE_ADD_NOTE to line below onActivityResult
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

        final Note noteFromData = new Note(name, content, notify_date, freq);  // make declaration in start func
        noteFromData.setId(id);  // important TODO id in constructor
        Log.d(TAG, noteFromData.getId() + ") " + noteFromData.getName() + "; content: " + noteFromData.getContent() + "; date: " + noteFromData.getDate() + "; frequency: " + noteFromData.getFrequency());

        return noteFromData;
    }

    private int getIdFromDeleteData(Intent data) {
        return data.getIntExtra("id", -1);
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

