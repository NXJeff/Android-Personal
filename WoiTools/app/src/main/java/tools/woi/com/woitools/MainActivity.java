package tools.woi.com.woitools;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.greysonparrelli.permiso.Permiso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tools.woi.com.woitools.archive.fragment.ArchiveFragment;
import tools.woi.com.woitools.archive.fragment.ArchiveListFragment;
import tools.woi.com.woitools.base.BaseFragment;
import tools.woi.com.woitools.system.domain.SystemConfiguration;
import tools.woi.com.woitools.system.domain.SystemPropertyKey;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FolderChooserDialog.FolderCallback {

    BaseFragment mFragment = null;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private Toast mToast;
    private String folderChooserDialogOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Permiso.getInstance().setActivity(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        requestPermission();
        selectFragment(new ArchiveFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.fab)
    public void fabOnClick(View view) {
        mFragment.fabButtonAction();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mFragment.getBackFragment() != null) {
                mFragment = mFragment.getBackFragment();
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        selectFragment(new ArchiveFragment());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectFragment(BaseFragment fragment) {
        mFragment = fragment;
        mFragment.setFab(fab);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.fragment_main, mFragment).commit();
        }
    }

    public void navigateToFragment(String FRAGMENT_CODE) {


        if (FRAGMENT_CODE.equals(ArchiveListFragment.TAG)) {
            BaseFragment baseFragment = new ArchiveListFragment();
            baseFragment.setBackFragment(mFragment);
            mFragment = baseFragment;
            mFragment.setFab(fab);
        }

        addFragment(mFragment);
    }

    @Override
    public void onFolderSelection(File folder) {
        if (folderChooserDialogOperation.equals(ArchiveFragment.CHOOSE_DESTINATION_PATH)) {
            showToast(folder.getAbsolutePath());
            saveDestinationPath(folder);
        }
    }

    public void showFolderChooser(String Operation, String path) {
        folderChooserDialogOperation = Operation;
        new FolderChooserDialog.Builder(this)
                .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                .initialPath(path)  // changes initial path, defaults to external storage directory
                .show();
    }

    /**
     * ArchiveFragment related methods
     */
    public void saveDestinationPath(File folder) {
        SystemConfiguration conf = SystemConfiguration.find(SystemPropertyKey.Archieve_DestinationPath);
        if (conf == null) {
            conf = new SystemConfiguration(SystemPropertyKey.Archieve_DestinationPath, folder.getAbsolutePath());
        } else {
            conf.setValue(folder.getAbsolutePath());
        }

        conf.save();
        ArchiveFragment fragment = (ArchiveFragment) mFragment;
        fragment.onDirectoryChoosen(folder.getAbsolutePath());
    }

    /**
     * Other methods
     *
     * @param message
     */
    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void showToast(@StringRes int message) {
        showToast(getString(message));
    }

    private void requestPermission() {
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    // Permission granted!
                } else {
                    // Permission denied.
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Title", "Message", null, callback);
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    // TO ADD A FRAGMENT.
    public void addFragment(BaseFragment baseFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        if(withAnimation) {
//            ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
//        }
        ft.replace(R.id.fragment_main, baseFragment, baseFragment.getTagText());
        ft.addToBackStack(baseFragment.getTagText());
        ft.commit();
    }

}
