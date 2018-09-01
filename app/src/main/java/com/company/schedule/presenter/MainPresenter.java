package com.company.schedule.presenter;

import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.view.MainView;

import timber.log.Timber;

public class MainPresenter {

    private MainView view;
    private MainInteractor interactor;
    // callback for Model, that call setAllNotes in MainView

    public MainPresenter(MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void viewHasCreated() {
        // we load data from DB in Model, and then set all notes in MainView
        loadData();
    }

    private void loadData() {
        interactor.loadData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }


    private void handleThrowable(Throwable throwable) {
        Timber.e(throwable, throwable.toString());
    }

    public void detachView() {
        this.view = null;
    }
}
