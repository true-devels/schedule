package com.company.schedule.model;

import android.content.Context;
import android.util.Log;

import com.company.schedule.contract.MainContract;
import com.company.schedule.database.AppDatabase;
import com.company.schedule.database.Note;
import com.company.schedule.database.data_source.NoteDataSourceClass;
import com.company.schedule.database.data_source.NoteRepository;

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

    @Override
    public void initDB(Context context) {
        //DB variables
        compositeDisposable = new CompositeDisposable();
        AppDatabase linkDatabase = AppDatabase.getInstance(context);
        noteRepository = NoteRepository.getmInstance(NoteDataSourceClass.getInstance(linkDatabase.notifyDAO()));
    }


    //method that inserts new note into DB
    //TODO make such methods for update
    @Override
    public void insertToDb(final Note note, final MainContract.Model.LoadNoteCallback callback) {
        Disposable disposable222 = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                noteRepository.insertNote(note);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        loadData(callback);  // we give callback for view.onGetAllLinkSuccess(myNewNotes)
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
    public void deleteFromDb(int id, final MainContract.Model.LoadNoteCallback callback) {
        Disposable disposable_get = noteRepository.getOneNote(id)  // TODO why we use it instead just a noteRepository.deleteNote(note)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Note>() {
                    @Override
                    public void accept(Note note) throws Exception {

                        toDel = note;
                        Disposable disposable_delete = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                noteRepository.deleteNote(toDel);
                                emitter.onComplete();
                            }
                        })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        Log.i(TAG, "Link deleted!");
                                        loadData(callback);  // we give callback for view.onGetAllLinkSuccess(myNewNotes)

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
    public void loadData(final MainContract.Model.LoadNoteCallback callback) {
        Disposable disposable = noteRepository.getAllNotes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> myNewNotes) throws Exception {
                        Log.v(TAG, " here loaddata is");

                        if (callback != null) {
                            callback.onLoadData(myNewNotes);
                        } else {
                            Log.e(TAG, "callback is empty");
                            // TODO throw
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "Error: "+throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }


}
