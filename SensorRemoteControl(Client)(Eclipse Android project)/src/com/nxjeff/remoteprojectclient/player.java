package com.nxjeff.remoteprojectclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class player  extends Activity{
	private remoteService s;
	private ImageButton vol1,vol2,vol3,prev,next,play,stop;
	
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.player);
		 doBindService();
		 
		 vol1 = (ImageButton)findViewById(R.id.volumeminus);
		 vol2 = (ImageButton)findViewById(R.id.volumemute);
		 vol3 = (ImageButton)findViewById(R.id.volumeplus);
		 prev = (ImageButton)findViewById(R.id.prev);
		 next = (ImageButton)findViewById(R.id.next);
		 play = (ImageButton)findViewById(R.id.playpause);
		 stop = (ImageButton)findViewById(R.id.stop);
		 
		 
		 vol1.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View arg0) {
					s.send("KEY 301");
				}
			});
		 
		 vol2.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View arg0) {
					s.send("KEY 302");
				}
			});
		 
		 vol3.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View arg0) {
					s.send("KEY 303");
				}
			});
		 
		 prev.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View arg0) {
					s.send("KEY 304");
				}
			});
		 
		 next.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View arg0) {
					s.send("KEY 305");
				}
			});
		 
		 play.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View arg0) {
					s.send("KEY 306");
				}
			});
		 
		 stop.setOnClickListener(new OnClickListener() {
			 
				@Override
				public void onClick(View arg0) {
					s.send("KEY 307");
				}
			});
		 
		 
		 
		 
		 
		 
	}
	
	public ServiceConnection mConnection = new ServiceConnection(){
		public void onServiceConnected(ComponentName className, IBinder binder) {
			s=((remoteService.MyBinder)binder).getService();
			
		}
		public void onServiceDisconnected(ComponentName className) {
			s=null;
		
		}
	};
	
	public void doBindService(){
		bindService(new Intent(this, com.nxjeff.remoteprojectclient.remoteService.class),mConnection, Context.BIND_AUTO_CREATE);
		
	}

}
