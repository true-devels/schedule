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
import com.company.schedule.presenter.MainPresenter;
import com.company.schedule.ui.adapters.CustomLayoutManager;
import com.company.schedule.ui.adapters.NotesAdapter;
import com.company.schedule.view.MainView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private MainPresenter presenter;
    final String TAG = "myLog MainActivity";

    NotesAdapter adapter;
    ArrayList<Note> notes = new ArrayList<>();
    RecyclerView notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(MainActivity.this,  // init view in presenter
                new MainInteractor(  // create interactor
                        new MainRepository(this,
                                AppDatabase.getDatabase(this).noteDAO()
                        )  // create repository with context and DAO
                )
        );

        // init adapter for notesList
        adapter = new NotesAdapter(this, notes);
        adapter.setClickListener(new NotesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.onItemClicked(MainActivity.this, notes.get(position));
            }
        });

        //recyclerview that is displaying all notes
        notesList = findViewById(R.id.notesList);

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
            presenter.onFabAddClicked(MainActivity.this);
            break;
        }
    }

    //method that writes all data to recyclerview
    @Override
    public void setAllNotes(List<Note> myNewNotes) {
        notes.clear();
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        notes.addAll(myNewNotes);
        adapter.notifyItemRangeInserted(0,myNewNotes.size());

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

