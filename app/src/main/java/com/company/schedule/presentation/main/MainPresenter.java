package com.company.schedule.presentation.main;

import android.annotation.SuppressLint;
import android.content.Context;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.SharedPrefsRepository;
import com.company.schedule.ui.main.fragments.DailyFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.company.schedule.utils.Error.handleThrowable;

public class MainPresenter {

    private MainView view;
    private MainInteractor interactor;
    private Context context;

    public MainPresenter(MainView view, MainInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;  // init interactor
        this.context = context;
    }



    public void detachView() {
        this.view = null;
    }

    public void swipedToLater(Note note, int tab){
        note.setLater(true);
        updateNote(note, tab);

    }

    public void restoreFromLater(Note item, int tab){
        item.setLater(false);
        updateNote(item, tab);
    }

    public void swipedToDone(Note note, int tab){
        note.setDone(true);
        updateNote(note, tab);
    }

    public void restoreFromDone(Note item, int tab){
        item.setDone(false);
        updateNote(item, tab);
    }

    public void refreshDailyData(){
        GregorianCalendar gc_now = new GregorianCalendar();
        GregorianCalendar gc_last = new GregorianCalendar();
        gc_last.setTimeInMillis(new SharedPrefsRepository(context).getTimeLastUpdateDaily());
        if(gc_now.get(Calendar.DAY_OF_YEAR)!=gc_last.get(Calendar.DAY_OF_YEAR)){
           interactor.refreshDailyNotes()
                .subscribe(
                        () -> {
                            loadAllDailyNotes();
                            new SharedPrefsRepository(context).setTimeLastUpdateDaily(new Date().getTime());
                            checkDoneNote();
                        },
                        e -> handleThrowable(e)
                );
        }else{
            loadAllDailyNotes();
        }
    }

    public void refreshWeeklyData(){
        GregorianCalendar gc_now = new GregorianCalendar();
        GregorianCalendar gc_last = new GregorianCalendar();
        gc_last.setTimeInMillis(new SharedPrefsRepository(context).getTimeLastUpdateWeekly());
        if(gc_now.get(Calendar.WEEK_OF_YEAR)!=gc_last.get(Calendar.WEEK_OF_YEAR)){
            interactor.refreshWeeklyNotes()
                    .subscribe(
                            () -> {
                                loadAllWeeklyNotes();
                                new SharedPrefsRepository(context).setTimeLastUpdateWeekly(new Date().getTime());
                                checkDoneNote();
                            },
                            e -> handleThrowable(e)
                    );
        }else{
            loadAllWeeklyNotes();
        }
    }

    public void refreshMonthlyData(){
        GregorianCalendar gc_now = new GregorianCalendar();
        GregorianCalendar gc_last = new GregorianCalendar();
        gc_last.setTimeInMillis(new SharedPrefsRepository(context).getTimeLastUpdateMonthly());
        if(gc_now.get(Calendar.MONTH)!=gc_last.get(Calendar.MONTH)){
            interactor.refreshMonthlyNotes()
                    .subscribe(
                            () -> {
                                loadAllMonthlyNotes();
                                new SharedPrefsRepository(context).setTimeLastUpdateMonthly(new Date().getTime());
                                checkDoneNote();
                            },
                            e -> handleThrowable(e)
                    );

        }else{
            loadAllMonthlyNotes();
        }
    }


    public void checkDoneNote() {
        // we load all notes and check their done
        interactor.getAllNotes()
                .subscribe(
                        (notes) -> ((DailyFragment)view).checkDone(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }

    @SuppressLint("CheckResult")
    private void loadAllDailyNotes() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.getAllDailyNotes()
                .subscribe(
                        (List<Note> notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e),
                        () -> checkDoneNote()
                );  // load data from DB
    }

    private void loadAllWeeklyNotes() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.getAllWeeklyNotes()
                .subscribe(
                        (List<Note> notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e),
                        () -> checkDoneNote()
                );  // load data from DB
    }

    private void loadAllMonthlyNotes() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.getAllMonthlyNotes()
                .subscribe(
                        (notes) ->  view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e),
                        () -> checkDoneNote()
                );  // load data from DB
    }

    private void deleteNote(int id, int tab) {
        switch (tab){
            case 0:
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> loadAllDailyNotes(),
                                e -> handleThrowable(e)
                        );
                break;
            case 1:
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> loadAllWeeklyNotes(),
                                e -> handleThrowable(e)
                        );
                break;
            case 2:
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> loadAllMonthlyNotes(),
                                e -> handleThrowable(e)
                        );
                break;
            default:
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> checkDoneNote(),
                                e -> handleThrowable(e)
                        );
                break;
        }
    }
    private void updateNote(Note noteToUpdate,int tab) {
        switch (tab){
            case 0:
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> loadAllDailyNotes(),
                                e -> handleThrowable(e)
                        );
                break;
            case 1:
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> loadAllWeeklyNotes(),
                                e -> handleThrowable(e)
                        );
                break;
            case 2:
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> loadAllMonthlyNotes(),
                                e -> handleThrowable(e)
                        );
                break;
            default:
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> checkDoneNote(),
                                e -> handleThrowable(e)
                        );
                break;
        }

    }
}
