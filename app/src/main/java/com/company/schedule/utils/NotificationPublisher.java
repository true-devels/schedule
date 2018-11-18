package com.company.schedule.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MyNoteInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.model.system.MyNotification;

import java.util.Calendar;

public  class NotificationPublisher extends BroadcastReceiver {
    Note noteToShow;
    @Override
    public void onReceive(Context context, Intent intent) {


    //    Notification notification = intent.getParcelableExtra(Constants.NOTIFICATION);
        int id = intent.getIntExtra(Constants.NOTIFICATION_ID, 0);

        Log.v("Publ",""+id);
       // notificationManager.notify(id, notification);

        MyNoteInteractor interactor = new MyNoteInteractor(new MainRepository(
                AppDatabase.getDatabase(context).noteDAO(),
                new AppSchedulers()  // for threads
        ));  // create repository and get DAO);

        interactor.getOneNoteByIdObservable(id).subscribe(
                (note) -> noteToShow = note,
                (e) -> e.printStackTrace(),
                () -> show(noteToShow, context, id)
        );

    }
    private void show(Note noteToShow,Context context, int id){
        MyNotification myNotification = new MyNotification(context);
        if(noteToShow.getDone()!=1 & noteToShow.getLater()!=1){

            noteToShow.getDate().set(Calendar.SECOND,0);
            noteToShow.getDate().set(Calendar.MILLISECOND,0);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification local = myNotification.getNotification(noteToShow.getName(), noteToShow.getContent());
            notificationManager.notify(id, local);



            myNotification.scheduleNotification(local,
                    id,
                    noteToShow
            );
        }
    }
}
