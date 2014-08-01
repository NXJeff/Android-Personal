package com.nxjeff.remoteprojectclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class RemoteSplash extends Activity {
	
	protected int SPLASHTIME = 1500; 
	
	private Thread splashTread;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
	    
	    
	    final RemoteSplash sPlashScreen = this; 
	    
	    // thread for displaying the SplashScreen
	    splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {	            	
	            	synchronized(this){
	            		wait(SPLASHTIME);
	            	}
	            	
	            } catch(InterruptedException e) {} 
	            finally {
	                finish();
	                
	                Intent i = new Intent();
	                i.setClass(sPlashScreen, RemoteProjectClientActivity.class);
	        		startActivity(i);
	        		interrupted();
	            }
	        }
	    };
	    
	    splashTread.start();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(splashTread){
	    		splashTread.notifyAll();
	    	}
	    }
	    return true;
	}
	
}
