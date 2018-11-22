package com.company.schedule.presentation.oneNote;

import android.os.Handler;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.OneNoteInteractor;

import static com.company.schedule.utils.Constants.PAUSE_TIMER;
import static com.company.schedule.utils.Constants.RESUME_TIMER;
import static com.company.schedule.utils.Constants.START_TIMER;
import static com.company.schedule.utils.Constants.STOP_TIMER;
import static com.company.schedule.utils.Constants.TASK_TIME_SECONDS;
import static com.company.schedule.utils.Error.handleThrowable;


public class OneNotePresenter {
    private OneNoteView view;
    private OneNoteInteractor interactor;

    //  timer
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private long finishTime = 0;
    //    private long pauseTime = 0;
    private boolean isTaskFinished = false;

    public OneNotePresenter(OneNoteView view, OneNoteInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void deleteClicked(int id) {
                interactor.deleteNoteById(id)
                        .subscribe(
                                () -> view.goToMainActivity(),
                                e -> handleThrowable(e)
                        );

    }
    public void laterClicked(Note noteToUpdate) {
        noteToUpdate.setLater(true);
                interactor.updateNote(noteToUpdate)
                        .subscribe(
                                () -> {
                                    view.setStatusLater();
                                    view.setBtnLaterInvisible();
                                },
                                e -> handleThrowable(e)
                        );
    }

    public void doneClicked(Note noteToUpdate) {
        noteToUpdate.setDone(true);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> {
                            view.setStatusDone();
                            view.setBtnDoneInvisible();
                            view.setBtnLaterInvisible();
                        },
                        e -> handleThrowable(e)
                );
    }

    public void doneCanceled(Note noteToUpdate){
        noteToUpdate.setDone(false);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> {
                            if(!noteToUpdate.isLater()) {
                                view.setStatusToBeDone();
                                view.setBtnLaterVisible();
                            } else {
                                view.setStatusLater();
                                view.setBtnLaterInvisible();
                            }

                            view.setBtnDoneVisible();

                        },
                        e -> handleThrowable(e)
                );
    }

    public void laterCanceled(Note noteToUpdate){
        noteToUpdate.setLater(false);
        interactor.updateNote(noteToUpdate)
                .subscribe(
                        () -> {
                            view.setStatusToBeDone();
                            view.setBtnLaterVisible();
                        },
                        e -> handleThrowable(e)
                );
    }


    //  ================_TIMER_================
    public void timerAction(String operation) {
        switch (operation) {
            case START_TIMER:
                startTimer();
                break;
            /* todo coming soon
            case PAUSE_TIMER:
                pauseTimer();
                break;

            case RESUME_TIMER:
                resumeTimer();
                break;
*/
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
                    view.setTimerText(String.format("%d:%02d", minutes, seconds));

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
        //      pauseTime = System.currentTimeMillis();
        view.setBtnTimerText(RESUME_TIMER);
        timerHandler.removeCallbacks(timerRunnable);
    }


    private void resumeTimer() {
//        if (pauseTime != 0)          finishTime += System.currentTimeMillis() - pauseTime;  // update time when task will be finished
        view.setBtnTimerText(STOP_TIMER); //  TODO PAUSE_TIMER
        timerHandler.postDelayed(timerRunnable, 0);
    }


    private void stopTimer() {
//        pauseTime = 0;
        view.setBtnTimerText(START_TIMER);
        timerHandler.removeCallbacks(timerRunnable);

        isTaskFinished = true;
        finishTime = 0L;
        interactor.saveFinishTime(getId(), finishTime);

        if (view.getNote() != null) {
            view.getNote().setDone(true);  // timer was finished so task is done

            interactor.updateNoteDone(view.getNote())
                    .subscribe(() -> view.showMessage("You have finished task"));  // update column `done` when timer will finished
            view.setStatusDone();
            view.setBtnDoneInvisible();
            view.setBtnLaterInvisible();
        }
        else
            view.showErrorMessage("you have done task, but we can't save this results");
    }


    private int getId() {
        return view.getNote().getId();
    }
    //  ================_DETACHING_================
    public void detachView() {
        timerHandler.removeCallbacks(timerRunnable);
        this.view = null;
    }
}

