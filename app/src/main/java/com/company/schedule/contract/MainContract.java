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
        NotesAdapter.ItemClickListener getItemClickListener(final ArrayList<Note> notes);
        void setAllNotes(List<Note> myLinks);
        // TODO delete logs
        void log(String log_message);
        void logV(String log_message);
        void logD(String log_message);
        void logI(String log_message);
        void logW(String log_message);
        void logE(String log_message);
    }

    interface Presenter {
        void viewHasCreated(Context context);
        void attachView(View view);
        void detachView();
        void onFabAddClicked(Context context);
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {
        void initDB(Context context);
        void insertToDb(final Note note, final LoadNoteCallback callback);
        void deleteFromDb(int id, final LoadNoteCallback callback);
        void loadData(LoadNoteCallback callback);
        // new code
        interface LoadNoteCallback {
            void onLoadData(List<Note> myNewNotes); //load data
        }

        interface OnComplectCallback {
            void onComplete();
        }
    }
}
