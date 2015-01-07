package com.woi.merlin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.woi.merlin.fragment.FragmentHome;
import com.woi.merlin.ui.drawer.CustomDrawerAdapter;
import com.woi.merlin.ui.drawer.DrawerItem;
import com.woi.merlin.ui.drawer.DrawerItemType;

import java.util.ArrayList;
import java.util.List;

import static com.woi.merlin.R.*;


public class MainActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private int mCurrentTitle;
    private int mSelectedFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    List<DrawerItem> dataList;
    private CustomDrawerAdapter adapter;

    private static final int CASE_HOME = 0;
    private static final int CASE_MY_CARDS = 1;
    private static final int CASE_MY_MISSIONS = 2;
    private static final int CASE_AVAILABLE_MISSIONS = 3;
    private static final int CASE_MY_TIMELINE = 4;
    private static final int CASE_TIMELINE = 5;

    private static final int CASE_SETTING = 6;
    private static final int CASE_ABOUT = 7;
    private static final int CASE_HELP = 8;
    private static final int CASE_EXIT = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        //init
        mCurrentTitle = CASE_HOME;

        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(id.drawer_layout);
        mDrawerLayout.setDrawerShadow(drawable.drawer_shadow,
                GravityCompat.START);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        // use action bar here
        getSupportActionBar().setIcon(drawable.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        //Initialisze navigation drawer
        initNavigationDrawer(savedInstanceState);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    public void initNavigationDrawer(Bundle savedInstanceState) {
        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(id.drawer_layout);
        mDrawerList = (ListView) findViewById(id.drawer);

        dataList.add(new DrawerItem("Navigate To")); // adding a header to the
        // list
        dataList.add(new DrawerItem(getTitleString(CASE_HOME), drawable.ic_action_email));
        dataList.add(new DrawerItem(getTitleString(CASE_MY_CARDS), drawable.ic_action_good));
        dataList.add(new DrawerItem(getTitleString(CASE_MY_MISSIONS), drawable.ic_action_gamepad));
        dataList.add(new DrawerItem(getTitleString(CASE_AVAILABLE_MISSIONS), drawable.ic_action_labels));
        dataList.add(new DrawerItem(getTitleString(CASE_MY_TIMELINE), drawable.ic_action_good));
        dataList.add(new DrawerItem(getTitleString(CASE_TIMELINE), drawable.ic_action_gamepad));

        dataList.add(new DrawerItem("Misc")); // adding a header to the
        // list
        dataList.add(new DrawerItem(getTitleString(CASE_SETTING), drawable.ic_action_settings));
        dataList.add(new DrawerItem(getTitleString(CASE_ABOUT), drawable.ic_action_about));
        dataList.add(new DrawerItem(getTitleString(CASE_HELP), drawable.ic_action_help));
        dataList.add(new DrawerItem(getTitleString(CASE_EXIT), drawable.ic_action_help));

        adapter = new CustomDrawerAdapter(this, layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                string.drawer_open,
//                string.drawer_close);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitleString(mCurrentTitle));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {

            int position = 0;
            for (DrawerItem di : dataList) {
                if (!di.getType().equals(DrawerItemType.TextWithImage)) {
                    position++;
                } else {
                    selectFragment(position);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private String getTitleString(int CASE) {
        String str = "";

        switch (CASE) {
            case CASE_HOME:
                str = "Home";
                break;
            case CASE_MY_CARDS:
                str = "My Cards";
                break;
            case CASE_MY_MISSIONS:
                str = "My Missions";
                break;
            case CASE_AVAILABLE_MISSIONS:
                str = "Available Missions";
                break;
            case CASE_MY_TIMELINE:
                str = "My Timeline";
                break;
            case CASE_TIMELINE:
                str = "Timeline";
                break;
            case CASE_SETTING:
                str = "Settings";
                break;
            case CASE_ABOUT:
                str = "About";
                break;
            case CASE_HELP:
                str = "Help";
                break;
            case CASE_EXIT:
                str = "Exit";
                break;
            default:
                break;
        }

        return str;
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (dataList.get(position).getTitle() == null) {
                selectFragment(position);
            }
        }
    }

    public void selectFragment(int position) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position) {

            case CASE_HOME:
                fragment = new FragmentHome();
                args.putString(FragmentHome.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;

            default:
                fragment = new FragmentHome();
                args.putString(FragmentHome.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                break;
        }

        if (fragment != null) {
            fragment.setArguments(args);

            FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(id.fragment_main, fragment)
                    .commit();

            mDrawerList.setItemChecked(position, true);
            setTitle(dataList.get(position).getItemName());
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }
}
