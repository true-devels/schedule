package com.company.schedule.model.repository;

import android.util.Log;

import com.company.schedule.model.data.base.NoteDAO;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.system.SchedulersProvider;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class MainRepository {

    private NoteDAO noteDAO;

    private SchedulersProvider schedulers;  // schedulers for threads

    public MainRepository(NoteDAO noteDAO, SchedulersProvider schedulers) {
        this.noteDAO = noteDAO;
        this.schedulers = schedulers;
    }


    //method that gets all data from DB and update Recycler view
    public Observable<List<Note>> loadData() {
        return Observable.create((ObservableOnSubscribe<List<Note>>) emitter -> {
            try {
                List<Note> notes = noteDAO.getAllNotes();  // get notes
                emitter.onNext(notes);  // send notes
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    public Observable<Note> getOneNoteByIdObservable(final int id) {
        return Observable.create((ObservableOnSubscribe<Note>) emitter -> {
            try {
                Note note = noteDAO.getOneNote(id);
                emitter.onNext(note);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    //method that inserts new note into DB
    public Observable insertNote(final Note note) {
        return Observable.create(emitter -> {
            long id = noteDAO.insertNote(note);
            emitter.onNext(id);
            emitter.onComplete();
           // Log.d("last check add",note.getDate().get(Calendar.MINUTE)+"");
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }


    public Completable updateNote(final Note note) {
        return Completable.create(emitter -> {
            noteDAO.updateNotes(note);
            emitter.onComplete();
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    public Completable deleteNoteById(final int id) {
        return Completable.create(emitter -> {
            noteDAO.deleteNoteById(id);
            emitter.onComplete();
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }


    public Observable<List<Note>> loadDataDaily() {
        return Observable.create((ObservableOnSubscribe<List<Note>>) emitter -> {
            try {
                List<Note> notes = noteDAO.getAllDailyNotes();  // get notes
                emitter.onNext(notes);  // send notes
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    public Observable<List<Note>> loadWeeklyData() {
        return Observable.create((ObservableOnSubscribe<List<Note>>) emitter -> {
            try {
                List<Note> notes = noteDAO.getAllWeeklyNotes();  // get notes
                emitter.onNext(notes);  // send notes
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    public Observable<List<Note>> loadMonthlyData() {
        return Observable.create((ObservableOnSubscribe<List<Note>>) emitter -> {
            try {
                List<Note> notes = noteDAO.getAllMonthlyNotes();  // get notes
                emitter.onNext(notes);  // send notes
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    public Observable<List<Note>> loadLaterData() {
        return Observable.create((ObservableOnSubscribe<List<Note>>) emitter -> {
            try {
                List<Note> notes = noteDAO.getAllLaterNotes();  // get notes
                emitter.onNext(notes);  // send notes
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    public Observable<List<Note>> loadOnceData() {
        return Observable.create((ObservableOnSubscribe<List<Note>>) emitter -> {
            try {
                List<Note> notes = noteDAO.getAllOnceNotes();  // get notes
                emitter.onNext(notes);  // send notes
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

}
