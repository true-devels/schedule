package com.company.schedule.presentation.later;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.LaterInteractor;

import java.util.ArrayList;
import java.util.List;

import static com.company.schedule.utils.Error.handleThrowable;

public class LaterPresenter {
    private LaterView view;
    private LaterInteractor interactor;

    public LaterPresenter(LaterView view, LaterInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void loadDataLater() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadLaterData()
                .subscribe(
                        (notes) -> sortNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }

    public void loadDataDone() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadDoneData()
                .subscribe(
                        (notes) -> sortNotes(notes),
                        (Throwable e) -> handleThrowable(e)
                );  // load data from DB
    }

    private void sortNotes(List<Note> noteList){
        ArrayList<Note> today = new ArrayList<>();
        ArrayList<Note> week = new ArrayList<>();
        ArrayList<Note> month = new ArrayList<>();

        for(Note note: noteList){
            switch (note.getFrequency()){
                case 1:
                    today.add(note);
                    break;
                case 2:
                    week.add(note);
                    break;
                case 3:
                    month.add(note);
                    break;
            }
        }
        view.setTodayNotes(today);
        view.setWeekNotes(week);
        view.setMonthNotes(month);
    }

    public void swipedToDone(Note note){
        note.setDone(true);
        note.setLater(false);
        updateNote(note);
    }

    public void restoreFromDone(Note item){
        item.setDone(false);
        item.setLater(true);
        updateNote(item);
    }

    private void updateNote(Note noteToUpdate) {
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> loadDataLater(),
                        e -> handleThrowable(e)
                        );
    }
}
