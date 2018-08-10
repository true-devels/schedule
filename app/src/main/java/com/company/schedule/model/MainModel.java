package com.company.schedule.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.company.schedule.contract.MainContract;
import com.company.schedule.database.AppDatabase;
import com.company.schedule.database.Note;
import com.company.schedule.database.data_source.NoteDataSourceClass;
import com.company.schedule.database.data_source.NoteRepository;
import com.company.schedule.ui.activities.AddNoteActivity;
import com.company.schedule.ui.activities.MainActivity;
import com.company.schedule.ui.adapters.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainModel implements MainContract.Model {

    private static final String TAG = "myLog MainModel";

    private CompositeDisposable compositeDisposable;
    private NoteRepository noteRepository;


    NotesAdapter adapter;
    ArrayList<Note> notes = new ArrayList<>();


    @Override
    public ArrayList<Note> getNotes() {
        return notes;
    }

    @Override
    public void initDB(Context context) {
        //DB variables
        compositeDisposable = new CompositeDisposable();
        AppDatabase linkDatabase = AppDatabase.getInstance(context);
        noteRepository = NoteRepository.getmInstance(NoteDataSourceClass.getInstance(linkDatabase.notifyDAO()));


    }

    @Override
    public void setAdapter(final Context context, NotesAdapter.ItemClickListener itemClickListener) {
        adapter = new NotesAdapter(context, notes);
        adapter.setClickListener(itemClickListener);
    }

    @Override
    public NotesAdapter getAdapter(){
        return adapter;
    }


    //method that inserts new note into DB
    //TODO make such methods for update
    @Override
    public void insertToDb(final Note note) {
        Disposable disposable222 = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                noteRepository.insertNotify(note);
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

    Note toDel;
    //method that deletes note from DB
    @Override
    public void deleteFromDb(int id) {

        Disposable disposable_get = noteRepository.getOneNotify(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Note>() {
                    @Override
                    public void accept(Note note) throws Exception {

                        toDel = note;
                        Disposable disposable_delete = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                noteRepository.deleteNotify(toDel);
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

    //method that gets all data from DB
    public void loadData() {
        Disposable disposable = noteRepository.getAllNotifies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> myLinks) throws Exception {
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
    private void onGetAllLinkSuccess(List<Note> myLinks) {
        notes.clear();
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        notes.addAll(myLinks);
        adapter.notifyItemRangeInserted(0,myLinks.size());

    }

}
