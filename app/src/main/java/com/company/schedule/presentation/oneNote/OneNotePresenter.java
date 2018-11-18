package com.company.schedule.presentation.oneNote;

import android.content.Context;

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

    public void deleteNote(int id) {
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> view.goToMainActivity(),
                                e -> handleThrowable(e)
                        );

    }
    public void updateNoteLater(Note noteToUpdate) {
        noteToUpdate.setLater(1);
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> view.onLaterButtonClicked(),
                                e -> handleThrowable(e)
                        );
    }

    public void updateNoteDone(Note noteToUpdate) {
        noteToUpdate.setDone(1);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> view.onDoneButtonClicked(),
                        e -> handleThrowable(e)
                );
    }

    public void updateNoteDoneCanceled(Note noteToUpdate){
        noteToUpdate.setDone(0);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> view.onDoneCanceled(),
                        e -> handleThrowable(e)
                );
    }

    public void updateNoteLaterCanceled(Note noteToUpdate){
        noteToUpdate.setLater(0);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> view.onLaterCanceled(),
                        e -> handleThrowable(e)
                );
    }
}

