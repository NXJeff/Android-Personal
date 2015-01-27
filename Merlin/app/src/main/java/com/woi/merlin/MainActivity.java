package com.woi.merlin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.woi.merlin.fragment.FragmentCardSample1;
import com.woi.merlin.fragment.FragmentHome;
import com.woi.merlin.notification.service.NotificationReceiver;
import com.woi.merlin.ui.drawer.CustomDrawerAdapter;
import com.woi.merlin.ui.drawer.DrawerItem;
import com.woi.merlin.ui.drawer.DrawerItemType;

import java.util.ArrayList;
import java.util.List;

import merlin.Box;

import static com.woi.merlin.R.*;


public class MainActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private int mCurrentDrawerId;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    List<DrawerItem> drawerItemList;
    private CustomDrawerAdapter adapter;

    private static final int CASE_HEADER_NAVIGATION = 0;
    private static final int CASE_HOME = 1;
    private static final int CASE_MY_CARDS = 2;
    private static final int CASE_MY_MISSIONS = 3;
    private static final int CASE_AVAILABLE_MISSIONS = 4;
    private static final int CASE_MY_TIMELINE = 5;
    private static final int CASE_TIMELINE = 6;
    private static final int CASE_HEADER_MISC = 7;
    private static final int CASE_SETTING = 8;
    private static final int CASE_ABOUT = 9;
    private static final int CASE_HELP = 10;
    private static final int CASE_EXIT = 11;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        NotificationReceiver.scheduleAlarms(this);


        //init
        mCurrentDrawerId = CASE_HOME;

        drawerItemList = new ArrayList<DrawerItem>();
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
        drawerItemList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(id.drawer_layout);
        mDrawerList = (ListView) findViewById(id.drawer);

        drawerItemList.add(new DrawerItem(CASE_HEADER_NAVIGATION, getTitleString(CASE_HEADER_NAVIGATION))); // adding a header to the
        // list
        drawerItemList.add(new DrawerItem(CASE_HOME, getTitleString(CASE_HOME), drawable.ic_action_email));
        drawerItemList.add(new DrawerItem(CASE_MY_CARDS, getTitleString(CASE_MY_CARDS), drawable.ic_action_good));
        drawerItemList.add(new DrawerItem(CASE_MY_MISSIONS, getTitleString(CASE_MY_MISSIONS), drawable.ic_action_gamepad));
        drawerItemList.add(new DrawerItem(CASE_AVAILABLE_MISSIONS, getTitleString(CASE_AVAILABLE_MISSIONS), drawable.ic_action_labels));
        drawerItemList.add(new DrawerItem(CASE_MY_TIMELINE, getTitleString(CASE_MY_TIMELINE), drawable.ic_action_good));
        drawerItemList.add(new DrawerItem(CASE_TIMELINE, getTitleString(CASE_TIMELINE), drawable.ic_action_gamepad));

        drawerItemList.add(new DrawerItem(CASE_HEADER_MISC, getTitleString(CASE_HEADER_MISC))); // adding a header to the
        // list
        drawerItemList.add(new DrawerItem(CASE_SETTING, getTitleString(CASE_SETTING), drawable.ic_action_settings));
        drawerItemList.add(new DrawerItem(CASE_ABOUT, getTitleString(CASE_ABOUT), drawable.ic_action_about));
        drawerItemList.add(new DrawerItem(CASE_HELP, getTitleString(CASE_HELP), drawable.ic_action_help));
        drawerItemList.add(new DrawerItem(CASE_EXIT, getTitleString(CASE_EXIT), drawable.ic_action_help));

        adapter = new CustomDrawerAdapter(this, layout.custom_drawer_item,
                drawerItemList);

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
                getSupportActionBar().setTitle(getTitleString(mCurrentDrawerId));
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
            for (DrawerItem di : drawerItemList) {
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
            case CASE_HEADER_NAVIGATION:
                str = "Navigate To";
                break;
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
            case CASE_HEADER_MISC:
                str = "MISC";
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
            if (drawerItemList.get(position).getTitle() == null) {
                selectFragment(position);
            }
        }
    }

    public void selectFragment(int position) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        int drawerId = drawerItemList.get(position).getDrawerId();
        changeActionbarTitle(drawerId);
        switch (drawerId) {

            case CASE_HOME:
                changeActionbarColor(color.primary_material_dark);
                fragment = new FragmentCardSample1();
                args.putString(FragmentHome.ITEM_NAME, drawerItemList.get(position)
                        .getItemName());
                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, drawerItemList.get(position)
                        .getImgResID());
                break;

            case CASE_MY_CARDS:
                changeActionbarColor(color.blue_500);
                break;

            case CASE_MY_MISSIONS:
                changeActionbarColor(color.purple_500);
                break;

            case CASE_AVAILABLE_MISSIONS:
                changeActionbarColor(color.red_500);
                break;

            case CASE_MY_TIMELINE:
                changeActionbarColor(color.teal_500);
                break;

            case CASE_TIMELINE:
                changeActionbarColor(color.amber_500);
                break;
            case CASE_SETTING:
                changeActionbarColor(color.orange_500);
                break;

            case CASE_ABOUT:
                break;

            case CASE_HELP:
                fragment = new FragmentCardSample1();
                break;

            case CASE_EXIT:
                break;

            default:
                fragment = new FragmentHome();
                args.putString(FragmentHome.ITEM_NAME, drawerItemList.get(position)
                        .getItemName());
                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, drawerItemList.get(position)
                        .getImgResID());
                break;
        }

        if (fragment != null) {
            fragment.setArguments(args);

            FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(id.fragment_main, fragment)
                    .commit();

        }
        mDrawerList.setItemChecked(position, true);
        setTitle(drawerItemList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    //Convenient method to change the action bar background color
    private void changeActionbarColor(int cid) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(cid)));
    }

    private void changeActionbarTitle(int drawerId) {
        mCurrentDrawerId = drawerId;
    }
}
