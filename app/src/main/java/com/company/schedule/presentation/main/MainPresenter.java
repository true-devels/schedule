package com.company.schedule.presentation.main;

import android.content.Context;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.SharedPrefsRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public void onCheckedDoneChanged(Note noteCheckedOn, boolean isChecked) {
       /* if (noteCheckedOn.isDone() != isChecked) {  // we update DB only if we need change value
            noteCheckedOn.setDone(isChecked);
            interactor.updateNote(noteCheckedOn)
                    .subscribe(
                            () -> {},  // we do nothing when update finished
                            e -> handleThrowable(e)
                    );
        }*/
    }

    public void loadData() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }

    public void loadDailyData() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadDailyData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }
    public void loadWeeklyData() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadWeeklyData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }
    public void loadMonthlyData() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadMonthlyData()
                .subscribe(
                        (notes) ->  view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }

    private void deleteNote(int id, int tab) {
        switch (tab){
            case 0:
                interactor.deleteNoteById(id)
                    .subscribe(
                        () -> loadDailyData(),
                        e -> handleThrowable(e)
                );
                break;
            case 1:
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> loadWeeklyData(),
                                e -> handleThrowable(e)
                        );
                break;
            case 2:
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> loadMonthlyData(),
                                e -> handleThrowable(e)
                        );
                break;
            default:
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> loadData(),
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
                                () -> loadDailyData(),
                                e -> handleThrowable(e)
                        );
                break;
            case 1:
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> loadWeeklyData(),
                                e -> handleThrowable(e)
                        );
                break;
            case 2:
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> loadMonthlyData(),
                                e -> handleThrowable(e)
                        );
                break;
            default:
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> loadData(),
                                e -> handleThrowable(e)
                        );
                break;
        }

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
           interactor.refreshDailyData()
                .subscribe(
                        () -> {
                            loadDailyData();
                            new SharedPrefsRepository(context).setTimeLastUpdateDaily(new Date().getTime());
                        },
                        e -> handleThrowable(e)
                );
        }else{
            loadDailyData();
        }
    }

    public void refreshWeeklyData(){
        GregorianCalendar gc_now = new GregorianCalendar();
        GregorianCalendar gc_last = new GregorianCalendar();
        gc_last.setTimeInMillis(new SharedPrefsRepository(context).getTimeLastUpdateWeekly());
        if(gc_now.get(Calendar.WEEK_OF_YEAR)!=gc_last.get(Calendar.WEEK_OF_YEAR)){
            interactor.refreshWeeklyData()
                    .subscribe(
                            () -> {
                                loadWeeklyData();
                                new SharedPrefsRepository(context).setTimeLastUpdateWeekly(new Date().getTime());
                            },
                            e -> handleThrowable(e)
                    );
        }else{
            loadWeeklyData();
        }
    }

    public void refreshMonthlyData(){
        GregorianCalendar gc_now = new GregorianCalendar();
        GregorianCalendar gc_last = new GregorianCalendar();
        gc_last.setTimeInMillis(new SharedPrefsRepository(context).getTimeLastUpdateMonthly());
        if(gc_now.get(Calendar.MONTH)!=gc_last.get(Calendar.MONTH)){
            interactor.refreshMonthlyData()
                    .subscribe(
                            () -> {
                                loadMonthlyData();
                                new SharedPrefsRepository(context).setTimeLastUpdateMonthly(new Date().getTime());
                            },
                            e -> handleThrowable(e)
                    );

        }else{
            loadMonthlyData();
        }
    }

}
