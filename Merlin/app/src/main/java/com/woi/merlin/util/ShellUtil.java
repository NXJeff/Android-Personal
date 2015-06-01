package com.woi.merlin.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.woi.merlin.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeekFeiTan on 4/29/2015.
 */
public class ShellUtil {

    public static final String INIT_D_PATH = "/system/etc/init.d/";
    public static final String HOST_PATH = "/system/etc/hosts";
    public static final String MEDIA_PROFILE_PATH = "/system/etc/media_profiles.xml";
    public static final String SYSTEM_ETC = "/system/etc";
    public static final String INIT_D_DIRECTORY_NAME = "INITD";
    static Process proc = null;

    public static void runCommandsAsRoot(List<String> cmds, Context context) {

        try {
            DataOutputStream oStream = new DataOutputStream(proc.getOutputStream());

            for (String cmd : cmds) {
                oStream.writeBytes(cmd + "\n");
            }

            oStream.writeBytes("exit\n");
            oStream.flush();
            new MaterialDialog.Builder(context)
                    .title(R.string.complete)
                    .content(R.string.operation_done)
                    .positiveText(R.string.ok)
                    .show();
        } catch (IOException ioe) {
            new MaterialDialog.Builder(context)
                    .title("Error")
                    .content("Process failed: " + ioe.toString())
                    .positiveText(R.string.ok)
                    .show();
        }
    }

    public static final void getRootAccess() {
        //Root
        try {
            proc = Runtime.getRuntime().exec("su");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * General *
     */
    public static final String getMountSystemCmd() {
        return "busybox mount -o remount,rw /system";
    }

    public static final String getUnmountSystemCmd() {
        return "busybox mount -o remount,ro /system";
    }

    public static final String changePermission(boolean recursive, int permissions, String path) {
        String cmd = "busybox chmod ";
        if (recursive) {
            cmd += "-R ";
        }
        return cmd + permissions + " " + path;
    }

    public static final String copyFileOrDirectory(boolean recursive, String pathFrom, String pathTo) {
        String cmd = "busybox cp ";
        if (recursive) {
            cmd += "-R ";
        }
        return cmd + pathFrom + " " + pathTo;
    }

    /**
     * Customised
     */

    public static final String getDPICmd(int dpi) {
        return "busybox sed -i 's|ro.sf.lcd_density=.*|ro.sf.lcd_density=" + dpi + "|g' /system/build.prop";
    }

    public static final String cloneInitDFolder(String pathFrom) {
        return copyFileOrDirectory(true, pathFrom, INIT_D_PATH);
    }

    public static final String copyToSystemEtc(String pathFrom) {
        return copyFileOrDirectory(false, pathFrom, SYSTEM_ETC);
    }

    public static final String applyPermissionInitDFolder() {
        return changePermission(true, 777, INIT_D_PATH);
    }

    public static final String applyPermissionHostFile() {
        return changePermission(false, 664, HOST_PATH);
    }

    public static final void backupAllSystemFiles(Context context) {

        String newBackupDestination = MediaUtil.getPhoneMgrNewBackUpDirectory().getAbsolutePath();
        List<String> cmds = new ArrayList<>();
        cmds.add(copyFileOrDirectory(true, INIT_D_PATH, newBackupDestination + File.separator + INIT_D_DIRECTORY_NAME));
        cmds.add(copyFileOrDirectory(false, HOST_PATH, newBackupDestination));
        cmds.add(copyFileOrDirectory(false, MEDIA_PROFILE_PATH, newBackupDestination));
        runCommandsAsRoot(cmds, context);
    }

    public static final void restoreAllSystemFiles(Context context, String backupDestination) {
        List<String> cmds = new ArrayList<>();
        cmds.add(getMountSystemCmd());
        cmds.add(copyFileOrDirectory(true, backupDestination + File.separator + INIT_D_DIRECTORY_NAME + "/*", INIT_D_PATH));
        cmds.add(copyFileOrDirectory(false, backupDestination + File.separator + "hosts", HOST_PATH));
        cmds.add(copyFileOrDirectory(false, backupDestination + File.separator + "media_profiles.xml", SYSTEM_ETC));
        cmds.add(applyPermissionInitDFolder());
        cmds.add(applyPermissionHostFile());
        cmds.add(getUnmountSystemCmd());
        runCommandsAsRoot(cmds, context);
    }
}
