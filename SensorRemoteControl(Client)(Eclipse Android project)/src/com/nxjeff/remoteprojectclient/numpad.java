package com.nxjeff.remoteprojectclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class numpad extends Activity{
	
	private Button n1,n2,n3,n4,n5,n6,n7,n8,n9,n0,nplus,nsub,nmul,ndiv,ndot,nper,nback,nenter;
	private remoteService s;
	
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 setContentView(R.layout.numpad);
		 doBindService();
		 
		 n0 = (Button)findViewById(R.id.n_0);
		 n1 = (Button)findViewById(R.id.n_1);
		 n2 = (Button)findViewById(R.id.n_2);
		 n3 = (Button)findViewById(R.id.n_3);
		 n4 = (Button)findViewById(R.id.n_4);
		 n5 = (Button)findViewById(R.id.n_5);
		 n6 = (Button)findViewById(R.id.n_6);
		 n7 = (Button)findViewById(R.id.n_7);
		 n8 = (Button)findViewById(R.id.n_8);
		 n9 = (Button)findViewById(R.id.n_9);
		 ndot = (Button)findViewById(R.id.n_dot);
		 nplus = (Button)findViewById(R.id.n_addition);
		 nsub = (Button)findViewById(R.id.n_substract);
		 nmul = (Button)findViewById(R.id.n_multiply);
		 ndiv = (Button)findViewById(R.id.n_divide);
		 nper = (Button)findViewById(R.id.n_percentages);
		 nenter = (Button)findViewById(R.id.n_enter);
		 nback = (Button)findViewById(R.id.n_back);
		 
		 
		//Set onclick listener to the buttons
	        n0.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 7");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n1.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 8");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n2.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 9");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n3.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 10");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        
	        
	        n4.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 11");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n5.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 12");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n6.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 13");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n7.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 14");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n8.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 15");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        n9.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 16");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        ndot.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 56");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        nper.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY SHIFT 12");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        nenter.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 66");
	        		Log.d(getClass().getSimpleName(),"KEY 66");
	        	}
			});
	        
	        nback.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 67");
	        		Log.d(getClass().getSimpleName(),"KEY 67");
	        	}
			});
	        
	        nplus.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY SHIFT 70");
	        		Log.d(getClass().getSimpleName(),"KEY SHIFT 70");
	        	}
			});
	        
	        nsub.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 69");
	        		Log.d(getClass().getSimpleName(),"KEY 69");
	        	}
			});
	        
	        nmul.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY SHIFT 15");
	        		Log.d(getClass().getSimpleName(),"KEY ");
	        	}
			});
	        
	        ndiv.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 76");
	        		Log.d(getClass().getSimpleName(),"KEY ");
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
