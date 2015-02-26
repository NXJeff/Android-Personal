package com.woi.merlin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.woi.merlin.fragment.FragmentCardSample1;
import com.woi.merlin.fragment.FragmentHome;
import com.woi.merlin.fragment.ReminderFragment;
import com.woi.merlin.notification.service.NotificationService;
import com.woi.merlin.ui.drawer.CustomDrawerAdapter;
import com.woi.merlin.ui.drawer.DrawerItem;
import com.woi.merlin.ui.drawer.DrawerItemType;
import com.woi.merlin.util.DbUtil;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import merlin.model.raw.DaoSession;

import static com.woi.merlin.R.*;


public class MainActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private int mCurrentDrawerId = -1;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    List<DrawerItem> drawerItemList;
    private CustomDrawerAdapter adapter;
    private DaoSession daoSession;

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
    private static final String TAG = "MERLIN-APP";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        //init joda
//        NotificationReceiver.scheduleAlarms(this);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        // use action bar here
        getSupportActionBar().setIcon(drawable.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialisze navigation drawer

        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();
            JodaTimeAndroid.init(this);
            daoSession = DbUtil.setupDatabase(this);
        } else {
            mCurrentDrawerId = savedInstanceState.getInt("mLastDrawerId");
        }

        initNavigationDrawer(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("mLastDrawerId", mCurrentDrawerId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        mCurrentDrawerId = savedInstanceState.getInt("mLastDrawerId");
    }


//    private void setupDatabase() {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "merlin-db-2", null);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        daoSession = daoMaster.newSession();
//        Log.d(TAG, "Initialise database completed on " + this.getClass().getSimpleName());
//    }

    private void testInsert() {

    }

    private void testNotification() {
        Intent service = new Intent(this, NotificationService.class);
//        service.putExtra("Notification", String.valueOf(alarmId));
//        service.setAction(AlarmService.POPULATE);
        startService(service);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void initNavigationDrawer(Bundle savedInstanceState) {
        // Initializing
        mDrawerList = (ListView) findViewById(id.drawer);

        //init
//        mCurrentDrawerId = CASE_HOME;

        mDrawerLayout = (DrawerLayout) findViewById(id.drawer_layout);
        mDrawerLayout.setDrawerShadow(drawable.drawer_shadow,
                GravityCompat.START);

        if (drawerItemList == null) {
            drawerItemList = new ArrayList<DrawerItem>();
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
        }
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
                changeActionbarTitle(mCurrentDrawerId);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (mCurrentDrawerId == -1) {

            int position = 0;
            for (DrawerItem di : drawerItemList) {
                if (!di.getType().equals(DrawerItemType.TextWithImage)) {
                    position++;
                } else {
                    selectFragment(position);
                    break;
                }
            }
        } else {
            changeActionbarTitle(mCurrentDrawerId);
            changeActionbarColor(mCurrentDrawerId);
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
        mCurrentDrawerId = drawerItemList.get(position).getDrawerId();
        changeActionbarTitle(mCurrentDrawerId);
        changeActionbarColor(mCurrentDrawerId);
        switch (mCurrentDrawerId) {

            case CASE_HOME:
                fragment = new ReminderFragment();
                args.putString(FragmentHome.ITEM_NAME, drawerItemList.get(position)
                        .getItemName());
                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, drawerItemList.get(position)
                        .getImgResID());
                break;

            case CASE_MY_CARDS:
                fragment = new FragmentCardSample1();
                args.putString(FragmentHome.ITEM_NAME, drawerItemList.get(position)
                        .getItemName());
                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, drawerItemList.get(position)
                        .getImgResID());
                break;

            case CASE_MY_MISSIONS:
                break;

            case CASE_AVAILABLE_MISSIONS:
                break;

            case CASE_MY_TIMELINE:
                break;

            case CASE_TIMELINE:
                break;
            case CASE_SETTING:
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
        int colorId = -1;

        switch (cid) {
            case CASE_HOME:
                colorId = color.primary_material_dark;
                break;

            case CASE_MY_CARDS:
                colorId = color.blue_500;
                break;

            case CASE_MY_MISSIONS:
                colorId = color.purple_500;
                break;

            case CASE_AVAILABLE_MISSIONS:
                colorId = color.red_500;
                break;

            case CASE_MY_TIMELINE:
                colorId = color.teal_500;
                break;

            case CASE_TIMELINE:
                colorId = color.amber_500;
                break;
        }

        if (colorId != -1)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(colorId)));
    }

    private void changeActionbarTitle(int drawerId) {
        getSupportActionBar().setTitle(getTitleString(drawerId));
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}
