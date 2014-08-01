package com.nxjeff.remoteprojectclient;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class mouse_c extends Activity{
	
	SensorManager m_sensormgr;
	TextView oriHead,oriAccu,tv_orientationA,tv_orientationB,tv_orientationC,tv_overview;
	 List<Sensor> m_sensorlist;
	 private remoteService s;
	 int oriStart;
	 String ori1,ori2,ori3;
	 Thread t;
	Button startB,keyB,leftB,rightB;
	Boolean isShift,softKey;
	 private EditText etText;
	 int repeat;
	 public Runnable runnable,rLeftDown,rLeftUp,rRightDown,rRightUp;
	 private Handler handler = new Handler();
	 private int SLEEPTHRES =15;
	 Drawable leftD,rightD,leftD2,rightD2;
	 
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.mouse_c);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//Keyboard INIT
		 initText();
		 hideKeyboard();
		 
		 startB = (Button)findViewById(R.id.c_start);
		 leftB=(Button)findViewById(R.id.c_leftClick);
		 rightB=(Button)findViewById(R.id.c_rightClick);
		 oriHead = (TextView) this.findViewById(R.id.TextView_oriHead);
		 oriAccu = (TextView) this.findViewById(R.id.oriAccuracy);
		 
		 leftD = getResources().getDrawable(R.drawable.left_b);
		 	rightD = getResources().getDrawable(R.drawable.right_b);
		 	leftD2 = getResources().getDrawable(R.drawable.left_b2);
		 	rightD2 = getResources().getDrawable(R.drawable.right_b2);
		 
	        tv_orientationA = (TextView) this.findViewById(R.id.TextView_oriA); 
	        tv_orientationB = (TextView) this.findViewById(R.id.TextView_oriB); 
	        tv_orientationC = (TextView) this.findViewById(R.id.TextView_oriC); 
	        tv_overview= (TextView) this.findViewById(R.id.TextViewOverview);
	        tv_overview.setText("");
	        
	        m_sensormgr  = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	        m_sensorlist =  m_sensormgr.getSensorList(Sensor.TYPE_ORIENTATION);
	        
	        keyB = (Button)findViewById(R.id.c_keyboard);
	        
	        connectSensors();
	        doBindService();
	        oriStart = 0;
	        isShift = false;
	        softKey=false;
	        repeat=0;
	        
	    
	        
	      //Start button - to start setting the value to the server
	        startB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		oriStart=1;
	        		t.start(); 
	        		startB.setVisibility(View.INVISIBLE);
	        	}
			});
	        
	        //Keyboard button - to pop up the soft-keyboard
	        keyB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) { 
	        		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
	        		 imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	        		 SLEEPTHRES=20;
	        		 softKey = true;
	        	}
			});
	        
	        
	      //Left Mouse Button 
	        leftB.setOnTouchListener(new View.OnTouchListener() {
	        	@Override
	        	 public boolean onTouch(View v, MotionEvent ev) {
                    return onLeftTouch(ev);
            }
			});
	        
	        //Right Mouse Button
	        rightB.setOnTouchListener(new View.OnTouchListener() {
	        	@Override
	        	public boolean onTouch(View v, MotionEvent ev) {
                    return onRightTouch(ev);
            }
			});
	        
	      //UI runnables
	        this.rLeftDown = new Runnable(){
	        	public void run(){
	        		leftB.setBackgroundDrawable(leftD2);
	        	}
	        };
	        this.rLeftUp = new Runnable(){
	        	public void run(){
	        		leftB.setBackgroundDrawable(leftD);
	        	}
	        };
	        this.rRightDown = new Runnable(){
	        	public void run(){
	        		rightB.setBackgroundDrawable(rightD2);
	        	}
	        };
	        this.rRightUp = new Runnable(){
	        	public void run(){
	        		rightB.setBackgroundDrawable(rightD);
	        	}
	        };
	        
	        //Thread start
	        runnable = new Runnable() {
				@Override
				public void run() {
					
					while(oriStart==1){
					if(ori1!=null && ori2!=null && ori3!=null )
		        	{
		        		s.send("MOUSE ORI "+ori1+" "+ori2+" "+ori3);
		        		try {
							Thread.sleep(SLEEPTHRES);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	  }
					}
				}
	        };
	        
	        try  
	        {  
	            t=new Thread(null,runnable);  	            	
	        }catch(Exception e)  {
	        }
	        
	        
	 }//Thread END
	//SENSOR REGISTRATION AND INITIATION
	        SensorEventListener senseventListener = new SensorEventListener(){
	        	 @Override
	                public void onSensorChanged(SensorEvent event) {
	        		 String accuracy;
	        		 switch(event.accuracy){
                     case SensorManager.SENSOR_STATUS_ACCURACY_HIGH: accuracy="SENSOR_STATUS_ACCURACY_HIGH";break;
                     case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM: accuracy="SENSOR_STATUS_ACCURACY_MEDIUM";break;
                     case SensorManager.SENSOR_STATUS_ACCURACY_LOW: accuracy="SENSOR_STATUS_ACCURACY_LOW";break;
                     case SensorManager.SENSOR_STATUS_UNRELIABLE: accuracy="SENSOR_STATUS_UNRELIABLE";break;
                     default: accuracy="UNKNOWN";
                     }
	        		 
	        		 if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
                         oriAccu.setText(accuracy);
                      ori1=String.format("%.1f",event.values[0]);
                      ori2=String.format("%.1f",event.values[1]);
                      ori3=String.format("%.1f",event.values[2]);

                     tv_orientationA.setText("Value 1: "+ori1);
                     tv_orientationB.setText("Value 2: "+ori2);
                     tv_orientationC.setText("Value 3: "+ori3);
              
	        		 }
	        	 }
	        	 
	        	 @Override
	                public void onAccuracyChanged(Sensor sensor, int accuracy) {
	                        
	                        
	                }
	        };
	        
	        
	        
	      //KEYBOARD START
	        /*
	         * hideKeyboard() will hide the keyboard until the user click on the editText again or the Keyboard button
	         */
	        private void hideKeyboard(){
	        	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	        }
	        
	        /*
	         * initText() initial the special EditText(etAdvancedText) which used to obtain the keystrokes that
	         * in order to send to the server. ONLY (ASCII characters)
	         */
	        
	        String changed = "";
	        private void initText() {
	        	EditText et = (EditText) this.findViewById(R.id.c_etAdvancedText);
	        	this.etText =et;
	        	et.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	        	
	        	
	        	 
	        	changed = "a  "	;
                etText.setText(changed);
               
                et.setOnKeyListener(new OnKeyListener(){
                	@Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                		//[(Log.d("mouse_a", keyCode + " character(code) to send");
                           if(keyCode==67 ){
                        	   
                        	   repeat++;
                        	   
                        	 //To avoid repeated keyCode generated
                        	   if(repeat==2){
                        	   //Log.d("mouse_a", keyCode + " character(code) to send"+repeat);
                        	   sendKeyCode(keyCode);
                        	   repeat=0;
                        	   }
                        	   
                           }
                            
                            changed = "a  ";
                            etText.setText(changed);
                            return false;
                    }

               
                });
                
                et.addTextChangedListener(new TextWatcher(){
                    public void onTextChanged(CharSequence cs, int start, int before,
                                    int count)
                    {
                    	 
                    	if (cs.toString().equals(changed))
                        {

                                etText.requestFocus();
                                etText.setSelection(2);
                                return;
                        }
                        changed = null;

                        //String change;
                        String change=cs.toString().substring(start,start+count);
                        // onAdvancedTextChanged(s, start, count);
                        
                        //Log.d("mouse_a", "'"+change + "' character to send");  
                        sendString(change);
                        changed = "a  ";
                       etText.setText(changed);
                    }
                                    

                    public void afterTextChanged(Editable cs)
                    {
                            
                    }
                    public void beforeTextChanged(CharSequence cs, int start, int count, int after)
                    {
                            
                    }
                });}
                
	        
                
                
	        	
	        
	        private synchronized void sendString(String keys)
	        {
	                if(keys.equals("a  ")) return;
	               
	                for (int i = 0; i < keys.length(); i++)
	                {
	                        String c = keys.substring(i, i + 1);

	                        boolean isShift = false;
	                        boolean isCtrl = false;
	                        
	                        if(!c.toLowerCase().equals(c))
	                        {
	                                isShift = true;
	                                c = c.toLowerCase();
	                        }
	                        
	                        int key = 0;

	                        if(c.equals(" "))
	                                key = 62;
	                        if(c.equals("\n"))
	                                key = 66;
	                        if(c.equals("\t"))
	                        {
	                                key = 45;
	                                isCtrl = true;
	                        }
	                        

	                        if (c.equals("_"))
	                        {
	                                key = 69;
	                                isShift = true;
	                        }else
	                        if (c.equals("\"")) //shift+'
	                        {
	                                key = 75;
	                                isShift = true;
	                        }else
	                        if(c.equals("|")){//SH _ BACK_SLASH
	                        	key=73;
	                        	isShift=true;
	                        }else
	                        if(c.equals("?")){ //SH + SLASH
		                        	key=76;
		                        	isShift=true;
		                     }else
		                     
			                if(c.equals("]")){ //OPEN_BRACKET
			                        	key=72;
			                        	
			                 }else
			                if(c.equals("[")){//CLOSE_BRACKET
			                        	key=71;
			                        	
			                 }else 
			                if(c.equals("~")){ //BACK_QUOTE
			                        	key=68;
			                        	isShift=true;
			                 }else
			                if(c.equals("%")){ //% shift +5
			                        	key=12;
			                        	isShift=true;
			                 }else
			                if(c.equals("!")){ //shift+1
			                        	key=8;
			                        	isShift=true;
			                 }else
			                if(c.equals("@")){ //SHIFT+2
			                        	key=9;
			                        	isShift=true;
			                 }else
			                if(c.equals("#")){ //SHIFT+3
			                        	key=10;
			                        	isShift=true;
			                 }else
			                if(c.equals("$")){ //SH+4
			                        	key=11;
			                        	isShift=true;
			                 }else
			                if(c.equals("^")){ //SH+6
			                        	key=13;
			                        	isShift=true;
			                 }else
			                if(c.equals("&")){ //sh+7
			                        	key=14;
			                        	isShift=true;
			                 }else
			                if(c.equals("*")){ //sh+8
			                        	key=15;
			                        	isShift=true;
			                 }else
			                if(c.equals("(")){ //sh+9
			                        	key=16;
			                        	isShift=true;
			                 }else
			                if(c.equals(")")){ //sh+0
			                        	key=17;
			                        	isShift=true;
			                 }else
			                if(c.equals("+")){ //sh+=
			                        	key=70;
			                        	isShift=true;
			                 }else
			                if(c.equals(":")){ //
			                        	key=74;
			                        	isShift=true;
			                 }else
			                if(c.equals(">")){ //Greater
			                        	key=56;
			                        	isShift=true;
			                 }else
			                if(c.equals("<")){ //Lesser
			                        	key=55;
			                        	isShift=true;
			                 }else
			                	 if(c.equals("`")){ //
			                        	key=68;
			                 }else
			                	 if(c.equals("-")){ //
			                        	key=69;
			                 }else
			                	 if(c.equals("=")){ //
			                        	key=70;
			                 }else
			                	 if(c.equals("/")){ //
			                        	key=76;
			                 }else
			                	 if(c.equals("[")){ //
			                        	key=71;
			                 }else
			                	 if(c.equals("]")){ //
			                        	key=72;
			                 }else
			                	 if(c.equals("\\")){ //
			                        	key=73;
			                 }else
			                	 if(c.equals(";")){ //
			                        	key=74;
			                 }else
			                	 if(c.equals("'")){ //
			                        	key=75;
			                 }else
			                	 if(c.equals(",")){ //
			                        	key=55;
			                 }else
			                	 if(c.equals(".")){ //
			                        	key=56;
			                 }else
			                	 if(c.equals("{")){ //
			                        	key=71;
			                        	isShift=true;
			                 }else 
			                	 if(c.equals("}")){ //
			                        	key=72;
			                        	isShift=true;
			                	 }
			                	 
	                        
	                        
	                        if (key == 0)
                                for (int z = 0; z < 1024; z++)
                                {
                                        if (db.charmap.isPrintingKey(z))
                                        {
                                                if (new Character(Character.toChars(db.charmap.get(z, 0))[0]).toString().equals(c))
                                                {
                                                        key = z;
                                                        break;
                                                }
                                        }
                                }
	                        
	                        
	                        
	                        
	                
	                if(isShift){
                       s.send("KEY SHIFT "+key);
                       //Log.d("mouse_a", "SHIFT '"+key + "' code send"); 
	                }else{
	                	s.send("KEY "+key);
	                	//Log.d("mouse_a", "'"+key + "' code send"); 
	                }
	                
	                

                        
                }
	        }
	        
	        
	        private void sendKeyCode(int keyCode) {
	        	
	                	s.send("KEY "+keyCode);
	                
	        }
	      //KEYBOARD END
	        
 //MOUSE LEFT AND RIGHT COMMAND SENDING START
	        
	        //WHEN LEFT BUTTON PRESSED
	        private boolean onLeftTouch(MotionEvent ev) {
	                switch (ev.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                        this.leftBDown();
	                        break;
	                case MotionEvent.ACTION_UP:
	                        this.leftBUp();
	                        break;
	                }
	                return true;
	        }
	        
	        //WHEN RIGHT BUTTON PRESSED

	        private boolean onRightTouch(MotionEvent ev) {
                switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                        this.rightBDown();
                        break;
                case MotionEvent.ACTION_UP:
                        this.rightBUp();
                        break;
                }
                return true;
        }
	        
	        //OnLeftMouse Down 
	        private synchronized void leftBDown() {
                String msg="MOUSE LEFT DOWN";
                try {
                	 s.send(msg);s.send(msg);
                }catch(Exception ex) {
                		 Log.d(getClass().getSimpleName(), "Exception on MOUSE LEFTD "+ex.toString());
                }
                // graphical feedback
                this.handler.post(this.rLeftDown);
        }
	        //OnLeftMouse UP / Released
	        private synchronized void leftBUp() {
                String msg="MOUSE LEFT UP";
                try {
                        s.send(msg);s.send(msg);
                } catch (Exception ex) {
                        Log.d(getClass().getSimpleName(), "Exception on MOUSE LEFTU "+ex.toString());
                }       
                //graphic
                this.handler.post(this.rLeftUp);
        }
	        
	        //OnRightMouse Down / Pressed
	        private void rightBDown() {
                String msg = "MOUSE RIGHT DOWN";
                try {
                        s.send(msg); s.send(msg);
                } catch (Exception ex) {
                	 Log.d(getClass().getSimpleName(), "Exception on MOUSE RIGHTD "+ex.toString());
                }
                //graphic
                this.handler.post(this.rRightDown);
        }
	        //onRightMouse UP / Released
        private void rightBUp() {
                String msg = "MOUSE RIGHT UP";
                try {
                        s.send(msg); s.send(msg);
                } catch (Exception ex) {
                	Log.d(getClass().getSimpleName(), "Exception on MOUSE RIGHTU "+ex.toString());
                }
                // graphical feedback
                this.handler.post(this.rRightUp);
        }
        
        
        
        

        //MOUSE LEFT AND RIGHT COMMAND SENDING END
	        
	        
	        
	        @Override
	        protected void onPause() {
	                m_sensormgr.unregisterListener(senseventListener);
	                super.onPause();
	                
	                oriStart = 0;
	                
	        }
	        @Override
	        protected void onResume() {
	                connectSensors();
	                super.onResume();
	        }
	        
	        @Override
	        protected void onDestroy() {
	        	super.onDestroy();
	        		//m_sensormgr.unregisterListener(senseventListener);
	        	try{
	                t.stop();
	                }catch(Exception e){
	                	
	                }
	                
	        }
	      //To Detect and Display the hardware information
	        protected String getSensorInfo(Sensor sen){
	            String sensorInfo="INVALID";
	            String snsType;
	            
	            switch(sen.getType()){                 
	                   case Sensor.TYPE_ORIENTATION       : snsType="TYPE_ORIENTATION";break;
	                   default: snsType="UNKNOWN_TYPE "+sen.getType();break;
	            }
	            
	            sensorInfo=sen.getName()+"\n";
	            sensorInfo+="Type: "+snsType+"\n";
	            sensorInfo+="MaxRange: "+sen.getMaximumRange()+"\n";
	            return sensorInfo;     
	           }
	        
	        protected void connectSensors(){
	        	Log.i(getClass().getSimpleName(), "sensor 1 ");
	                //m_sensormgr.unregisterListener(senseventListener);
	                Log.i(getClass().getSimpleName(), "sensor  2");	
                if(!m_sensorlist.isEmpty()){
                Sensor snsr;
                tv_overview.setText("");
                for(int i=0;i<m_sensorlist.size();i++){
                    snsr=m_sensorlist.get(i);
                    
                    if(snsr.getType()==Sensor.TYPE_ORIENTATION){
                        oriHead.setText(getSensorInfo(snsr));
                        
                        m_sensormgr.registerListener(senseventListener, snsr, SensorManager.SENSOR_DELAY_GAME);
                }
                }
                }
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
	    		bindService(new Intent(this,  com.nxjeff.remoteprojectclient.remoteService.class),mConnection, Context.BIND_AUTO_CREATE);
	    	}
                

}
