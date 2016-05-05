package com.woi.tools;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.woi.tools.archive.fragment.ArchiveFragment;
import com.woi.tools.flashtool.fragment.FlashToolFragment;
import com.woi.tools.global.system.domain.SystemConfiguration;
import com.woi.tools.global.system.domain.SystemPropertyKey;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import butterknife.ButterKnife;

public class MainActivity extends NavigationLiveo implements OnItemClickListener,
        FolderChooserDialog.FolderCallback {

    private static final int CASE_FLASH_TOOL = 1;
    private static final int CASE_ARCHIEVE = 0;
    private static final int CASE_SETTING = 99;

    TextView mainTitle;
    private HelpLiveo mHelpLiveo;
    private Toast mToast;

    @Override
    public void onInt(Bundle bundle) {
        // set listener {required}

        //First item of the position selected from the list

        // name of the list items
        List<String> mListNameItem = new ArrayList<>();
        mListNameItem.add(0, getTitleBarString(CASE_FLASH_TOOL));
        mListNameItem.add(1, getTitleBarString(CASE_ARCHIEVE));

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
//        mListIconItem.add(0, R.drawable.ic_action_email);
//        mListIconItem.add(1, drawable.ic_action_good); //Item no icon set 0

        //{optional} - Among the names there is some subheader, you must indicate it here
//        List<Integer> mListHeaderItem = new ArrayList<>();

//        this.setFooterInformationDrawer(getTitleBarString(CASE_SETTING), R.drawable.ic_action_settings);

//        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, null);

        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getTitleBarString(CASE_FLASH_TOOL));
        mHelpLiveo.add(getTitleBarString(CASE_ARCHIEVE));



        with(this) // default theme is dark
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem(getTitleBarString(CASE_SETTING), R.mipmap.ic_launcher)
                .setOnClickUser(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //DONOTHING
                    }
                })
//                .setOnPrepareOptionsMenu(onPrepare)
//                .setOnClickFooter(onClickFooter)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        setContentView(R.layout.activity_main);
//        mainTitle = (TextView) findViewById(R.id.main_title);
//        runCommands();
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

    public void runCommands() {

        String[] cmds = new String[10];
        cmds[0] = "busybox mount -o remount,rw /system";
        cmds[1] = "busybox cp -R /storage/sdcard1/Others/new/zzmove/* /system/etc/init.d/";
        cmds[2] = "busybox cp -R /storage/sdcard1/Others/hosts /system/etc/";
        cmds[3] = "busybox sleep 2";
        cmds[4] = "busybox chmod -R 777 /system/etc/init.d";
        cmds[5] = "busybox chmod -R 664 /system/etc/hosts";
        cmds[6] = "busybox sleep 2";
        cmds[7] = "busybox sed -i 's|ro.sf.lcd_density=.*|ro.sf.lcd_density=272|g' /system/build.prop";
        cmds[8] = "busybox mount -o remount,ro /system";
        try {
            runCommandsAsRoot(cmds);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void runCommandsAsRoot(String[] cmds) throws IOException {
        Process proc = Runtime.getRuntime().exec("su");

        DataOutputStream oStream = new DataOutputStream(proc.getOutputStream());

        for (String cmd : cmds) {
            oStream.writeBytes(cmd + "\n");
        }

        oStream.writeBytes("exit\n");
        oStream.flush();
        mainTitle.setText("Operation done.");
    }

    public void selectFragment(int position) {


        Fragment mFragment = null;
        switch (position) {

            case CASE_FLASH_TOOL:
                mFragment = new FlashToolFragment();
                break;

            case CASE_ARCHIEVE:
                mFragment = new ArchiveFragment();
                break;

            default:
                mFragment = new FlashToolFragment();
                break;

        }
        getSupportActionBar().setTitle(getTitleBarString(position));

        FragmentManager mFragmentManager = getSupportFragmentManager();

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }
    }

    private String getTitleBarString(int position) {
        String str = "";
        switch (position) {
            case CASE_FLASH_TOOL:
                str = getString(R.string.flash_tools);
                break;
            case CASE_ARCHIEVE:
                str = getString(R.string.archive_manager);
                break;

            case CASE_SETTING:
                str = getString(R.string.settings);
        }
        return str;
    }

    @Override
    public void onItemClick(int position) {
        selectFragment(position);
    }

    public void showFolderChooser(String path) {
        new FolderChooserDialog.Builder(this)
                .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                .initialPath(path)  // changes initial path, defaults to external storage directory
                .show();
    }

    /**
     * FlashTool Activities
     */
    @Override
    public void onFolderSelection(File folder) {
        showToast(folder.getAbsolutePath());
        List<SystemConfiguration> configs = SystemConfiguration.find(SystemConfiguration.class, "key = ?", SystemPropertyKey.Archieve_DestinationPath.name());
        SystemConfiguration conf;
        if(configs != null && !configs.isEmpty()) {
            conf = configs.get(0);
            conf.setValue(folder.getAbsolutePath());
        } else {
            conf = new SystemConfiguration(SystemPropertyKey.Archieve_DestinationPath, folder.getAbsolutePath());
        }

        conf.save();
    }

    /**
     * Archive Manager Activities
     */



    /**
     * Other methods
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
}
