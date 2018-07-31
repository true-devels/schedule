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

        compositeDisposable = new CompositeDisposable();
        NotifyDatabase linkDatabase = NotifyDatabase.getInstance(this);
        notifyRepository = NotifyRepository.getmInstance(NotifyDataSourceClass.getInstance(linkDatabase.notifyDAO()));

        final Toolbar toolbar =  findViewById(R.id.toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);

        testOutputNoteName =  findViewById(R.id.testOutputNoteName);  // test TV for test result

        FloatingActionButton fab =  findViewById(R.id.fab);  // button for jump to AddNoteActivity
        fab.setOnClickListener(this);  // setting handle

        RecyclerView recyclerView = findViewById(R.id.notesList );
        byte test = 1;

        notifies.add(new CustomNotify("Hello, works","Simple content" ,new GregorianCalendar(),test));

        recyclerView.setLayoutManager(new CustomLayoutManager(this));
        adapter = new NotesAdapter(this, notifies);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new NotesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                CustomNotify toSend = notifies.get(position);
                Toast.makeText(getApplicationContext(),"pos" + Integer.toString(position),Toast.LENGTH_LONG).show();
                testOutputNoteName.setText("pos" + Integer.toString(position) + Integer.toString(notifies.get(position).getId()));

                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("id",toSend.getId());
                intent.putExtra("name",toSend.getName());
                intent.putExtra("content",toSend.getContent());
                intent.putExtra("frequency",toSend.getFrequency());
                intent.putExtra("date",toSend.getDate());
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
            }
        });
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

    CustomNotify toDel;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        on requestCode we determine from which subsidiary activity the result came
//        resultCode - return code. Determines whether the call has passed successfully or not.
//        data - Intent, in which the data is returned

        // if data was correct entered
        if (resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "RESULT_OK");
            switch (requestCode) {  // check from which object data come
            case REQUEST_CODE_ADD_NOTE:  // if adding
                String noteName = data.getStringExtra("note_name");
                //TODO make normal default value
                String name = data.getStringExtra("note_name");
                final String content = data.getStringExtra("note_content");
                int year = data.getIntExtra("year", 1900);
                int month = data.getIntExtra("month", 0);
                int day = data.getIntExtra("day", 0);
                int hour = data.getIntExtra("hour", 0);
                int minute = data.getIntExtra("minute", 0);
                byte prototype = (byte) data.getIntExtra("freq",0);

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

                final CustomNotify loc = new CustomNotify(name,content,not_date,prototype);
                insertToDb(loc);

                Log.d(TAG, "case REQUEST_CODE_ADD_NOTE, noteName: \"" + noteName + "\";");
              //  loadData();

                testOutputNoteName.setText(noteName);
             //   if(loc.getDate()!=null){
               // testOutputNoteName.setText(Long.toString(loc.getDate().getTimeInMillis())+" and now " + Long.toString(System.currentTimeMillis())+ " freq " + " byte " + Byte.toString(prototype));
                //}
                break;
                case REQUEST_CODE_EDIT_NOTE:
                    final int id=  data.getIntExtra("id",-1);
                    final String name_ = data.getStringExtra("note_name");
                    final String content_ = data.getStringExtra("note_content");
                    final int year_ = data.getIntExtra("year", 1900);
                    final int month_ = data.getIntExtra("month", 0);
                    final int day_ = data.getIntExtra("day", 0);
                    final int hour_ = data.getIntExtra("hour", 0);
                    final int minute_ = data.getIntExtra("minute", 0);
                    final byte prototype_ = (byte) data.getIntExtra("freq",0);
                    GregorianCalendar not__date = new GregorianCalendar();
                    if(year_==-1){
                        not__date=null;
                    }else{
                        not__date.set(GregorianCalendar.YEAR,year_);
                        not__date.set(GregorianCalendar.MONTH,month_);
                        not__date.set(GregorianCalendar.DAY_OF_MONTH,day_);
                        not__date.set(GregorianCalendar.HOUR,hour_);
                        not__date.set(GregorianCalendar.MINUTE,minute_);
                    }
                    final CustomNotify local = new CustomNotify(name_,content_,not__date,prototype_);
                    insertToDb(local);
                    Toast.makeText(this,"YEP" + Integer.toString(id),Toast.LENGTH_LONG).show();

                    Disposable disposablex = notifyRepository.getOneNotify(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<CustomNotify>() {
                                @Override
                                public void accept(CustomNotify customNotify) throws Exception {

                                    toDel = customNotify;
                                    Disposable disposablex2 = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
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
                                                    Log.d(TAG, " here works ");
                                                    Toast.makeText(MainActivity.this, "Link deleted!", Toast.LENGTH_SHORT).show();
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    Toast.makeText(MainActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, " here error " + throwable.getMessage());
                                                }
                                            });
                                    compositeDisposable.add(disposablex2);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(MainActivity.this,""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, " here error " + throwable.getMessage());
                                }
                            });
                    compositeDisposable.add(disposablex);

                    if(toDel!=null){
                        Log.d(TAG, " here " + toDel.getName());
                    }else{
                        Log.d(TAG, " here  is nothing");
                    }


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
                        Log.d(TAG, " here loaddata is ");
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
                        Toast.makeText(MainActivity.this, "Link added!", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable222);
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

