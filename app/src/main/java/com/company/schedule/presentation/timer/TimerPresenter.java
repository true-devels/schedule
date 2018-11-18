package com.company.schedule.presentation.timer;

import android.os.Handler;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.TimerInteractor;

import static com.company.schedule.utils.Constants.PAUSE_TIMER;
import static com.company.schedule.utils.Constants.RESUME_TIMER;
import static com.company.schedule.utils.Constants.START_TIMER;
import static com.company.schedule.utils.Constants.STOP_TIMER;
import static com.company.schedule.utils.Constants.TASK_TIME_SECONDS;

public class TimerPresenter {

    private TimerView view;
    private TimerInteractor interactor;


    //  timer
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private long finishTime = 0;
//    private long pauseTime = 0;
    private boolean isTaskFinished = false;

    private Note noteCarryOut;


    public TimerPresenter(TimerView view, Note noteToDone, TimerInteractor interactor) {
        this.view = view;
        this.noteCarryOut = noteToDone;
        this.interactor = interactor;

        if(this.noteCarryOut.getId() == 0)
        {
            view.showErrorMessage("Id of note is equal 0. You must see timer only when integer id >= 1");
        }
    }

    public void timerAction(String operation) {
        switch (operation) {
            case START_TIMER:
                startTimer();
                break;

            case PAUSE_TIMER:
                pauseTimer();
                break;

            case RESUME_TIMER:
                resumeTimer();
                break;

            case STOP_TIMER:
                stopTimer();
                break;
        }
    }


//  ================_LIFECYCLE_================
    public void onActivityCreated() {  // note to update column `done`
        view.setTimerText(String.format("%d:%02d", TASK_TIME_SECONDS / 60, TASK_TIME_SECONDS % 60));

        timerRunnable = new Runnable() {

            @Override
            public void run() {
                long millis = finishTime - System.currentTimeMillis();

                if (millis < 0) {
                    millis = 0;
                    if (!isTaskFinished) {
                        stopTimer();
                    }
                }

                int seconds = (int) (millis / 1000);
                millis %= 1000;
                int minutes = seconds / 60;
                seconds = seconds % 60;

                if(view != null)
                    view.setTimerText(String.format("%d:%02d.%03d", minutes, seconds, millis));

                timerHandler.postDelayed(this, 10);  // delay for update timer       }
            }
        };
    }

    public void onStart() {
        finishTime = interactor.getFinishTime(noteCarryOut.getId());

        if (finishTime != 0)  // It means we haven't finished task
            resumeTimer();  // continue count
    }


    public void onStop() {
        interactor.saveFinishTime(noteCarryOut.getId(), finishTime);
    }


    //  ================_PRIVATE_================
    private void startTimer() {
        finishTime = System.currentTimeMillis() + TASK_TIME_SECONDS*1000;  // finishTime is time when task will finished
        isTaskFinished = false;

        resumeTimer();
    }

    private void pauseTimer() {
  //todo      pauseTime = System.currentTimeMillis();
        view.setBtnTimerText(RESUME_TIMER);
        timerHandler.removeCallbacks(timerRunnable);
    }


    private void resumeTimer() {
//    todo    if (pauseTime != 0)          finishTime += System.currentTimeMillis() - pauseTime;  // update time when task will be finished
        view.setBtnTimerText(STOP_TIMER); //  TODO PAUSE_TIMER
        timerHandler.postDelayed(timerRunnable, 0);
    }


    private void stopTimer() {
//todo        pauseTime = 0;
        view.setBtnTimerText(START_TIMER);  // TODO null pointer exception
        timerHandler.removeCallbacks(timerRunnable);
        view.showMessage("You finish task");
        isTaskFinished = true;
        finishTime = 0L;
        interactor.saveFinishTime(noteCarryOut.getId(), finishTime);

        if (noteCarryOut != null) {
            noteCarryOut.setDone(true);  // timer was finished so task is done
            interactor.updateNoteDone(noteCarryOut)
                    .subscribe();  // update column `done` when timer will finished
        }
        else
            view.showErrorMessage("you have done task, but we can't save this results");
    }

//  ================_DETACHING_================
    public void detachView() {
    //    timerHandler.removeCallbacks(timerRunnable);
        this.view = null;
    }
}