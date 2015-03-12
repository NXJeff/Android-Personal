package com.woi.merlin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.ThemeSingleton;
import com.squareup.picasso.Picasso;
import com.woi.merlin.component.ColorPickerDialog;
import com.woi.merlin.fragment.FragmentCardSample1;
import com.woi.merlin.fragment.FragmentHome;
import com.woi.merlin.fragment.MealFragment;
import com.woi.merlin.fragment.ReminderFragment;
import com.woi.merlin.notification.service.NotificationService;
import com.woi.merlin.ui.drawer.CustomDrawerAdapter;
import com.woi.merlin.ui.drawer.DrawerItem;
import com.woi.merlin.ui.drawer.DrawerItemType;
import com.woi.merlin.util.DbUtil;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;
import merlin.model.raw.DaoSession;

import static com.woi.merlin.R.*;


public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {

    private ListView mDrawerList;
    private int mCurrentDrawerId = -1;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    List<DrawerItem> drawerItemList;
    private CustomDrawerAdapter adapter;
    private DaoSession daoSession;
    public List<String> mListNameItem;

//    private static final int CASE_HEADER_NAVIGATION = 0;
    private static final int CASE_REMINDER = 0;
    private static final int CASE_MEAL = 1;
    //    private static final int CASE_MY_MISSIONS = 3;
//    private static final int CASE_AVAILABLE_MISSIONS = 4;
//    private static final int CASE_MY_TIMELINE = 5;
//    private static final int CASE_TIMELINE = 6;
//    private static final int CASE_HEADER_MISC = 7;
    private static final int CASE_SETTING = 2;
    //    private static final int CASE_ABOUT = 9;
//    private static final int CASE_HELP = 10;
//    private static final int CASE_EXIT = 11;
    private static final String TAG = "MERLIN-APP";


    @Override
    public void onUserInformation() {
        //User information here
        this.mUserName.setText("NXJeff");
        this.mUserEmail.setText("nxjeff@gmail.com");
//        this.mUserPhoto.setImageResource(R.drawable.ic_rudsonlive);
        Picasso.with(this).load("https://lh6.googleusercontent.com/-lfCWdJ4wPo4/AAAAAAAAAAI/AAAAAAAAAAA/V4RQzSNHQSc/s96-c/photo.jpg").into(mUserPhoto);
        this.mUserBackground.setImageResource(drawable.ic_user_background);
    }

    @Override
    public void onInt(Bundle bundle) {
        JodaTimeAndroid.init(this);
        // set listener {required}
        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(1);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, getTitleString(CASE_REMINDER));
        mListNameItem.add(1, getTitleString(CASE_MEAL));
//        mListNameItem.add(2, getTitleString(CASE_SETTING));
//        mListNameItem.add(3, getString(R.string.drafts));
//        mListNameItem.add(4, getString(R.string.more_markers)); //This item will be a subHeader
//        mListNameItem.add(5, getString(R.string.trash));
//        mListNameItem.add(6, getString(R.string.spam));

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_action_email);
        mListIconItem.add(1, drawable.ic_action_good); //Item no icon set 0
//        mListIconItem.add(2, R.drawable.ic_send_black_24dp); //Item no icon set 0
//        mListIconItem.add(3, R.drawable.ic_drafts_black_24dp);
//        mListIconItem.add(4, 0); //When the item is a subHeader the value of the icon 0
//        mListIconItem.add(5, R.drawable.ic_delete_black_24dp);
//        mListIconItem.add(6, R.drawable.ic_report_black_24dp);

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
//        mListHeaderItem.add(4);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 123);
//        mSparseCounterItem.put(6, 250);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer(getTitleString(CASE_SETTING), drawable.ic_action_settings);

        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(layout.activity_main);
//        //init joda
////        NotificationReceiver.scheduleAlarms(this);
//        // enable ActionBar app icon to behave as action to toggle nav drawer
//        // use action bar here
////        getSupportActionBar().setIcon(drawable.ic_launcher);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
////        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        //Initialisze navigation drawer
//
//        if (savedInstanceState == null) {
//            savedInstanceState = new Bundle();
//            JodaTimeAndroid.init(this);
//            daoSession = DbUtil.setupDatabase(this);
//        } else {
//            mCurrentDrawerId = savedInstanceState.getInt("mLastDrawerId");
//        }
//        initNavigationDrawer(savedInstanceState);
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putInt("mLastDrawerId", mCurrentDrawerId);
//    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//        mCurrentDrawerId = savedInstanceState.getInt("mLastDrawerId");
//    }


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

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }

