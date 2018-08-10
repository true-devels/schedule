package com.company.schedule.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.company.schedule.contract.MainContract;
import com.company.schedule.database.Note;
import com.company.schedule.model.MainModel;
import com.company.schedule.ui.activities.AddNoteActivity;
import com.company.schedule.ui.adapters.NotesAdapter;

import java.util.GregorianCalendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.company.schedule.utils.Constants.REQUEST_CODE_ADD_NOTE;
import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.Model model = new MainModel();
    // callback for Model, that call setAllNotes in View
    private MainContract.Model.LoadNoteCallback callbackLoadDataFinish = new MainContract.Model.LoadNoteCallback() {
        @Override
        public void onLoadData(List<Note> myNewNotes) {
            loadDataFinish(myNewNotes);
        }
    };

    @Override
    public void viewHasCreated(Context context) {
        model.initDB(context);
        // we load data from DB in Model, and then set all notes in View
        model.loadData(new MainContract.Model.LoadNoteCallback() {
            @Override
            public void onLoadData(List<Note> myNewNotes) {
                view.setAllNotes(myNewNotes);  // what we do when load data finish
            }
        });  // load data from DB
    }

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onFabAddClicked(Context context) {
        //going to addnote activity
        Intent intent = new Intent(context, AddNoteActivity.class);  // we indicate an explicit transition to AddNoteActivity to enter the data of a note
        view.startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if data was correct entered
        if (resultCode == RESULT_OK && data != null) {
            view.logI("RESULT_OK");
            switch (requestCode) {  // check from which object data come
            case REQUEST_CODE_ADD_NOTE:  // if data come from .AddNoteActivity
                String noteName = data.getStringExtra("note_name");
                //TODO make good default value
                //getting all data
                String name = data.getStringExtra("note_name");
                final String content = data.getStringExtra("note_content");
                byte prototype = (byte) data.getIntExtra("freq",0);  // TODO comment it (why prototype)

                //creating calendar with data, that is got from addnote activity
                GregorianCalendar not_date =  new GregorianCalendar();  // TODO not_date means notification date?
                final long timeInMillis = data.getLongExtra("time_in_millis", -1);  // TODO make it better
                if(timeInMillis==-1){
                    not_date=null;
                }else{
                    not_date.setTimeInMillis(timeInMillis);
                }

                //creating and inserting to DB new note
                final Note local = new Note(name,content,not_date,prototype);
                model.insertToDb(local, callbackLoadDataFinish);
                view.log("case REQUEST_CODE_ADD_NOTE, noteName: \"" + noteName + "\";");
                break;
            case REQUEST_CODE_EDIT_NOTE:
                //checking, if button 'delete' was pressed
                boolean isDel = data.getBooleanExtra("isDel",false);
                if(!isDel) {
                    //getting all data
                    final int id = data.getIntExtra("id", -1);  // TODO move repeated cod from REQUEST_CODE_EDIT_NOTE and REQUEST_CODE_ADD_NOTE to line below onActivityResult
                    final String name_ = data.getStringExtra("note_name");
                    final String content_ = data.getStringExtra("note_content");
                    byte prototype_ = (byte) data.getIntExtra("freq",0);  // TODO comment it (why prototype)

                    //creating calendar with data, that is got from editnote activity
                    GregorianCalendar not__date = new GregorianCalendar();
                    final long timeInMillis_ = data.getLongExtra("time_in_millis", -1);
                    if (timeInMillis_ == -1) {
                        not__date = null;
                    } else {
                        not__date.setTimeInMillis(timeInMillis_);
                    }
                    // TODO make update
                    //creating and inserting updated note to DB
                    final Note local2 = new Note(name_, content_, not__date, prototype_);  // make declaration in start func
                    model.insertToDb(local2, callbackLoadDataFinish);
                    view.logV("YEP" + Integer.toString(id));

                    //getting and deleting old version of note from DB
                    model.deleteFromDb(id, callbackLoadDataFinish);

                }else{
                    final int id = data.getIntExtra("id", -1);
                    view.logV( id + " here ");
                    model.deleteFromDb(id, callbackLoadDataFinish);
                }
                break;
            default:
                view.logV("onActivityResult in default");
            }
        } else {
            view.logE("data == null or any different error, requestCode: \"" + requestCode + "\"; resultCode: \"" + resultCode + "\";"); //RESULT_OK: -1; RESULT_CANCELED: 0; RESULT_FIRST_USER(other user result): 1, 2, 3...
        }

    }

    public void loadDataFinish(List<Note> myNewNotes) {
        view.setAllNotes(myNewNotes);
    }
}
