package com.woi.merlin.notification.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.woi.merlin.MainActivity;
import com.woi.merlin.R;
import com.woi.merlin.enumeration.NotificationActionType;
import com.woi.merlin.util.DbUtil;

import merlin.model.raw.DaoSession;
import merlin.model.raw.Reminder;

/**
 * Created by YeekFeiTan on 1/27/2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final int PERIOD = 10000;
    private DaoSession daoSession;

    @Override
    public void onReceive(Context context, Intent intent) {
        daoSession = DbUtil.setupDatabase(context);
        long id = intent.getLongExtra("NotificationId", -1);
        String notificationType = intent.getStringExtra("NotificationType");
        NotificationActionType actionType = (NotificationActionType) intent.getSerializableExtra("NotificationActionType");
        Log.d("NotificationReceiver", " received id : " + id);

        String title = "";
        String content = "";
        String[] events = null;

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        if (notificationType.equals("Reminder")) {
            Reminder reminder = daoSession.getReminderDao().load(id);
            title = reminder.getSubject();
            content = "Swipe down see details and dismiss the reminder.";

            inboxStyle.setBigContentTitle("Event details:");
            events = new String[1];
            if(reminder.getDescription()!= null) {
                events[0] = reminder.getDescription();
            }

//            events[1] = "";

        }

        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                inboxStyle.addLine(events[i]);
            }
        }

        Intent dismissIntent = new Intent(context, MainActivity.class);
//        dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
        PendingIntent piDismiss = PendingIntent.getService(context, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(context, MainActivity.class);
//        snoozeIntent.setAction(CommonConstants.ACTION_SNOOZE);
        PendingIntent piSnooze = PendingIntent.getService(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.noti_bell_50)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(events[0]))
                        .addAction(R.drawable.noti_tick_50,
                                "Dismiss", piDismiss)
                        .addAction(R.drawable.noti_clock_50,
                                "Snooze", piSnooze);

//        mBuilder.setStyle(inboxStyle);


        // Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(context, MainActivity.class);



// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(dimissIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
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
