package com.woi.merlin.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.woi.merlin.R;
import com.woi.merlin.component.FolderChooserDialog;
import com.woi.merlin.util.MediaUtil;
import com.woi.merlin.util.SharedPreferenceUtil;
import com.woi.merlin.util.ShellUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeekFeiTan on 4/28/2015.
 */
public class PhoneMgrFragment extends Fragment implements
        FolderChooserDialog.FolderSelectCallback {

    private static final String PREFERENCE_DPI = "preferred_dpi";
    private static final String PREFERENCE_CLONE_INIT_D_PATH = "clone_init_d_path";
    private static final String PREFERENCE_HOSTS_PATH = "hosts_path";
    private static final String PREFERENCE_MEDIA_PROFILE_XML = "media_profile_xml_path";
//    private static final String HOSTFILEFROM = "/storage/sdcard1/Others/hosts"; //TODO REMOVE

    //FileChooser indicator
    private static final String FILE_CHOOSER_CHOOSE_RESTORE_DIR = "FC_1";
//    private static final String FILE_CHOOSER_HOSTS = "FC_2";
//    private static final String FILE_CHOOSER_MEDIA_PROFILE = "FC_3";

    EditText etDPI = null;
    TextView tvCloneInitDPath, tvHostsPath, tvMediaProfilePath;
    String cloneInitPath, hostsPath, mediaProfilePath;
    Button backupAllBtn, restoreAllBtn;
    List<String> cmds = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_mgr, container,
                false);

        etDPI = (EditText) view.findViewById(R.id.preferredDPI);
        tvCloneInitDPath = (TextView) view.findViewById(R.id.cloneInitDFolderPath);
        tvHostsPath = (TextView) view.findViewById(R.id.hostsFilePath);
        tvMediaProfilePath = (TextView) view.findViewById(R.id.mediaProfileFilePath);
        backupAllBtn = (Button) view.findViewById(R.id.phone_mgr_backup_btn);
        restoreAllBtn = (Button) view.findViewById(R.id.phone_mgr_restore_btn);

        tvCloneInitDPath.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFolderChooser(PREFERENCE_CLONE_INIT_D_PATH, cloneInitPath, false);
            }
        });

        tvHostsPath.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFolderChooser(PREFERENCE_HOSTS_PATH, hostsPath, true);
            }
        });

        tvMediaProfilePath.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFolderChooser(PREFERENCE_MEDIA_PROFILE_XML, mediaProfilePath, true);
            }
        });

        view.findViewById(R.id.phone_mgr_apply_value_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAction();
            }
        });

        backupAllBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShellUtil.backupAllSystemFiles(getActivity());
            }
        });

        restoreAllBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFolderChooser(FILE_CHOOSER_CHOOSE_RESTORE_DIR, MediaUtil.getPhoneMgrDirectoryAbsolutePath(), false);
            }
        });


        getPreferences();

        ShellUtil.getRootAccess();

        return view;
    }

    @Override
    public void onFolderSelection(String id, final File folder) {
        switch (id) {
            case PREFERENCE_CLONE_INIT_D_PATH:
                cloneInitPath = folder.getAbsolutePath();
                tvCloneInitDPath.setText(cloneInitPath);
                break;
            case PREFERENCE_HOSTS_PATH:
                hostsPath = folder.getAbsolutePath();
                tvHostsPath.setText(hostsPath);
                break;
            case PREFERENCE_MEDIA_PROFILE_XML:
                mediaProfilePath = folder.getAbsolutePath();
                tvMediaProfilePath.setText(mediaProfilePath);
                break;
            case FILE_CHOOSER_CHOOSE_RESTORE_DIR:
                new MaterialDialog.Builder(getActivity())
                        .title("Restore")
                        .content("Are you sure you want to restore this backup? " + folder.getName())
                        .positiveText("Proceed")
                        .negativeText("Back")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                dialog.dismiss();
                                ShellUtil.restoreAllSystemFiles(getActivity(), folder.getAbsolutePath());
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    private void showFolderChooser(String id, String path, boolean isFile) {
        Bundle args = new Bundle();
        args.putString("FileURI", path);
        args.putBoolean("isFile", isFile);
        args.putString("id", id);

        FolderChooserDialog dialog = new FolderChooserDialog();
        dialog.setArguments(args);
        dialog.show(this);
    }

    private void showToast(String message) {
        Toast mToast = null;
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void showToast(@StringRes int message) {
        showToast(getString(message));
    }

    private void applyAction() {
        if (validate()) {
            savePreferences();
            generateCmds();
        }
    }

    private boolean validate() {

        boolean valid = true;

        if (etDPI.getText().toString() != null && !etDPI.getText().toString().isEmpty()) {
            try {
                int dpi = Integer.parseInt(etDPI.getText().toString());
                if (dpi < 180 || dpi > 480) {
                    valid = false;
                }
            } catch (NumberFormatException nfe) {
                valid = false;
            }

            if (!valid) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.select_dpi)
                        .content(R.string.dpi_error)
                        .positiveText(R.string.ok)
                        .show();

                return valid;
            }
        }

        if (tvCloneInitDPath.getText().toString() != null && !tvCloneInitDPath.getText().toString().isEmpty()) {
            int count = MediaUtil.getNumberOfFilesInDir(tvCloneInitDPath.getText().toString());
            if (count == 0) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.warning)
                        .content(R.string.file_not_found_in_path)
                        .positiveText(R.string.ok)
                        .show();
            }
        }

        return valid;
    }

    private void savePreferences() {
        SharedPreferenceUtil.setPreference(getActivity(), PREFERENCE_DPI, Integer.parseInt(etDPI.getText().toString()));
        if (cloneInitPath != null && !cloneInitPath.isEmpty()) {
            SharedPreferenceUtil.setPreference(getActivity(), PREFERENCE_CLONE_INIT_D_PATH, cloneInitPath);
        }
        if (hostsPath != null && !hostsPath.isEmpty()) {
            SharedPreferenceUtil.setPreference(getActivity(), PREFERENCE_HOSTS_PATH, hostsPath);
        }
        if (mediaProfilePath != null && !mediaProfilePath.isEmpty()) {
            SharedPreferenceUtil.setPreference(getActivity(), PREFERENCE_MEDIA_PROFILE_XML, mediaProfilePath);
        }
    }

    private void generateCmds() {
        cmds.clear();
        //Mount System
        cmds.add(ShellUtil.getMountSystemCmd());

        //InitD copy
        String path = "";
        if (cloneInitPath.substring(cloneInitPath.length() - 1) != "/") {
            path = cloneInitPath + "/*";
        } else {
            path = cloneInitPath + "*";
        }
        cmds.add(ShellUtil.cloneInitDFolder(path));
        cmds.add(ShellUtil.applyPermissionInitDFolder());

        //Hosts copy
        if (MediaUtil.isFileExist(hostsPath)) {
            cmds.add(ShellUtil.copyToSystemEtc(hostsPath));
            cmds.add(ShellUtil.applyPermissionHostFile());
        }

        if (MediaUtil.isFileExist(mediaProfilePath)) {
            cmds.add(ShellUtil.copyToSystemEtc(mediaProfilePath));
        }


        //Apply DPI value
        cmds.add(ShellUtil.getDPICmd(SharedPreferenceUtil.getIntPreference(getActivity(), PREFERENCE_DPI)));
        cmds.add(ShellUtil.getUnmountSystemCmd());


        //Run commands
        ShellUtil.runCommandsAsRoot(cmds, getActivity());

    }

    private void getPreferences() {
        int dpi = SharedPreferenceUtil.getIntPreference(getActivity(), PREFERENCE_DPI);
        if (dpi != 0) {
            etDPI.setText(String.valueOf(dpi));
        }

        if (SharedPreferenceUtil.getPreference(getActivity(), PREFERENCE_CLONE_INIT_D_PATH) != null) {
            cloneInitPath = SharedPreferenceUtil.getPreference(getActivity(), PREFERENCE_CLONE_INIT_D_PATH);
            tvCloneInitDPath.setText(cloneInitPath);
        }

        if (SharedPreferenceUtil.getPreference(getActivity(), PREFERENCE_HOSTS_PATH) != null) {
            hostsPath = SharedPreferenceUtil.getPreference(getActivity(), PREFERENCE_HOSTS_PATH);
            tvHostsPath.setText(hostsPath);
        }

        if (SharedPreferenceUtil.getPreference(getActivity(), PREFERENCE_MEDIA_PROFILE_XML) != null) {
            mediaProfilePath = SharedPreferenceUtil.getPreference(getActivity(), PREFERENCE_MEDIA_PROFILE_XML);
            tvMediaProfilePath.setText(mediaProfilePath);
        }
    }

    //---------------------------


}
