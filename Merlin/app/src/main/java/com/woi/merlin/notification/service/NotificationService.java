package com.woi.merlin.notification.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Log;

import merlin.DaoMaster;
import merlin.DaoSession;

/**
 * Created by YeekFeiTan on 1/27/2015.
 */
public class NotificationService extends IntentService {
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    private static final String TAG = "MERLIN-SRV";

    private IntentFilter matcher;
    private DaoSession daoSession;

    public NotificationService() {
        super("NotificationService");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        setupDatabase();
        String action = intent.getAction();
        String notificationId = intent.getStringExtra("notificationId");

        execute("asd", "12");

        if (matcher.matchAction(action)) {
            execute(action, notificationId);
        }
    }

    private void execute(String action, String notificationId) {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i;
        PendingIntent pi;
        long PERIOD = 10000;

        i = new Intent(this, NotificationReceiver.class);
        i.setAction("com.appsrox.remindme."+1);
        i.putExtra("NOTIFICATIONID", 1);
        pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//        am.set(AlarmManager.RTC_WAKEUP, time, pi);

        am.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi);
        Log.d("NotificationService", "alarm setted");


    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "merlin-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        Log.d(TAG, "Initialise database completed on " + this.getClass().getSimpleName());
    }
}
