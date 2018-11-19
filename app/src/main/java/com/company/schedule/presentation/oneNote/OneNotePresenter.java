package com.company.schedule.presentation.oneNote;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.OneNoteInteractor;

import static com.company.schedule.utils.Error.handleThrowable;


public class OneNotePresenter {
    private OneNoteView view;
    private OneNoteInteractor interactor;

    public OneNotePresenter(OneNoteView view, OneNoteInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void deleteClicked(int id) {
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> view.goToMainActivity(),
                                e -> handleThrowable(e)
                        );

    }
    public void laterClicked(Note noteToUpdate) {
        noteToUpdate.setLater(true);
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> {
                                    view.setStatusLater();
                                    view.setBtnLaterInvisible();
                                },
                                e -> handleThrowable(e)
                        );
    }

    public void doneClicked(Note noteToUpdate) {
        noteToUpdate.setDone(true);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> {
                            view.setStatusDone();
                            view.setBtnDoneInvisible();
                            view.setBtnLaterInvisible();
                        },
                        e -> handleThrowable(e)
                );
    }

    public void doneCanceled(Note noteToUpdate){
        noteToUpdate.setDone(false);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> {
                            if(!noteToUpdate.isLater()) {
                                view.setStatusToBeDone();
                                view.setBtnLaterVisible();
                            } else {
                                view.setStatusLater();
                                view.setBtnLaterInvisible();
                            }

                            view.setBtnDoneVisible();

                        },
                        e -> handleThrowable(e)
                );
    }

    public void updateNoteLaterCanceled(Note noteToUpdate){
        noteToUpdate.setLater(false);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> {
                            view.setStatusToBeDone();
                            view.setBtnLaterVisible();
                        },
                        e -> handleThrowable(e)
                );
    }
}

