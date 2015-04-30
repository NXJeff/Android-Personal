package com.woi.merlin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Created by YeekFeiTan on 4/28/2015.
 */
public class SharedPreferenceUtil {

    private static final String APP_DIR_NAME = "Merlin";
    private static final String BACKUP_DIR_NAME = "Backup";

    public static String getPreference(Context context, String key) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        return preferences.getString(key, null);
    }

    public static Boolean getBooleanPreference(Context context, String key) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        return preferences.getBoolean(key, false);
    }

    public static int getIntPreference(Context context, String key) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        return preferences.getInt(key, 0);
    }

    public static long getLongPreference(Context context, String key) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        return preferences.getLong(key, 0l);
    }

    public static void setPreference(Context context, String key, String value) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPreference(Context context, String key, int value) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setPreference(Context context, String key, Boolean value) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setPreference(Context context, String key, Long value) {
        // Access the default SharedPreferences
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // The SharedPreferences editor - must use commit() to submit changes
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void backupPreferences(Context context) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> keys = preferences.getAll();
        Properties properties = new Properties();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            properties.setProperty(key, value);
        }
        try {
            File backupPath = new File(
                    Environment
                            .getExternalStorageDirectory(),
                    APP_DIR_NAME + File.separator + BACKUP_DIR_NAME + File.separator + "backup.xml");

            FileOutputStream fileOut = new FileOutputStream(backupPath);
            properties.storeToXML(fileOut, "Merlin Preferences");
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restorePreferences(Context context) {
        try {
            File backupPath = new File(
                    Environment
                            .getExternalStorageDirectory(),
                    APP_DIR_NAME + File.separator + BACKUP_DIR_NAME + File.separator + "backup.xml");
            FileInputStream fileInput = new FileInputStream(backupPath);
            Properties properties = new Properties();
            properties.loadFromXML(fileInput);
            fileInput.close();

            Enumeration enuKeys = properties.keys();
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);
                editor.putString(key, value);
                editor.commit();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
