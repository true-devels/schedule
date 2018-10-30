package com.company.schedule.presentation.presenter;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.view.MainView;

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


    public void detachView() {
        this.view = null;
    }
}
