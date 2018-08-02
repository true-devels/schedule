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

//  when we move to a new activity to get the result we need to be indexed by its number
    final int REQUEST_CODE_ADD_NOTE = 1;
    final int REQUEST_CODE_EDIT_NOTE = 2;
    final String TAG = "myLog MainActivity";
    private CompositeDisposable compositeDisposable;
    private NotifyRepository notifyRepository;
    NotesAdapter adapter;
    ArrayList<CustomNotify> notifies = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB variables
        compositeDisposable = new CompositeDisposable();
        NotifyDatabase linkDatabase = NotifyDatabase.getInstance(this);
        notifyRepository = NotifyRepository.getmInstance(NotifyDataSourceClass.getInstance(linkDatabase.notifyDAO()));

        final Toolbar toolbar =  findViewById(R.id.toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);


        FloatingActionButton fab =  findViewById(R.id.fab);  // button for jump to AddNoteActivity
        fab.setOnClickListener(this);  // setting handle

        //recyclerview that is displaying all notes
        RecyclerView notesList = findViewById(R.id.notesList);

        notesList.setLayoutManager(new CustomLayoutManager(this));
        adapter = new NotesAdapter(this, notifies);
        notesList.setAdapter(adapter);

        adapter.setClickListener(new NotesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //if user clicks on item of recyclerview, app goes to editnote activity
                CustomNotify toSend = notifies.get(position);
                Log.v(TAG,"pos" + Integer.toString(position) + Integer.toString(notifies.get(position).getId()));
                //sending all data, that is needed for editing note
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("id",toSend.getId());
                intent.putExtra("name",toSend.getName());
                intent.putExtra("content",toSend.getContent());
                intent.putExtra("frequency",toSend.getFrequency());
                intent.putExtra("date",toSend.getDate());
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
            }
        });

        //load data from DB
        loadData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.fab:
            //going to addnote activity
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
            Log.i(TAG, "RESULT_OK");
        switch (requestCode) {  // check from which object data come
        case REQUEST_CODE_ADD_NOTE:  // if adding
            String noteName = data.getStringExtra("note_name");
            //TODO make good default value
            //getting all data
            String name = data.getStringExtra("note_name");
            final String content = data.getStringExtra("note_content");
            int year = data.getIntExtra("year", 1900);
            int month = data.getIntExtra("month", 0);
            int day = data.getIntExtra("day", 0);
            int hour = data.getIntExtra("hour", 0);
            int minute = data.getIntExtra("minute", 0);
            byte prototype = (byte) data.getIntExtra("freq",0);

            //creating calendar with data, that is got from addnote activity
            GregorianCalendar not_date =  new GregorianCalendar();
            if(year==-1){
                not_date=null;
            }else{
                not_date.set(GregorianCalendar.YEAR,year);
                not_date.set(GregorianCalendar.MONTH,month);
                not_date.set(GregorianCalendar.DAY_OF_MONTH,day);
                not_date.set(GregorianCalendar.HOUR,hour);
                not_date.set(GregorianCalendar.MINUTE,minute);
            }

            //creating and inserting to DB new note
            final CustomNotify loc = new CustomNotify(name,content,not_date,prototype);
            insertToDb(loc);

            Log.v(TAG, "case REQUEST_CODE_ADD_NOTE, noteName: \"" + noteName + "\";");

            break;
        case REQUEST_CODE_EDIT_NOTE:
            //checking, if button 'delete' was pressed
            boolean isDel = data.getBooleanExtra("isDel",false);
            if(!isDel) {
                //getting all data
                final int id = data.getIntExtra("id", -1);
                final String name_ = data.getStringExtra("note_name");
                final String content_ = data.getStringExtra("note_content");
                final int year_ = data.getIntExtra("year", 1900);
                final int month_ = data.getIntExtra("month", 0);
                final int day_ = data.getIntExtra("day", 0);
                final int hour_ = data.getIntExtra("hour", 0);
                final int minute_ = data.getIntExtra("minute", 0);
                final byte prototype_ = (byte) data.getIntExtra("freq", 0);

                //creating calendar with data, that is got from editnote activity
                GregorianCalendar not__date = new GregorianCalendar();
                if (year_ == -1) {
                    not__date = null;
                } else {
                    not__date.set(GregorianCalendar.YEAR, year_);
                    not__date.set(GregorianCalendar.MONTH, month_);
                    not__date.set(GregorianCalendar.DAY_OF_MONTH, day_);
                    not__date.set(GregorianCalendar.HOUR, hour_);
                    not__date.set(GregorianCalendar.MINUTE, minute_);
                }

                //creating and inserting updated note to DB
                final CustomNotify local = new CustomNotify(name_, content_, not__date, prototype_);
                insertToDb(local);
                Log.v(TAG, "YEP" + Integer.toString(id));

                //getting and deleting old version of note from DB
                deleteFromDb(id);

            }else{
                final int id = data.getIntExtra("id", -1);
                Log.v(TAG, id + " here ");
                deleteFromDb(id);

            }

            //TODO delete next 4 lines
            if(toDel!=null){
                Log.v(TAG, " here " + toDel.getName());
            }else{
                Log.v(TAG, " here  is nothing");
            }

            break;
        default:
            Log.v(TAG, "onActivityResult in default");
        }
        } else {
            Log.e(TAG, "data == null or any different error, requestCode: \"" + requestCode + "\"; resultCode: \"" + resultCode + "\";"); //RESULT_OK: -1; RESULT_CANCELED: 0; RESULT_FIRST_USER(other user result): 1, 2, 3...
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

    //method thed gets all data from DB
    private void loadData() {

        Disposable disposable = notifyRepository.getAllNotifies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CustomNotify>>() {
                    @Override
                    public void accept(List<CustomNotify> myLinks) throws Exception {
                        onGetAllLinkSuccess(myLinks);
                        Log.v(TAG, " here loaddata is ");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "Error: "+throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }
    //method that writes all data to recyclerview
    private void onGetAllLinkSuccess(List<CustomNotify> myLinks) {

        notifies.clear();
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        notifies.addAll(myLinks);
        adapter.notifyItemRangeInserted(0,myLinks.size());

    }

    //method that inserts new note into DB
    //TODO make such methods for update
    private void insertToDb(final CustomNotify customNotify){

        Disposable disposable222 = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                notifyRepository.insertNotify(customNotify);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        loadData();
                        Log.i(TAG, "Link added!");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable222);
    }

    CustomNotify toDel;
    //method that deletes note from DB
    private void deleteFromDb(int id){
        Disposable disposable_get = notifyRepository.getOneNotify(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CustomNotify>() {
                    @Override
                    public void accept(CustomNotify customNotify) throws Exception {

                        toDel = customNotify;
                        Disposable disposable_delete = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                notifyRepository.deleteNotify(toDel);
                                emitter.onComplete();
                            }
                        })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        Log.i(TAG, "Link deleted!");
                                        loadData();

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e(TAG, "Error: " + throwable.getMessage());
                                    }
                                });
                        compositeDisposable.add(disposable_delete);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable_get);

    }

}

//class that catches bugs of recyclerview
//these bugs really can`t be fixed in another way
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

