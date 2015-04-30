package com.woi.merlin.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String HOSTFILEFROM = "/storage/sdcard1/Others/hosts"; //TODO REMOVE

    EditText etDPI = null;
    TextView tvCloneInitDPath = null;
    String cloneInitPath = null;
    List<String> cmds = new ArrayList<>();
    Process proc = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_mgr, container,
                false);

        view.findViewById(R.id.cloneInitDFolderPath).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFolderChooser();
            }
        });

        view.findViewById(R.id.phone_mgr_apply_value_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAction();
            }
        });

        etDPI = (EditText) view.findViewById(R.id.preferredDPI);
        tvCloneInitDPath = (TextView) view.findViewById(R.id.cloneInitDFolderPath);

        getPreferences();
        //Root
        try {
            proc = Runtime.getRuntime().exec("su");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return view;
    }

    @Override
    public void onFolderSelection(File folder) {
        cloneInitPath = folder.getAbsolutePath();
        tvCloneInitDPath.setText(cloneInitPath);
    }

    private void showFolderChooser() {
        new FolderChooserDialog().show(this);
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
    }

    private void generateCmds() {
        cmds.add(ShellUtil.getMountSystemCmd());
        String path = "";
        if (cloneInitPath.substring(cloneInitPath.length() - 1) != "/") {
            path = cloneInitPath + "/*";
        } else {
            path = cloneInitPath + "*";
        }
        cmds.add(ShellUtil.cloneInitDFolder(path));
        cmds.add(ShellUtil.copyHostFile(HOSTFILEFROM));
        cmds.add(ShellUtil.applyPermissionInitDFolder());
        cmds.add(ShellUtil.applyPermissionHostFile());
        cmds.add(ShellUtil.getDPICmd(SharedPreferenceUtil.getIntPreference(getActivity(), PREFERENCE_DPI)));
        cmds.add(ShellUtil.getUnmountSystemCmd());
        try {
            runCommandsAsRoot(cmds);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
    }

    //---------------------------

    public void runCommandsAsRoot(List<String> cmds) throws IOException {


        DataOutputStream oStream = new DataOutputStream(proc.getOutputStream());

        for (String cmd : cmds) {
            oStream.writeBytes(cmd + "\n");
        }

        oStream.writeBytes("exit\n");
        oStream.flush();
        new MaterialDialog.Builder(getActivity())
                .title(R.string.complete)
                .content(R.string.operation_done)
                .positiveText(R.string.ok)
                .show();
    }

}
