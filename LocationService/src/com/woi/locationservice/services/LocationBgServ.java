package com.woi.locationservice.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.woi.locationservice.MainActivity;
import com.woi.locationservice.R;

public class LocationBgServ extends Service {
	private Notification noti = null;
	private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
			this);
	public final static String TAG = "BgServ";

	int notificationId = 0;
	boolean firstTime = true;
	boolean runnable;
	String provider = LocationManager.NETWORK_PROVIDER;
	Location location;
	LocationListener locationListener;

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
		stopLocationService();
		stopNotification();
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

		startLocationService();

		// runnable = true;
		// update();
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

	private void startLocationService() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onLocationChanged(Location location) {
				// Do work with new location. Implementation of this method will
				// be covered later.
				doWorkWithNewLocation(location);
				Log.d("Location updated",
						"Location: (" + location.getLongitude() + ", "
								+ location.getLatitude() + ") Provider: "
								+ location.getProvider() + " Accuracy:"
								+ location.getAccuracy());
			}

		};

		long minTime = 5 * 1000; // Minimum time interval for update in seconds,
									// i.e. 5 seconds.
		long minDistance = 10; // Minimum distance change for update in meters,
								// i.e. 10 meters.

		// Assign LocationListener to LocationManager in order to receive
		// location updates.
		// Acquiring provider that is used for location updates will also be
		// covered later.
		// Instead of LocationListener, PendingIntent can be assigned, also
		// instead of
		// provider name, criteria can be used, but we won't use those
		// approaches now.
		locationManager.requestLocationUpdates(getProviderName(), minTime,
				minDistance, locationListener);

		location = locationManager.getLastKnownLocation(provider);
	}

	public void stopLocationService() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Stop listening to location updates, also stops providers.
		locationManager.removeUpdates(locationListener);
	}

	/**
	 * Make use of location after deciding if it is better than previous one.
	 * 
	 * @param location
	 *            Newly acquired location.
	 */
	void doWorkWithNewLocation(Location location) {
		if (isBetterLocation(getLocation(), location)) {
			// If location is better, do some user preview.
			Toast.makeText(getApplication(),
					"Better location found: " + provider, Toast.LENGTH_SHORT)
					.show();
			setLocation(location);
			updateNotification(location);
		}
	}

	/**
	 * Time difference threshold set for one minute.
	 */
	static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;

	/**
	 * Decide if new location is better than older by following some basic
	 * criteria. This algorithm can be as simple or complicated as your needs
	 * dictate it. Try experimenting and get your best location strategy
	 * algorithm.
	 * 
	 * @param oldLocation
	 *            Old location used for comparison.
	 * @param newLocation
	 *            Newly acquired location compared to old one.
	 * @return If new location is more accurate and suits your criteria more
	 *         than the old one.
	 */
	boolean isBetterLocation(Location oldLocation, Location newLocation) {
		// If there is no old location, of course the new location is better.
		if (oldLocation == null) {
			return true;
		}

		// Check if new location is newer in time.
		boolean isNewer = newLocation.getTime() > oldLocation.getTime();

		// Check if new location more accurate. Accuracy is radius in meters, so
		// less is better.
		boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation
				.getAccuracy();
		if (isMoreAccurate && isNewer) {
			// More accurate and newer is always better.
			return true;
		} else if (isMoreAccurate && !isNewer) {
			// More accurate but not newer can lead to bad fix because of user
			// movement.
			// Let us set a threshold for the maximum tolerance of time
			// difference.
			long timeDifference = newLocation.getTime() - oldLocation.getTime();

			// If time difference is not greater then allowed threshold we
			// accept it.
			if (timeDifference > -TIME_DIFFERENCE_THRESHOLD) {
				return true;
			}
		}

		return false;
	}

	String getProviderName() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired
															// power consumption
															// level.
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy
														// requirement.
		criteria.setSpeedRequired(true); // Chose if speed for first location
											// fix is required.
		criteria.setAltitudeRequired(false); // Choose if you use altitude.
		criteria.setBearingRequired(false); // Choose if you use bearing.
		criteria.setCostAllowed(false); // Choose if this provider can waste
										// money :-)

		// Provide your criteria and flag enabledOnly that tells
		// LocationManager only to return active providers.
		return locationManager.getBestProvider(criteria, true);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void updateNotification(Location location) {
		if (firstTime) {
			mBuilder.setOnlyAlertOnce(true);
			firstTime = false;
		}
		mBuilder.setContentText("Location: (" + location.getLongitude() + ", "
				+ location.getLatitude() + ") Provider: "
				+ location.getProvider() + " Accuracy:"
				+ location.getAccuracy());
		noti = mBuilder.build();
		// hide the notification after its selected
		noti.flags |= Notification.FLAG_ONGOING_EVENT;
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, noti);
	}

}
