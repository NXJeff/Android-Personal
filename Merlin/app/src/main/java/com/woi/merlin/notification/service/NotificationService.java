package com.woi.merlin.notification.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.woi.merlin.MainActivity;
import com.woi.merlin.R;
import com.woi.merlin.enumeration.NotificationActionType;
import com.woi.merlin.util.DbUtil;

import java.util.Calendar;

import merlin.model.raw.DaoMaster;
import merlin.model.raw.DaoSession;
import merlin.model.raw.Reminder;
import merlin.model.raw.ReminderDao;

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
//        matcher = new IntentFilter();
//        matcher.addAction(CREATE);
//        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        daoSession = DbUtil.setupDatabase(this);
        String action = intent.getAction();
//        String actionType = intent.getExtra
        NotificationActionType actionType = (NotificationActionType) intent.getSerializableExtra("NotificationActionType");
        Long reminderId = intent.getLongExtra("ReminderId", 0l);
        if(reminderId == 0l) {
            return;
        }
//        Reminder reminder = (Reminder) intent.getSerializableExtra("Reminder");
        ReminderDao reminderDao = daoSession.getReminderDao();

        Reminder reminder = reminderDao.load(reminderId);

        execute(actionType, reminder);
//        String notificationId = intent.getStringExtra("notificationId");

//        execute("asd", "12");
//
//        if (matcher.matchAction(action)) {
//            execute(action, notificationId);
//        }
    }

    private void execute(NotificationActionType actionType, Reminder reminder) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i;
        PendingIntent pi;
        long PERIOD = 10000;

        i = new Intent(this, NotificationReceiver.class);
//        i.setAction("com.appsrox.remindme."+1);
        i.putExtra("NotificationId", reminder.getId());
        i.putExtra("NotificationType", "Reminder");
        i.putExtra("NotificationActionType", actionType);
        pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//        am.set(AlarmManager.RTC_WAKEUP, time, pi);

        if (actionType.equals(NotificationActionType.APPLY)) {
//        am.setRepeating(AlarmManager.ELAPSED_REALTIME,
//                SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi);
            am.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pi);
            Log.d("NotificationService", "alarm set");
        } else {
            am.cancel(pi);
        }

    }
}
