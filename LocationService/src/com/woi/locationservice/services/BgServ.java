package com.woi.locationservice.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BgServ extends Service {
	public final static String TAG = "BgServ";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG)
				.show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		// Note: You can start a new thread and use it for long background
		// processing from here.
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}
}
