package com.woi.locationservice.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.woi.locationservice.MainActivity;
import com.woi.locationservice.R;

public class BgServ extends Service {
	private Notification noti = null;
	private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
			this);
	public final static String TAG = "BgServ";

	int notificationId = 0;
	boolean firstTime = true;
	boolean runnable;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		createNotification();
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
		stopNotification();
		Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}

	public void createNotification() {
		// Prepare intent which is triggered if the
		// notification is selected
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// Build notification
		// Actions are just fake
		noti = new Notification.Builder(this)
				.setContentTitle("New mail from " + "test@gmail.com")
				.setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent)
				.addAction(R.drawable.ic_launcher, "Call", pIntent)
				.addAction(R.drawable.ic_launcher, "More", pIntent)
				.addAction(R.drawable.ic_launcher, "And more", pIntent)
				.setTicker("Location Service is running... ").build();

		mBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("New mail from " + "test@gmail.com")
				.setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent)
				.addAction(R.drawable.ic_launcher, "Call", pIntent)
				.addAction(R.drawable.ic_launcher, "More", pIntent)
				.addAction(R.drawable.ic_launcher, "And more", pIntent)
				.setTicker("Location Service is running... ");

		noti = mBuilder.build();
		// hide the notification after its selected
		noti.flags |= Notification.FLAG_ONGOING_EVENT;
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, noti);

		runnable = true;
		update();
	}

	public void stopNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
		
		runnable = false;
	}

	public void update() {

		Thread background = new Thread(new Runnable() {
			@Override
			public void run() {
				// this will be done in the Pipeline Thread
				/* Do your GPS task here */
				// active the update handler
				int i = 0;
				do {
					if (firstTime) {
						mBuilder.setOnlyAlertOnce(true);
						firstTime = false;
					}
					mBuilder.setContentText("Testing " + i++);
					noti = mBuilder.build();
					// hide the notification after its selected
					noti.flags |= Notification.FLAG_ONGOING_EVENT;
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.notify(notificationId, noti);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} while (runnable);
			}
		});

		// start the background thread
		background.start();
	}
}
