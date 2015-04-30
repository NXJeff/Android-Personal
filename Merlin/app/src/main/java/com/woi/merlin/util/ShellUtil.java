package com.woi.merlin.util;

/**
 * Created by YeekFeiTan on 4/29/2015.
 */
public class ShellUtil {

    public static final String INIT_D_PATH = "/system/etc/init.d/";
    public static final String HOST_PATH = "/system/etc/hosts";
    public static final String HOST_DIR = "/system/etc";

    /** General **/
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
        if(recursive) {
            cmd+= "-R ";
        }
        return cmd + pathFrom + " " + pathTo;
    }

    /** Customised */

    public static final String getDPICmd(int dpi) {
        return "busybox sed -i 's|ro.sf.lcd_density=.*|ro.sf.lcd_density=" + dpi + "|g' /system/build.prop";
    }

    public static final String cloneInitDFolder(String pathFrom) {
        return copyFileOrDirectory(true, pathFrom, INIT_D_PATH);
    }

    public static final String copyHostFile(String pathFrom) {
        return copyFileOrDirectory(false, pathFrom, HOST_DIR);
    }

    public static final String applyPermissionInitDFolder() {
        return changePermission(true, 777, INIT_D_PATH);
    }

    public static final String applyPermissionHostFile() {
        return changePermission(false, 664, HOST_PATH);
    }




}
