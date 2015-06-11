package com.woi.merlin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.afollestad.materialdialogs.ThemeSingleton;
import com.squareup.picasso.Picasso;
import com.woi.merlin.component.ColorPickerDialog;
import com.woi.merlin.fragment.BillMaintenanceFragment;
import com.woi.merlin.fragment.MealFragment;
import com.woi.merlin.fragment.PhoneMgrFragment;
import com.woi.merlin.fragment.ReminderFragment;
import com.woi.merlin.notification.service.NotificationService;
import com.woi.merlin.ui.drawer.CustomDrawerAdapter;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;
import merlin.model.raw.DaoSession;

import static com.woi.merlin.R.drawable;


public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {

    private int mCurrentDrawerId = -1;
    private CustomDrawerAdapter adapter;
    private DaoSession daoSession;
    public List<String> mListNameItem;

    private static final int CASE_REMINDER = 0;
    private static final int CASE_MEAL = 1;
    private static final int CASE_PHONE_MGR = 2;
    private static final int CASE_BILL_CALC = 3;
    private static final int CASE_SETTING = 99;
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
    public void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {

            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onInt(Bundle bundle) {

        JodaTimeAndroid.init(this);
        // set listener {required}
        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(0);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, getTitleString(CASE_REMINDER));
        mListNameItem.add(1, getTitleString(CASE_MEAL));
        mListNameItem.add(2, getTitleString(CASE_PHONE_MGR));
        mListNameItem.add(3, getTitleString(CASE_BILL_CALC));

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_action_email);
        mListIconItem.add(1, drawable.ic_action_good);
        mListIconItem.add(2, drawable.ic_action_settings);//Item no icon set 0
        mListIconItem.add(3, drawable.ic_action_good);


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

    private void testNotification() {
        Intent service = new Intent(this, NotificationService.class);
        startService(service);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private String getTitleString(int CASE) {
        String str = "";

        switch (CASE) {
            case CASE_REMINDER:
                str = "Reminders";
                break;
            case CASE_MEAL:
                str = "Meals";
                break;

            case CASE_PHONE_MGR:
                str = "Phone Manager";
                break;

            case CASE_SETTING:
                str = "Settings";
                break;

            case CASE_BILL_CALC:
                str = "Bill Calculator";
                break;

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

    public void selectFragment(int position, int layoutContainerId) {


        Fragment mFragment = null;
        changeActionbarTitle(position);
//        changeActionbarColor(position); temporary disable because will impact to the material design navigation bar
        switch (position) {

            case CASE_REMINDER:
                mFragment = new ReminderFragment();
                break;

            case CASE_MEAL:
                mFragment = new MealFragment();
                break;

            case CASE_PHONE_MGR:
                mFragment = new PhoneMgrFragment();
                break;

            case CASE_BILL_CALC:
                mFragment = new BillMaintenanceFragment();
                break;
            case CASE_SETTING:
                break;

            default:
//                mFragment = new FragmentHome();
                break;
        }

        FragmentManager mFragmentManager = getFragmentManager();

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }
    }

    //Convenient method to change the action bar background color
    private void changeActionbarColor(int cid) {
        int colorId = -1;

        switch (cid) {
            case CASE_REMINDER:
                applyDefaultColorToActivity(11);
                break;

            case CASE_MEAL:
                applyDefaultColorToActivity(13);
                break;

            case CASE_PHONE_MGR:
                applyDefaultColorToActivity(8);
                break;

            case CASE_BILL_CALC:
                applyDefaultColorToActivity(13);
                break;
            case CASE_SETTING:
                applyDefaultColorToActivity(4);
                break;

        }

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
