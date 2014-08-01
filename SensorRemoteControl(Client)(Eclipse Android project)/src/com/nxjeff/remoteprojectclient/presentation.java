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
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class presentation extends Activity{
	
	//ProgressBar pb_accelA,pb_accelB,pb_accelC;
	SensorManager m_sensormgr;
	//TextView accHead,accAccu,tv_orientationA,tv_accelA,tv_accelB,tv_accelC,tv_overview;
	TextView tv;
	 List<Sensor> m_sensorlist;
	 static final int FLOATTOINTPRECISION = 100;
	 private remoteService s;
	 String acc1,acc2,acc3;
	 public Runnable runnable;
	 Thread t;
	 int accStart;
	 Button startB,fsB,endB,keyB,preB,nextB,penB,blackB;
	 Boolean isShift,softKey,useAcc;
	 private EditText etText;
	 int repeat,repeat2;
	 private float THRESHOLD=15.00f;
	 private ToggleButton useAcctb;
	 Drawable pen,unpen,black,unblack;
	 

	 
	
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.presentation);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 initText();
		 hideKeyboard();
		 	
		 	tv = (TextView)findViewById(R.id.p_textView);
		 
		 	//gotoB = (Button)findViewById(R.id.p_goto);
		 	startB = (Button)findViewById(R.id.p_start);
		 	endB = (Button)findViewById(R.id.p_end);
		 	preB = (Button)findViewById(R.id.p_leftClick);
		 	nextB = (Button)findViewById(R.id.p_rightClick);
		 	keyB = (Button)findViewById(R.id.p_keyboard);
		 	penB=(Button)findViewById(R.id.p_pen);
		 	blackB=(Button)findViewById(R.id.p_black);
		 	useAcctb=(ToggleButton)findViewById(R.id.toggleButton2);
		 	useAcc=false;
	        
	        m_sensormgr  = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	        m_sensorlist =  m_sensormgr.getSensorList(Sensor.TYPE_ALL);
	        connectSensors();
	        doBindService();
	        accStart = 0; //START Sending the value
	        isShift = false;
	        softKey=false;
	        repeat=0;
	        repeat2=0;
	        
	        pen = getResources().getDrawable(R.drawable.pen);
	        unpen = getResources().getDrawable(R.drawable.unpen);
	        black = getResources().getDrawable(R.drawable.black);
	        unblack =getResources().getDrawable(R.drawable.unblack);
	        penB.setBackgroundDrawable(pen);
	        blackB.setBackgroundDrawable(black);
	        //Keyboard button - to pop up the soft-keyboard
	        keyB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
	        		 imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	        		 softKey = true;
	        	}
			});
			
	        //Start button - to start the presentation 
	        startB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("SLIDE START");
	        	}
			});
	        
	      
	        
	      //END button - to end the slide show
	        endB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("SLIDE END");
	        	}
			});
	        
	      //Previous button - to previous slide
	        preB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
						
							s.send("SLIDE PREVIOUS");	
	        		
	        		Log.d(getClass().getSimpleName(),"SLIDE PREVIOUS");
	        	}
			});
	        
	      //Next button - to next slide
	        nextB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		
	        		Log.d(getClass().getSimpleName(),"SLIDE NEXT");
	        		
	        				s.send("SLIDE NEXT");
							
	        		
	        	}
			});
	        
	      //PEN button - to draw on slide
	        penB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		if(penB.getBackground().equals(pen)){
	        			Log.d("","WAS PEN NOW UNPEN ");
	        			penB.setBackgroundDrawable(unpen);
	        			s.send("SLIDE PEN");
	        		}else if(penB.getBackground().equals(unpen)){
	        			Log.d("","WAS UNPEN NOW PEN");
	        			penB.setBackgroundDrawable(pen);
	        			s.send("SLIDE UNPEN");
	        		}

	        	}
			});
	        
	      //Black/UnBlack button - to black up the slide
	        blackB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		
	        		if(blackB.getBackground().equals(black)){
	        			blackB.setBackgroundDrawable(unblack);
	        			s.send("SLIDE BLACK");
	        		}else if(blackB.getBackground().equals(unblack)){
	        			blackB.setBackgroundDrawable(black);
	        			s.send("SLIDE UNBLACK");
	        		}
	        	}
			});
	        
	        //Toggle button for on/off accelerometer sensor
	        
	        useAcctb.setOnClickListener(new OnClickListener()
	        {
	        public void onClick(View v)
	        {
	        	if (((ToggleButton) v).isChecked()) {
	                useAcc=true;
	                tv.setText("");
	            } else {
	            	useAcc=false;
	            	 tv.setText("Accelerometer Sensor is disabled.");
	            }
	        
	        }
	        
	        
	        });
	        useAcctb.setChecked(false);
	        
	        
	        
	 }
	      
	        
	
	        
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
	        		 
	        		 if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
	        			 
	        			 acc1= String.format("%.2f",event.values[0]);
	        			 acc2=String.format("%.2f",event.values[1]);
	        			 acc3=String.format("%.2f",event.values[2]);
                         
	        		 }
	        		 /*If the function is turned on by user */
	        		 if(useAcc==true){ 
	        		 float i = Round(Float.parseFloat(acc1),4);
	        		 
	        		 if(i>THRESHOLD){
	        			 tv.setText("Left shake detected. Value: "+i);
							preB.performClick();
	                 }
	                 else if(i<-THRESHOLD){
	                		 nextB.performClick();
	                     tv.setText("Right shake detected. Value: "+i);
	                 }
	        		}
	        	 }
	        	 
	        	 
	        	 
	        	 
	             
	        	 
	        	 @Override
	                public void onAccuracyChanged(Sensor sensor, int accuracy) {
	                        
	                        
	                }
	        };
	        
	        //to return the round result
	        public static float Round(float Rval, int Rpl) {
	            float p = (float)Math.pow(10,Rpl);
	            Rval = Rval * p;
	            float tmp = Math.round(Rval);
	            return (float)tmp/p;
	            }
	        
	        
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
	        	EditText et = (EditText) this.findViewById(R.id.etAdvancedText);
	        	this.etText =et;
	        	et.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	        	
	        	
	        	 
	        	changed = "a  "	;
                etText.setText(changed);
                
                
               
                et.setOnKeyListener(new OnKeyListener(){
                	@Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                		//Log.d("mouse_a", keyCode + " character(code) to send");
                           if(keyCode==67 ){
                        	   
                        	   repeat++;
                        	   
                        	   //To avoid repeated keyCode generated
                        	   if(repeat==2){
                        	   //Log.d("mouse_a", keyCode + " character(code) to send"+repeat);
                        	   sendKeyCode(keyCode);
                        	   repeat=0;
                        	   }
                        	   //s.send("KEY "+keyCode);
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
                        
                        Log.d("mouse_a", "'"+change + "' character to send");  
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
	        
	       
	        
	     
	        
	        @Override
	        protected void onPause() {
	                m_sensormgr.unregisterListener(senseventListener);
	                super.onPause();
	                
	                accStart = 0;
	                
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
	                   case Sensor.TYPE_ACCELEROMETER     : snsType="TYPE_ACCELEROMETER";break;
	                   default: snsType="UNKNOWN_TYPE "+sen.getType();break;
	            }
	            
	            sensorInfo=sen.getName()+"\n";
	            
	            sensorInfo+="Type: "+snsType+"\n";
	            sensorInfo+="MaxRange: "+sen.getMaximumRange()+"\n";
	          
	            return sensorInfo;     
	           }
	        
	        protected void connectSensors(){
	        	//if(m_sensormgr==null){
                //m_sensormgr.unregisterListener(senseventListener);
	        	//}
                if(!m_sensorlist.isEmpty()){
                Sensor snsr;
                
                for(int i=0;i<m_sensorlist.size();i++){
                    snsr=m_sensorlist.get(i);
                    
                    if(snsr.getType()==Sensor.TYPE_ACCELEROMETER){
                    	
                        m_sensormgr.registerListener(senseventListener, snsr, SensorManager.SENSOR_DELAY_UI);
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
	    		bindService(new Intent(this, com.nxjeff.remoteprojectclient.remoteService.class),mConnection, Context.BIND_AUTO_CREATE);
	    	}
	    	
	    	
                

}
