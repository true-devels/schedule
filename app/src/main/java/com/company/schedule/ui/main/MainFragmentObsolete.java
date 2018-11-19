package com.company.schedule.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.company.schedule.R;
import com.company.schedule.model.data.base.AppDatabase;
import com.company.schedule.model.data.base.Note;
import com.company.schedule.model.interactor.MainInteractor;
import com.company.schedule.model.repository.MainRepository;
import com.company.schedule.model.system.AppSchedulers;
import com.company.schedule.presentation.main.MainPresenter;
import com.company.schedule.presentation.main.MainView;
import com.company.schedule.ui.updateNoteObsolete.UpdateNoteFragment;

import java.util.List;

import timber.log.Timber;

public class MainFragmentObsolete extends Fragment {// implements MainView {
/*  TODO obsolete
    // architecture
    private MainActivity mainActivity;
    private MainPresenter presenter;

    // view components
    private RecyclerView notesList;
    private FloatingActionButton fab;

    private NotesAdapterObsolete adapter;
//    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("TAG").d("my message");
        if (presenter == null)  // if presenter isn't created we create it
            presenter = new MainPresenter(this,  // init view in presenter
                    new MainInteractor(  // create interactor
                            new MainRepository(
                                    AppDatabase.getDatabase(getContext()).noteDAO(),
                                    new AppSchedulers()  // for threads
                            )  // create repository and get DAO
                    ),
                    getContext()
            );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentMain = inflater.inflate(R.layout.fragment_main, container, false);
        mainActivity = (MainActivity) getActivity();
        // init view components
        notesList = fragmentMain.findViewById(R.id.notesList);
        fab = fragmentMain.findViewById(R.id.fab);  // button for jump to AddNoteActivity

        return fragmentMain;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new NotesAdapterObsolete();
        adapter.setClickListener((noteClickedOn) -> goToUpdateNoteFragment(noteClickedOn));
        adapter.setOnCheckedChangeListener((noteCheckedOn, isChecked) -> presenter.onCheckedDoneChanged(noteCheckedOn, isChecked));

        notesList.setLayoutManager(new LinearLayoutManager(getContext()));
        notesList.setAdapter(adapter);

        presenter.loadData();  // we load data to Recycler view

        fab.setOnClickListener(v -> goToAddNoteFragment());  // setting handle
    }

    //method that writes all data to recyclerview
    public void setAllNotes(List<Note> newNotes) {
        adapter.setAllNotes(newNotes);
    }


    public void setNote(Note newNote) {
        // TODO make something with new Note
    }

    private void goToAddNoteFragment() {
        Fragment fragment = new UpdateNoteFragment();
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);  // where we place fragment
        fragmentTransaction.addToBackStack(null);  // features for the back button

        fragmentTransaction.commit();
    }

    private void goToUpdateNoteFragment(Note noteToSend) {
        Fragment fragment = new UpdateNoteFragment();
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);  // where we place fragment
        fragmentTransaction.addToBackStack(null);  // features for the back button

        Bundle transmission = new Bundle();
        transmission.putSerializable("note", noteToSend);
        fragment.setArguments(transmission);

        fragmentTransaction.commit();
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
*/
}
