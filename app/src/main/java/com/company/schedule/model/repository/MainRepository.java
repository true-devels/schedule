package com.company.schedule.model.repository;

import android.content.Context;
import android.util.Log;

import com.company.schedule.model.data.base.NoteDAO;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.callback.LoadNoteCallback;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainRepository {

    private Context context;
    private NoteDAO noteDAO;

    private CompositeDisposable compositeDisposable;

    private static final String TAG = "myLog MainRepository";


    public MainRepository(Context context, NoteDAO noteDAO) {
        this.context = context;
        this.noteDAO = noteDAO;

        compositeDisposable = new CompositeDisposable();
    }


    //method that inserts new note into DB
    public void insertNoteDisposable(final Note note, final LoadNoteCallback callback) {
        Disposable disposable_insert = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                insertNotes(note);
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
        compositeDisposable.add(disposable_insert);
    }

    public void updateNoteDisposable(final Note note, final LoadNoteCallback callback) {
        Disposable disposable_update = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                updateNotes(note);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        loadData(callback);  // we give callback for view.onGetAllLinkSuccess(myNewNotes)
                        Log.i(TAG, "Link updated!");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable_update);
    }

    //method that deletes note from DB
    public void deleteNoteDisposable(final Note noteToDel, final LoadNoteCallback callback) {
        Disposable disposable_delete = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                deleteNote(noteToDel);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.i(TAG, "Note has deleted!");
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

    public void deleteNoteById() {
        //
//        Disposable disposable_get = getOneNote(id)  // TODO why we use it instead just a repository.deleteNote(note)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer<Note>() {
//                    @Override
//                    public void accept(final Note note) throws Exception {
//
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.e(TAG, "Error: " + throwable.getMessage());
//                    }
//                });
//        compositeDisposable.add(disposable_get);
    }



    //method that gets all data from DB and update Recycler view
    public void loadData(final LoadNoteCallback callback) {
        Disposable disposable = getAllNotes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> myNewNotes) throws Exception {
                        for (Note note : myNewNotes) {
                            Log.v(TAG, "here note with id:" + note.getId());
                        }

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

    //work with DB
    private Flowable<Note> getOneNote(int id) {
        return noteDAO.getOneNote(id);
    }

    private Flowable<List<Note>> getAllNotes() {
        return noteDAO.getAllNotes();
    }

    private void insertNotes(Note... notes) {
        noteDAO.insertNotes(notes);
    }

    private void updateNotes(Note... notes) {
        noteDAO.updateNotes(notes);
    }

    private void deleteNote(Note note) {
        noteDAO.deleteNote(note);  // Note shall delete by note.id
    }

    private void deleteAllNotes() {
        noteDAO.deleteAllNotes();
    }
}
