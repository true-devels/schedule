package com.company.schedule.presentation.timer;

import android.os.Handler;

import static com.company.schedule.utils.Constants.PAUSE_TIMER;
import static com.company.schedule.utils.Constants.RESUME_TIMER;
import static com.company.schedule.utils.Constants.START_TIMER;
import static com.company.schedule.utils.Constants.STOP_TIMER;
import static com.company.schedule.utils.Constants.TASK_TIME_SECONDS;

public class TimerPresenter {

    private TimerView view;


    //runs without a timer by reposting this handler at the end of the runnable
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private long finishTime = 0;
    private boolean isTaskFinished = false;


    public TimerPresenter(TimerView view) {
        this.view = view;

    }

    public void onActivityCreated() {
        timerRunnable = new Runnable() {

            @Override
            public void run() {
                long millis = finishTime - System.currentTimeMillis();

                if (millis < 0) {
                    millis = 0;
                    if (!isTaskFinished) {
                        view.stopTimer();
                        timerHandler.removeCallbacks(timerRunnable);

                        view.showMessage("You finish task");
                        isTaskFinished = true;
                    }
                }

                int seconds = (int) (millis / 1000);
                millis %= 1000;
                int minutes = seconds / 60;
                seconds = seconds % 60;

                view.setTimerText(String.format("%d:%02d.%04d", minutes, seconds, millis));

                timerHandler.postDelayed(this, 10);  // delay for update timer       }
            }
        };
    }

    public void timerAction(String operation) {
        switch (operation) {
            case START_TIMER:
                view.startTimer();
                finishTime = System.currentTimeMillis() + TASK_TIME_SECONDS*1000;  // finishTime is time when task will finished
                timerHandler.postDelayed(timerRunnable, 0);
                break;

            case PAUSE_TIMER:
                view.pauseTimer();
                timerHandler.removeCallbacks(timerRunnable);
                break;

            case RESUME_TIMER:
                view.resumeTimer();
                //finishTime = System.currentTimeMillis()+5*1000;
                timerHandler.postDelayed(timerRunnable, 0);
                break;

            case STOP_TIMER:
                view.stopTimer();
                timerHandler.removeCallbacks(timerRunnable);
                break;
        }
    }


//  ================_DETACHING_================
    public void detachView() {
        this.view = null;
    }
}
