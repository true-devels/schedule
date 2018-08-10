package com.company.schedule.contract;

import android.content.Context;
import android.content.Intent;

import com.company.schedule.database.Note;
import com.company.schedule.ui.adapters.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

public interface MainContract {

    interface View {  // UI,  animation
        void startActivityForResult(Intent intent, int requestCode);
        void setAllNotes(List<Note> myLinks);
        void toast(String toast_message);
        void toastLong(String toast_message);
    }

    interface Presenter {
        void attachView(View view);
        void viewHasCreated(Context context);
        void onFabAddClicked(Context context);
        void onItemClicked(Context context, Note noteToSend);
        void onActivityResult(int requestCode, int resultCode, Intent data);
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
