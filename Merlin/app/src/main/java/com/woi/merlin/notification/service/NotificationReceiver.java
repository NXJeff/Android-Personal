package com.woi.merlin.notification.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.woi.merlin.MainActivity;
import com.woi.merlin.R;

/**
 * Created by YeekFeiTan on 1/27/2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final int PERIOD = 10000;

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra("NotificationId", -1);
        Log.d("NotificationReceiver", " received id : " + id);

//        Intent userIntent = new Intent(context, MainActivity.class);
//        userIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent userPi = PendingIntent.getActivity(context, 0, userIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Notification n = new Notification(R.drawable.ic_launcher, alarm.getName(), alarmMsg.getDateTime());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(9011, mBuilder.build());


    }

//    public static void scheduleAlarms(Context context) {
//        AlarmManager mgr =
//                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, NotificationService.class);
//        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
//
//        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
//                SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi);
//    }
}
