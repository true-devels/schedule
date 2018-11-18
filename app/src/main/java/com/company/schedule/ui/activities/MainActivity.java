package com.company.schedule.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;

import com.company.schedule.R;
import com.company.schedule.ui.adapters.PagerAdapter;



public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get data from sharedPrefs to set theme mode
       /* SharedPrefs sharedPrefs = new SharedPrefs(this);

        if(sharedPrefs.isNightMode()) setTheme(R.style.darktheme);  //dark
        else setTheme(R.style.AppTheme);  //white*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init view components
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    switch (menuItem.getItemId()){
                        case R.id.nav_later:
                            Intent intent= new Intent(this,LaterActivity.class);
                            intent.putExtra("role",1);
                            startActivity(intent);
                            break;
                        case R.id.nav_done:
                            Intent intent2 = new Intent(this,LaterActivity.class);
                            intent2.putExtra("role",2);
                            startActivity(intent2);
                    }
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    return true;
                });

        final Toolbar toolbar =  findViewById(R.id.my_toolbar);  // maybe toolbar will be useful
        setSupportActionBar(toolbar);

       /* if (presenter == null)  // if presenter isn't created we create it
            presenter = new MainPresenter(this,  // init view in presenter
                    new MainInteractor(  // create interactor
                            new MainRepository(
                                    AppDatabase.getDatabase(this).noteDAO(),
                                    new AppSchedulers()  // for threads
                            )  // create repository and get DAO
                    )
            );*/
      /*  notes_rc.setHasFixedSize(true);
        mainLayout = findViewById(R.id.content_frame);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        notes_rc.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        notes_rc.setItemAnimator(new DefaultItemAnimator());
        notes_rc.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        presenter.loadDailyData();
        adapter = new NodeAdapter(this);

        notes_rc.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback callback_left = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(callback_left).attachToRecyclerView(notes_rc);

        ItemTouchHelper.SimpleCallback callback_right = new RecyclerViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(callback_right).attachToRecyclerView(notes_rc);*/



        TabLayout tb  = findViewById(R.id.tabs);
        final ViewPager viewPager =  findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tb.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));
        tb.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ImageButton imgbtn2 = findViewById(R.id.imageButton2);
        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                intent.putExtra("tab",tb.getSelectedTabPosition());
                startActivity(intent);
            }
        });

        ImageButton imgbtn = findViewById(R.id.imageButton);
        imgbtn.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));




       /* adapter.setClickListener((noteClickedOn) -> goToUpdateNoteFragment(noteClickedOn));
        adapter.setOnCheckedChangeListener((noteCheckedOn, isChecked) -> presenter.onCheckedDoneChanged(noteCheckedOn, isChecked));

        notesList.setLayoutManager(new LinearLayoutManager(getContext()));
        notesList.setAdapter(adapter);*/

          // we load data to Recycler view



        // open fragment transaction
        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, new MainFragment());  // add fragment to screen
//        if (useBackStack) fragmentTransaction.addToBackStack(null); // feature
        fragmentTransaction.commit();*/
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


   /* @Override
    public void setAllNotes(List<Note> newNotes) {
        adapter.setAllNotes(newNotes);
    }

    @Override
    public void toast(String toast_message) {
        Toast.makeText(this, toast_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastLong(String toast_message) {
        Toast.makeText(this, toast_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        int deleteIndex = viewHolder.getAdapterPosition ();
        Note item = adapter.removeItem(deleteIndex);
        if(direction == ItemTouchHelper.LEFT){
            Snackbar snackbar = Snackbar.make(mainLayout,"Deleted " + ((NodeAdapter.MyViewHolder) viewHolder).mTextView.getText(),Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("UNDO", v -> adapter.restoreItem(item,deleteIndex));
            int id = ((NodeAdapter.MyViewHolder) viewHolder).id;
            presenter.swipedToDelete(id);
            Log.d("id_check ",Integer.toString(id));
        }

    }*/
}

