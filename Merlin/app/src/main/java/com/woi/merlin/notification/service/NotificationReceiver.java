package com.woi.merlin.notification.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by YeekFeiTan on 1/27/2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final int PERIOD = 5000;

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleAlarms(context);
    }

    public static void scheduleAlarms(Context ctxt) {
        AlarmManager mgr =
                (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(ctxt, 0, i, 0);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi);
    }
}
