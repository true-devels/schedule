package com.company.schedule.contract;

import android.content.Context;
import android.content.Intent;

import com.company.schedule.database.Note;
import com.company.schedule.ui.adapters.NotesAdapter;

import java.util.ArrayList;

public interface MainContract {

    interface View {  // UI,  animation
        void startActivityForResult(Intent intent, int requestCode);
        NotesAdapter.ItemClickListener getItemClickListener(final ArrayList<Note> notes);
        void log(String log_message);
        void logV(String log_message);
        void logD(String log_message);
        void logI(String log_message);
        void logW(String log_message);
        void logE(String log_message);
    }

    interface Presenter {
        void onCreate(Context context);
//        void setAdapter(final Context context, NotesAdapter.ItemClickListener itemClickListener);
        NotesAdapter getAdapter(Context context);
        void attachView(View view);
        void detachView();
        void onFabAddClicked(Context context);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void loadData();
    }

    interface Model {
        void initDB(Context context);
        ArrayList<Note> getNotes();
        void setAdapter(final Context context, NotesAdapter.ItemClickListener itemClickListener);
        NotesAdapter getAdapter();
        void insertToDb(final Note note);
        void deleteFromDb(int id);
        void loadData();
    }
}
