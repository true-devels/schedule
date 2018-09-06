package com.company.schedule.presentation.presenter;

import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.view.MainView;

import static com.company.schedule.utils.Error.handleThrowable;

public class MainPresenter {

    private MainView view;
    private MainInteractor interactor;
    // callback for Model, that call setAllNotes in MainView

    public MainPresenter(MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void loadData() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadData()
                .subscribe(
                        (notes) -> view.setAllNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }


    public void detachView() {
        this.view = null;
    }
}
