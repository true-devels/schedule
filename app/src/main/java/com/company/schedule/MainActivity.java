package com.company.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.company.schedule.Database.NotifyRepository;
import com.company.schedule.Local.NotifyDataSourceClass;
import com.company.schedule.Local.NotifyDatabase;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView testOutputNoteName;// TODO delete the testOutputNoteName
//  when we move to a new activity to get the result we need to be indexed by its number
    final int REQUEST_CODE_ADD_NOTE = 1;
    final String TAG = "myLog MainActivity";
    private CompositeDisposable compositeDisposable;
    private NotifyRepository notifyRepository;
    NotesAdapter adapter;
    ArrayList<CustomNotify> notifies = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();
        NotifyDatabase linkDatabase = NotifyDatabase.getInstance(this);
        notifyRepository = NotifyRepository.getmInstance(NotifyDataSourceClass.getInstance(linkDatabase.notifyDAO()));

        Toolbar toolbar =  findViewById(R.id.toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);

        testOutputNoteName =  findViewById(R.id.testOutputNoteName);  // test TV for test result

        FloatingActionButton fab =  findViewById(R.id.fab);  // button for jump to AddNoteActivity
        fab.setOnClickListener(this);  // setting handle

        RecyclerView recyclerView = findViewById(R.id.notesList );
        byte test = 1;

        notifies.add(new CustomNotify("Hello, works",new GregorianCalendar(),test));

        recyclerView.setLayoutManager(new CustomLayoutManager(this));
        adapter = new NotesAdapter(this, notifies);
       /// adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        notifies.remove(0);
        adapter.notifyItemRemoved(0);
        loadData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.fab:
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);  // we indicate an explicit transition to AddNoteActivity to enter the data of a note
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE); // and getting this information back. (using REQUEST_CODE_ADD_NOTE (1) we can find out that the result came exactly with AddNoteActivity)
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        on requestCode we determine from which subsidiary activity the result came
//        resultCode - return code. Determines whether the call has passed successfully or not.
//        data - Intent, in which the data is returned

        // if data was correct entered
        if (resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "RESULT_OK");
            switch (requestCode) {  // check from which daughter object data come
            case REQUEST_CODE_ADD_NOTE:  // if from AddNoteActivity
                String noteName = data.getStringExtra("note_name");
                //TODO make normal default value
                String name = data.getStringExtra("note_name");
                int year = data.getIntExtra("year", 1900);
                int month = data.getIntExtra("month", 0);
                int day = data.getIntExtra("day", 0);
                int hour = data.getIntExtra("hour", 0);
                int minute = data.getIntExtra("minute", 0);

                GregorianCalendar not_date =  new GregorianCalendar();
                not_date.set(GregorianCalendar.YEAR,year);
                not_date.set(GregorianCalendar.MONTH,month);
                not_date.set(GregorianCalendar.DAY_OF_MONTH,day);
                not_date.set(GregorianCalendar.HOUR,hour);
                not_date.set(GregorianCalendar.MINUTE,minute);

                //TODO remove prototype with real value
                byte prototype = 0;
                final CustomNotify loc = new CustomNotify(name,not_date,prototype);

                Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {


                        notifyRepository.insertNotify(loc);
                        emitter.onComplete();
                    }
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                Toast.makeText(MainActivity.this, "Link added!", Toast.LENGTH_SHORT).show();
                                //notifies.add(loc);
                                //adapter.notifyItemInserted(adapter.getItemCount()-1);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(MainActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                compositeDisposable.add(disposable);
                Log.d(TAG, "case REQUEST_CODE_ADD_NOTE, noteName: \"" + noteName + "\";");
              //  loadData();

                testOutputNoteName.setText(noteName);
                testOutputNoteName.setText(Long.toString(loc.getDate().getTimeInMillis())+" and now " + Long.toString(System.currentTimeMillis()));
                //TODO delete this line
                //TODO to DB element with name noteName
                break;
            default:
                Log.d(TAG, "onActivityResult in default");
            }
        } else {
            //TODO output error
            Log.d(TAG, "data == null or any different error, requestCode: \"" + requestCode + "\"; resultCode: \"" + resultCode + "\";"); //RESULT_OK: -1; RESULT_CANCELED: 0; RESULT_FIRST_USER(other user result): 1, 2, 3...
        }

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

    private void loadData() {

        Disposable disposable = notifyRepository.getAllNotifies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CustomNotify>>() {
                    @Override
                    public void accept(List<CustomNotify> myLinks) throws Exception {
                        onGetAllLinkSuccess(myLinks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,""+throwable.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllLinkSuccess(List<CustomNotify> myLinks) {

        notifies.clear();
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        notifies.addAll(myLinks);
        adapter.notifyItemRangeInserted(0,myLinks.size());

    }

}
class CustomLayoutManager extends LinearLayoutManager {
    CustomLayoutManager(Context context){
        super(context);
    }
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("myLog MainActivity", "Bugs of RecyclerView");
        }
    }
}

