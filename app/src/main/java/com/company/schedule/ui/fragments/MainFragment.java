package com.company.schedule.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presenter.MainPresenter;
import com.company.schedule.ui.activities.AddNoteActivity;
import com.company.schedule.ui.activities.MainActivity;
import com.company.schedule.ui.activities.SettingsActivity;
import com.company.schedule.ui.adapters.CustomLayoutManager;
import com.company.schedule.ui.adapters.NotesAdapter;
import com.company.schedule.view.MainView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static com.company.schedule.utils.Constants.REQUEST_CODE_EDIT_NOTE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements MainView {

    private MainActivity mainActivity;
    private View view;
    private MainPresenter presenter;

    private NotesAdapter adapter;
    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this,  // init view in presenter
                new MainInteractor(  // create interactor
                        new MainRepository(
                                AppDatabase.getDatabase(getContext()).noteDAO(),
                                new AppSchedulers()  // for threads
                        )  // create repository and get DAO
                )
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mainActivity = (MainActivity) getActivity();
        // init view components
        // init adapter for notesList
        adapter = new NotesAdapter(getContext(), notes);
        adapter.setClickListener((view, position) -> {
            Note noteToSend = notes.get(position);
            //if user clicks on item of recycler view, app goes to AddNoteActivity but with requestCode (...)_EDIT_NOTE
            //sending all data, that is needed for editing note
            Intent intent = new Intent(getContext(), AddNoteActivity.class);
            // TODO replace on Serializable or Parcelable
            intent.putExtra("id", noteToSend.getId());
            intent.putExtra("name", noteToSend.getName());
            intent.putExtra("content", noteToSend.getContent());
            intent.putExtra("frequency", noteToSend.getFrequency());
            intent.putExtra("date", noteToSend.getDate());

            startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);  // going to .AddNoteActivity for EDIT note
        });

        //recyclerview that is displaying all notes
        RecyclerView notesList = view.findViewById(R.id.notesList);

        notesList.setLayoutManager(new CustomLayoutManager(getContext()));
        notesList.setAdapter(adapter);


        FloatingActionButton fab = view.findViewById(R.id.fab);  // button for jump to AddNoteActivity
        fab.setOnClickListener(v -> mainActivity.showUpdateNoteFragment());  // setting handle

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // we say the MainPresenter that the MainView are almost created
        presenter.viewHasCreated();

    }

    //method that writes all data to recyclerview

    public void setAllNotes(List<Note> newNotes) {
        notes.clear();
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        notes.addAll(newNotes);
        adapter.notifyItemRangeInserted(0,newNotes.size());

    }

    public void setNote(Note newNote) {
        // TODO make something with new Note
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        on requestCode we determine from which subsidiary activity the result came
//        resultCode - return code. Determines whether the call has passed successfully or not.
//        data - Intent, in which the data is returned
//        if data was correct entered
        presenter.onActivityResult(requestCode,
                resultCode,
                getNoteFromIntent(data),
                data != null && data.getBooleanExtra("isDel",false)
        );
    }


    private Note getNoteFromIntent(Intent data) {
        if (data == null) return null;  // it's not a bug
        //getting all data
        final int id = data.getIntExtra("id", -1);
        final String name = data.getStringExtra("note_name");
        final String content = data.getStringExtra("note_content");
        byte freq = (byte) data.getIntExtra("freq",0);

        //creating calendar with data, that is got from editnote activity
        GregorianCalendar notify_date = new GregorianCalendar();
        final long timeInMillis = data.getLongExtra("time_in_millis", -1L);

        if (timeInMillis == -1L) notify_date = null;
        else notify_date.setTimeInMillis(timeInMillis);

        return new Note(id, name, content, notify_date, freq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void toast(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastLong(String toast_message) {
        Toast.makeText(getContext(), toast_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
