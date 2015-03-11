package com.woi.merlin.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import merlin.model.raw.DaoMaster;
import merlin.model.raw.DaoSession;

/**
 * Created by YeekFeiTan on 2/11/2015.
 */
public class DbUtil {

    final static String TAG = "MERLIN.DBUTIL";

    public static DaoSession setupDatabase(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "merlin-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        Log.d(TAG, "Initialise database completed on " + context.getClass().getSimpleName());

        return daoSession;
    }


}
