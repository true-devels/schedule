package com.company.schedule.obsolete.timerObsolete;

public class TimerPresenterObsolete {
/*
    private TimerViewObsolete view;
    private TimerInteractor interactor;


    //  timer
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private long finishTime = 0;
//    private long pauseTime = 0;
    private boolean isTaskFinished = false;

    private String NOTE_IS_NULL_ERROR = "I haven't note, so I can't save result when timer will be finished";

    private Note noteCarryOut;


    public TimerPresenterObsolete(TimerViewObsolete view, Note noteToDone, TimerInteractor interactor) {
        this.view = view;
        this.noteCarryOut = noteToDone;
        this.interactor = interactor;

        if(this.noteCarryOut == null)     view.showErrorMessage(NOTE_IS_NULL_ERROR);
        else if(this.noteCarryOut.getId() == 0)
            view.showErrorMessage("Id of note is equal 0. You must see timer only when integer id >= 1");
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
        finishTime = interactor.getFinishTime(getId());

        if (finishTime != 0)  // It means we haven't finished task
            resumeTimer();  // continue count
    }


    public void onStop() {
        interactor.saveFinishTime(getId(), finishTime);
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

        isTaskFinished = true;
        finishTime = 0L;
        interactor.saveFinishTime(getId(), finishTime);

        if (noteCarryOut != null) {
            noteCarryOut.setDone(true);  // timer was finished so task is done
            interactor.updateNoteDone(noteCarryOut)
                    .subscribe(() -> {view.showMessage("You have finished task");});  // update column `done` when timer will finished

        }
        else
            view.showErrorMessage("you have done task, but we can't save this results");
    }


    private int getId() {
        if (noteCarryOut != null)
            return noteCarryOut.getId();
        else {
            view.showErrorMessage(NOTE_IS_NULL_ERROR);
        return 0;
        }
    }
//  ================_DETACHING_================
    public void detachView() {
    //    timerHandler.removeCallbacks(timerRunnable);
        this.view = null;
    }
    */
}