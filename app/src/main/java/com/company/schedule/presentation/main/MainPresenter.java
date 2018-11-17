package com.company.schedule.presentation.main;

import android.util.Log;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.ui.fragments.MonthlyFragment;

import java.util.Calendar;
import java.util.List;

import static com.company.schedule.utils.Error.handleThrowable;

public class MainPresenter {

    private MainView view;
    private MainInteractor interactor;

    public MainPresenter(MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void onCheckedDoneChanged(Note noteCheckedOn, boolean isChecked) {
        if (noteCheckedOn.isDone() != isChecked) {  // we update DB only if we need change value
            noteCheckedOn.setDone(isChecked);
            interactor.updateNote(noteCheckedOn)
                    .subscribe(
                            () -> {},  // we do nothing when update finished
                            e -> handleThrowable(e)
                    );
        }
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


    public void detachView() {
        this.view = null;
    }

    public void swipedToDelete(int id, int tab){
        deleteNote(id, tab);
    }

}
