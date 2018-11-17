package com.company.schedule.model.interactor;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.repository.MainRepository;

import java.util.List;

import io.reactivex.Observable;

public class LaterInteractor {
    private MainRepository repository;

    public LaterInteractor(MainRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Note>> loadLaterData() {
        return repository.loadLaterData();
    }
}
