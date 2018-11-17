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


//  ================_FRAGMENT_LIFECYCLE_================
    public void onActivityCreated() {  // note to update column `done`
        view.setTimerText(String.format("fake %d:%02d", TASK_TIME_SECONDS / 60, TASK_TIME_SECONDS % 60));

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

    public void onPause() {
        /* TODO decoment
        pauseTimer();
        // save must be after pause Timer
        interactor.saveTimerState(finishTime, pauseTime);
        */
    }


    public void onResume() {
        /* TODO decoment
        // get must be before resume Timer
        finishTime = interactor.getFinishTime();
        pauseTime = interactor.getPauseTime();

        if (finishTime == 0)
            startTimer();
        else
            resumeTimer();
        */
    }




    //  ================_PRIVATE_================
    private void startTimer() {
        finishTime = System.currentTimeMillis() + TASK_TIME_SECONDS*1000;  // finishTime is time when task will finished
        view.startTimer();
        timerHandler.postDelayed(timerRunnable, 0);
        isTaskFinished = false;
    }

    private void pauseTimer() {
  //todo      pauseTime = System.currentTimeMillis();
        view.pauseTimer();
        timerHandler.removeCallbacks(timerRunnable);
    }


    private void resumeTimer() {
/*    todo    if (pauseTime != 0)
  todo          finishTime += System.currentTimeMillis() - pauseTime;  // update time when task will be finished
*/
        view.resumeTimer();
        //finishTime = System.currentTimeMillis()+5*1000;
        timerHandler.postDelayed(timerRunnable, 0);
    }


    private void stopTimer() {
//todo        pauseTime = 0;
        view.stopTimer();
        timerHandler.removeCallbacks(timerRunnable);
        view.showMessage("You finish task");
        isTaskFinished = true;
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