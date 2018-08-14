package com.company.schedule.model.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppSchedulers implements SchedulersProvider {

    private ExecutorService backgroundExecutor = Executors.newCachedThreadPool();
    private Scheduler BACKGROUND_SCHEDULERS = Schedulers.from(backgroundExecutor);
    private ExecutorService internetExecutor = Executors.newCachedThreadPool();
    private Scheduler INTERNET_SCHEDULERS  = Schedulers.from(internetExecutor);

    @Override
    public Scheduler runOnBackground() {
        return BACKGROUND_SCHEDULERS;
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler compute() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler internet() {
        return INTERNET_SCHEDULERS;
    }
}
