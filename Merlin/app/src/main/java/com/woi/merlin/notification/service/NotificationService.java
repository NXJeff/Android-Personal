package com.woi.merlin.notification.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by YeekFeiTan on 1/27/2015.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "I ran!");
    }
}
