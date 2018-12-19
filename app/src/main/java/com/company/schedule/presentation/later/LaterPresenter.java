package com.company.schedule.presentation.later;

import android.content.Context;
import android.content.SharedPreferences;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.LaterInteractor;
import com.company.schedule.model.repository.SharedPrefsRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.company.schedule.utils.Error.handleThrowable;

public class LaterPresenter {
    private LaterView view;
    private LaterInteractor interactor;
    private SharedPrefsRepository sharedPrefsRepository;

    public LaterPresenter(LaterView view, LaterInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;  // init interactor
        sharedPrefsRepository = new SharedPrefsRepository(context);
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
        ArrayList<String> list = sharedPrefsRepository.getStatistics();
        int done_task = Integer.valueOf(list.get(new GregorianCalendar().get(Calendar.DAY_OF_WEEK)-2));
        done_task++;
        sharedPrefsRepository.updateStatistics(new GregorianCalendar().get(Calendar.DAY_OF_WEEK),done_task);
        note.setDone(true);
        note.setLater(false);
        updateNote(note);
    }

    public void restoreFromDone(Note item){
        ArrayList<String> list = sharedPrefsRepository.getStatistics();
        int done_task = Integer.valueOf(list.get(new GregorianCalendar().get(Calendar.DAY_OF_WEEK)-2));
        done_task--;
        sharedPrefsRepository.updateStatistics(new GregorianCalendar().get(Calendar.DAY_OF_WEEK),done_task);
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
