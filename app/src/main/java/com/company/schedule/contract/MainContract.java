package com.company.schedule.contract;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

import com.company.schedule.database.Note;
import com.company.schedule.ui.fragments.pickers.DatePickerFragment;
import com.company.schedule.ui.fragments.pickers.TimePickerFragment;

import java.util.GregorianCalendar;
import java.util.List;

public interface MainContract {

    interface MainView {  // UI,  animation

        void startActivityForResult(Intent intent, int requestCode);

        void setAllNotes(List<Note> myLinks);
        void toast(String toast_message);
        void toastLong(String toast_message);

    }
    interface MainPresenter {
        void attachView(MainView view);
        void viewHasCreated(Context context);
        void onFabAddClicked(Context context);
        void onItemClicked(Context context, Note noteToSend);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void detachView();

    }


    interface AddNoteView {
        //
        void remindMeIsChecked();
        void remindMeIsNotChecked();
        // pickers
        void showDatePicker(DatePickerFragment datePickerFragment);
        void showTimePicker(TimePickerFragment timePickerDialog);
        // setters
        void setResultOK(Intent data);
        void setResultCancel();
        // getters
        int getId();
        String getTextFromNameNote();
        String getTextFromContentNote();
        boolean getIsReminded();
        boolean getIsEdited();
        GregorianCalendar getDateNotification();
        GregorianCalendar getGcEditDate();
        Spinner getSpinnerFreq();
        Context getContext();
        AlarmManager getAlarmManager();

        void finish();
    }

    interface AddNotePresenter {
        void attachView(AddNoteView view);
        void viewHasCreated(Bundle extras);
        // click handles
        void pressedToSubmitNote();
        void pressedToEditDate();
        void pressedToEditTime();
        void pressedToFabDelete();
        //checked changed
        void changedRemindMe(boolean isChecked);

        void detachView();
    }

    interface Model {
        void initDB(Context context);
        void insertToDb(final Note note, final LoadNoteCallback callback);
        void deleteFromDb(int id, final LoadNoteCallback callback);
        void loadData(LoadNoteCallback callback);
        // callback interface
        interface LoadNoteCallback {
            void onLoadData(List<Note> myNewNotes); //load data
        }
    }
}