//    public void initNavigationDrawer(Bundle savedInstanceState) {
//        // Initializing
//        mDrawerList = (ListView) findViewById(id.drawer);
//
//        //init
////        mCurrentDrawerId = CASE_REMINDER;
//
//        mDrawerLayout = (DrawerLayout) findViewById(id.drawer_layout);
//        mDrawerLayout.setDrawerShadow(drawable.drawer_shadow,
//                GravityCompat.START);
//
//        if (drawerItemList == null) {
//            drawerItemList = new ArrayList<DrawerItem>();
//            drawerItemList.add(new DrawerItem(CASE_HEADER_NAVIGATION, getTitleString(CASE_HEADER_NAVIGATION))); // adding a header to the
//            // list
//            drawerItemList.add(new DrawerItem(CASE_REMINDER, getTitleString(CASE_REMINDER), drawable.ic_action_email));
//            drawerItemList.add(new DrawerItem(CASE_MEAL, getTitleString(CASE_MEAL), drawable.ic_action_good));
////            drawerItemList.add(new DrawerItem(CASE_MY_MISSIONS, getTitleString(CASE_MY_MISSIONS), drawable.ic_action_gamepad));
////            drawerItemList.add(new DrawerItem(CASE_AVAILABLE_MISSIONS, getTitleString(CASE_AVAILABLE_MISSIONS), drawable.ic_action_labels));
////            drawerItemList.add(new DrawerItem(CASE_MY_TIMELINE, getTitleString(CASE_MY_TIMELINE), drawable.ic_action_good));
////            drawerItemList.add(new DrawerItem(CASE_TIMELINE, getTitleString(CASE_TIMELINE), drawable.ic_action_gamepad));
////
////            drawerItemList.add(new DrawerItem(CASE_HEADER_MISC, getTitleString(CASE_HEADER_MISC))); // adding a header to the
//            // list
//            drawerItemList.add(new DrawerItem(CASE_SETTING, getTitleString(CASE_SETTING), drawable.ic_action_settings));
////            drawerItemList.add(new DrawerItem(CASE_ABOUT, getTitleString(CASE_ABOUT), drawable.ic_action_about));
////            drawerItemList.add(new DrawerItem(CASE_HELP, getTitleString(CASE_HELP), drawable.ic_action_help));
////            drawerItemList.add(new DrawerItem(CASE_EXIT, getTitleString(CASE_EXIT), drawable.ic_action_help));
//            adapter = new CustomDrawerAdapter(this, layout.custom_drawer_item,
//                    drawerItemList);
//            mDrawerList.setAdapter(adapter);
//        }
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
////        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
////                string.drawer_open,
////                string.drawer_close);
//
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                R.string.drawer_open,  /* "open drawer" description */
//                R.string.drawer_close  /* "close drawer" description */
//        ) {
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                changeActionbarTitle(mCurrentDrawerId);
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("");
//            }
//        };
//
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
//
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//        if (mCurrentDrawerId == -1) {
//
//            int position = 0;
//            for (DrawerItem di : drawerItemList) {
//                if (!di.getType().equals(DrawerItemType.TextWithImage)) {
//                    position++;
//                } else {
//                    selectFragment(position);
//                    break;
//                }
//            }
//        } else {
//            changeActionbarTitle(mCurrentDrawerId);
//            changeActionbarColor(mCurrentDrawerId);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent e) {
//        if (keyCode == KeyEvent.KEYCODE_MENU) {
//
//            if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
//                mDrawerLayout.openDrawer(mDrawerList);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, e);
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Pass the event to ActionBarDrawerToggle, if it returns
//        // true, then it has handled the app icon touch event
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        // Handle your other action bar items...
//
//        return super.onOptionsItemSelected(item);
//    }

    private String getTitleString(int CASE) {
        String str = "";

        switch (CASE) {
//            case CASE_HEADER_NAVIGATION:
//                str = "Navigate To";
//                break;
            case CASE_REMINDER:
                str = "Reminders";
                break;
            case CASE_MEAL:
                str = "Meals";
                break;
//            case CASE_MY_MISSIONS:
//                str = "My Missions";
//                break;
//            case CASE_AVAILABLE_MISSIONS:
//                str = "Available Missions";
//                break;
//            case CASE_MY_TIMELINE:
//                str = "My Timeline";
//                break;
//            case CASE_TIMELINE:
//                str = "Timeline";
//                break;
//            case CASE_HEADER_MISC:
//                str = "MISC";
//                break;
            case CASE_SETTING:
                str = "Settings";
                break;
//            case CASE_ABOUT:
//                str = "About";
//                break;
//            case CASE_HELP:
//                str = "Help";
//                break;
//            case CASE_EXIT:
//                str = "Exit";
//                break;
            default:
                break;
        }

        return str;
    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {
        selectFragment(position, layoutContainerId);
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {
        //hide the menu when the navigation is opens
//        switch (position) {
//            case 0:
//                menu.findItem(R.id.menu_add).setVisible(!visible);
//                menu.findItem(R.id.menu_search).setVisible(!visible);
//                break;
//
//            case 1:
//                menu.findItem(R.id.menu_add).setVisible(!visible);
//                menu.findItem(R.id.menu_search).setVisible(!visible);
//                break;
//        }
    }

    @Override
    public void onClickFooterItemNavigation(View view) {
        //user photo onClick
        Toast.makeText(this, "open setting", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickUserPhotoNavigation(View view) {
        //user photo onClick
        Toast.makeText(this, "open user profile", Toast.LENGTH_SHORT).show();
    }


//    private class DrawerItemClickListener implements
//            ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position,
//                                long id) {
//            if (drawerItemList.get(position).getTitle() == null) {
//                selectFragment(position);
//            }
//        }
//
//    }

    public void selectFragment(int position,int layoutContainerId) {


        Fragment mFragment = null;
        Bundle args = new Bundle();
//        mCurrentDrawerId = drawerItemList.get(position).getDrawerId();
        changeActionbarTitle(mCurrentDrawerId);
        changeActionbarColor(mCurrentDrawerId);
        switch (position) {

            case CASE_REMINDER:
                mFragment = new ReminderFragment();
//                args.putString(FragmentHome.ITEM_NAME, drawerItemList.get(position)
//                        .getItemName());
//                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, drawerItemList.get(position)
//                        .getImgResID());
                break;

            case CASE_MEAL:
                mFragment = new MealFragment();
//                args.putString(FragmentHome.ITEM_NAME, drawerItemList.get(position)
//                        .getItemName());
//                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, drawerItemList.get(position)
//                        .getImgResID());
                break;

//            case CASE_MY_MISSIONS:
//                fragment = new MealFragment();
//                break;
//
//            case CASE_AVAILABLE_MISSIONS:
//                break;
//
//            case CASE_MY_TIMELINE:
//                break;
//
//            case CASE_TIMELINE:
//                break;
            case CASE_SETTING:
                break;

//            case CASE_ABOUT:
//                break;
//
//            case CASE_HELP:
//                fragment = new FragmentCardSample1();
//                break;
//
//            case CASE_EXIT:
//                break;

            default:
                mFragment = new FragmentHome();
//                args.putString(FragmentHome.ITEM_NAME, drawerItemList.get(position)
//                        .getItemName());
//                args.putInt(FragmentHome.IMAGE_RESOURCE_ID, drawerItemList.get(position)
//                        .getImgResID());
                break;
        }

        FragmentManager mFragmentManager = getFragmentManager();
//        Fragment mFragment = new FragmentMain().newInstance(mListNameItem.get(position));

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }

//        if (fragment != null) {
//            fragment.setArguments(args);
//
//            FragmentManager frgManager = getFragmentManager();
//            frgManager.beginTransaction().replace(id.fragment_main, fragment)
//                    .commit();
//
//        }
//        mDrawerList.setItemChecked(position, true);
//        setTitle(drawerItemList.get(position).getItemName());
//        mDrawerLayout.closeDrawer(mDrawerList);

    }

    //Convenient method to change the action bar background color
    private void changeActionbarColor(int cid) {
        int colorId = -1;

        switch (cid) {
            case CASE_REMINDER:
//                colorId = color.primary_material_dark;
                applyDefaultColorToActivity(11);
                break;

            case CASE_MEAL:
                applyDefaultColorToActivity(13);
//                colorId = color.blue_500;
                break;

            case CASE_SETTING:
                applyDefaultColorToActivity(4);
                break;

//            case CASE_MY_MISSIONS:
////                colorId = color.purple_500;
//                break;
//
//            case CASE_AVAILABLE_MISSIONS:
////                colorId = color.red_500;
//                break;
//
//            case CASE_MY_TIMELINE:
////                colorId = color.teal_500;
//                break;
//
//            case CASE_TIMELINE:
////                colorId = color.amber_500;
//                break;
        }

//        if (colorId != -1)
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(colorId)));
    }

    private void applyDefaultColorToActivity(int colorPosition) {
        TypedArray ca = getResources().obtainTypedArray(R.array.colors);
        TypedArray cna = getResources().obtainTypedArray(R.array.colorsName);
        int selectedColor = ca.getColor(colorPosition, 0);
        int darker = ColorPickerDialog.shiftColor(selectedColor);
        String colorName = cna.getString(colorPosition);
        applyColorToActivity(selectedColor, darker, colorName);
    }

    private void applyColorToActivity(int color, int darker, String colorName) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        ThemeSingleton.get().positiveColor = color;
        ThemeSingleton.get().neutralColor = color;
        ThemeSingleton.get().negativeColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(darker);
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
