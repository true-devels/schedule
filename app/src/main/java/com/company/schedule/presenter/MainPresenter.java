package com.company.schedule.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.ui.activities.AddNoteActivity;
import com.company.schedule.view.MainView;

import java.util.GregorianCalendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.company.schedule.utils.Constants.REQUEST_CODE_ADD_NOTE;
import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;

public class MainPresenter {

    private MainView view;
    private MainInteractor interactor;
    // callback for Model, that call setAllNotes in MainView

    private final String TAG = "myLog MainMainPresenter";

    public MainPresenter(MainView view, MainInteractor interactor) {
        this.view = view;
        this.interactor = interactor;  // init interactor
    }

    public void viewHasCreated() {
        // we load data from DB in Model, and then set all notes in MainView
        interactor.loadData();  // load data from DB
    }

    public void onFabAddClicked(Context context) {
        //going to .AddNoteActivity for ADD new note
        Intent intent = new Intent(context, AddNoteActivity.class);  // we indicate an explicit transition to AddNoteActivity to enter the data of a note
        view.startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
    }

    public void onItemClicked(Context context, Note noteToSend) {
        //if user clicks on item of recyclerview, app goes to AddNoteActivity but with requestCode (...)_EDIT_NOTE
        //sending all data, that is needed for editing note
        Intent intent = new Intent(context, AddNoteActivity.class);
        intent.putExtra("id", noteToSend.getId());
        intent.putExtra("name", noteToSend.getName());
        intent.putExtra("content", noteToSend.getContent());
        intent.putExtra("frequency", noteToSend.getFrequency());
        intent.putExtra("date", noteToSend.getDate());
        view.startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);  // going to .AddNoteActivity for EDIT note
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if data was correct entered
        if (resultCode == RESULT_OK && data != null) {  // move part code to different class
            Log.i(TAG, "RESULT_OK");
            switch (requestCode) {  // check from which object data come
            case REQUEST_CODE_ADD_NOTE:  // if data come from .AddNoteActivity
                resultFromAddNote(data);
                Log.v(TAG, "case REQUEST_CODE_ADD_NOTE, noteName: \"" + data.getStringExtra("note_name") + "\";");
                break;
            case REQUEST_CODE_EDIT_NOTE:
                //checking, if button 'delete' was pressed
                boolean isDel = data.getBooleanExtra("isDel",false);
                if(!isDel) {  // if note has not deleted
                    resultFromEditNote(data);
                }else{  // if note has deleted
                    resultFromDeleteNote(data.getIntExtra("id", -1));
                }
                break;
            default:
                Log.v(TAG, "onActivityResult in default");
            }
        } else {
            Log.v(TAG, "data == null or resultCode != RESULT_OK, requestCode: \"" + requestCode + "\"; resultCode: \"" + resultCode + "\";"); //RESULT_OK: -1; RESULT_CANCELED: 0; RESULT_FIRST_USER(other user result): 1, 2, 3...
        }

    }

    private void resultFromDeleteNote(final int id) {
        Note note = new Note(id);  // create object for delete TODO make it better
        interactor.deleteNote(note);
    }

    public void detachView() {
        this.view = null;
    }

    // Misha mey be below functions must be in Model?
    private void resultFromAddNote(Intent data) {
        //TODO make good default value
        //getting all data
        String name = data.getStringExtra("note_name");
        final String content = data.getStringExtra("note_content");
        byte freq = (byte) data.getIntExtra("freq",0);

        //creating calendar with data, that is got from addnote activity
        GregorianCalendar notify_date =  new GregorianCalendar();
        final long timeInMillis = data.getLongExtra("time_in_millis", -1);  // TODO make it better
        if(timeInMillis==-1) notify_date = null;
        else notify_date.setTimeInMillis(timeInMillis);

        //creating and inserting to DB new note
        final Note local = new Note(name, content, notify_date, freq);
        interactor.insertNote(local);

    }

    private void resultFromEditNote(Intent data) {
        //getting all data
        final int id = data.getIntExtra("id", -1);  // TODO move repeated cod from REQUEST_CODE_EDIT_NOTE and REQUEST_CODE_ADD_NOTE to line below onActivityResult
        final String name = data.getStringExtra("note_name");
        final String content = data.getStringExtra("note_content");
        byte freq = (byte) data.getIntExtra("freq",0);

        //creating calendar with data, that is got from editnote activity
        GregorianCalendar notify_date = new GregorianCalendar();
        final long timeInMillis = data.getLongExtra("time_in_millis", -1);
        if (timeInMillis == -1) notify_date = null;
        else notify_date.setTimeInMillis(timeInMillis);

        final Note local = new Note(name, content, notify_date, freq);  // make declaration in start func
        local.setId(id);  // important
        Log.d(TAG, local.getId() + ") " + local.getName() + "; content: " + local.getContent() + "; date: " + local.getDate() + "; frequency: " + local.getFrequency());
        interactor.updateNote(local);
    }
}
