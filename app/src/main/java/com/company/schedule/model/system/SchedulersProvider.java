package com.company.schedule.model.system;

import io.reactivex.Scheduler;
public interface SchedulersProvider {
    Scheduler runOnBackground();
    Scheduler io();
    Scheduler compute();
    Scheduler ui();
    Scheduler internet();
}