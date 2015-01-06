package com.woi.merlin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.woi.merlin.ui.sidedrawer.CustomDrawerAdapter;
import com.woi.merlin.ui.sidedrawer.DrawerItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    //FragmentID
    public static final int CASE_MAIN = 0;
    public static final int CASE_MY_CARDS = 1;
    public static final int CASE_MY_MISSIONS = 2;
    public static final int CASE_MY_TIMELINE = 3;
    public static final int CASE_ALL_MISSIONS = 4;
    public static final int CASE_TIMELINE = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Add Drawer Item to dataList
        dataList.add(new DrawerItem(true)); // adding a spinner to the list

        dataList.add(new DrawerItem("My Favorites")); // adding a header to the
        // list
        dataList.add(new DrawerItem("Message", R.drawable.ic_action_email));
        dataList.add(new DrawerItem("Likes", R.drawable.ic_action_good));
        dataList.add(new DrawerItem("Games", R.drawable.ic_action_gamepad));
        dataList.add(new DrawerItem("Lables", R.drawable.ic_action_labels));

        dataList.add(new DrawerItem("Main Options"));// adding a header to the
        // list
        dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
        dataList.add(new DrawerItem("Cloud", R.drawable.ic_action_cloud));
        dataList.add(new DrawerItem("Camara", R.drawable.ic_action_camera));
        dataList.add(new DrawerItem("Video", R.drawable.ic_action_video));
        dataList.add(new DrawerItem("Groups", R.drawable.ic_action_group));
        dataList.add(new DrawerItem("Import & Export",
                R.drawable.ic_action_import_export));

        dataList.add(new DrawerItem("Other Option")); // adding a header to the
        // list
        dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
        dataList.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
        dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {

            if (dataList.get(0).isSpinner()
                    & dataList.get(1).getTitle() != null) {
                SelectItem(2);
            } else if (dataList.get(0).getTitle() != null) {
                SelectItem(1);
            } else {
                SelectItem(0);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectFragment(int CASE) {

        Fragment fragment = null;
        String itemName = "";
        String IMAGE_RESOURCE_ID = "iconResourceID";
        Bundle args = new Bundle();

        switch (CASE) {
            case CASE_MAIN:
                //itemName = fragment.ITEM_NAME;
                break;
            case CASE_MY_CARDS:
                //itemName = fragment.ITEM_NAME;
                break;
            case CASE_MY_MISSIONS:
                //itemName = fragment.ITEM_NAME;
                break;
            case CASE_MY_TIMELINE:
                //itemName = fragment.ITEM_NAME;
                break;
            case CASE_TIMELINE:
                //itemName = fragment.ITEM_NAME;
                break;
        }

        if (fragment != null) {
            args.putString(itemName, dataList.get(CASE)
                    .getItemName());
            args.putInt(IMAGE_RESOURCE_ID, dataList.get(CASE)
                    .getImgResID());
            fragment.setArguments(args);
            FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();

            mDrawerList.setItemChecked(CASE, true);
            setTitle(dataList.get(CASE).getItemName());
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }
}
