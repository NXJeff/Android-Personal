package com.nxjeff.remoteprojectclient;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;

public class Keyboard extends Activity{
	Boolean isShift,softKey;
	private EditText etText;
	private remoteService s;
	private int repeat;
	private Button keyB,up,down,left,right;
	 
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.keyboard);
		 keyB = (Button)findViewById(R.id.a_keyboard);
		 up = (Button)findViewById(R.id.k_up);
		 down = (Button)findViewById(R.id.k_down);
		 left = (Button)findViewById(R.id.k_left);
		 right = (Button)findViewById(R.id.k_right);
		 initText();
		 
		//Keyboard button - to pop up the soft-keyboard
	        keyB.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
	        		 imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	        		 softKey = true;
	        	}
			});
	        
	      //UP - to send UP keyCode
	        up.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 401");
	        	}
			});
	        
	        
	      //Down - to send Down keyCode
	        down.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 402");
	        	}
			});
	        
	      //Left - to send Left keyCode
	        left.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 403");
	        	}
			});
	        
	      //Right - to send Right keyCode
	        right.setOnClickListener(new View.OnClickListener() {
	        	@Override
	            public void onClick(View v) {
	        		s.send("KEY 404");
	        	}
			});
	        
	     
		 
		 doBindService();
		 isShift = false;
	     softKey=false;
	     repeat=0;
	     
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
    	changed = "aaa"	;
        etText.setText(changed);

        et.setOnKeyListener(new OnKeyListener(){
        	@Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
        		Log.d("mouse_a", keyCode + " from onKey");
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
                    changed = "aaa";
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
                changed = "aaa";
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

                    boolean isShift = false;  //if shift is used
                    boolean isCtrl = false;   //if ctrl is used
                    
                    /** if(!c.toLowerCase().equals(c))
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
            
            */
			
			s.send("STRING " + c);

                
        }
    }
    
    
    private void sendKeyCode(int keyCode) {
    	
            	s.send("KEY "+keyCode);
            
    }
  //KEYBOARD END
    
	  //Service COmponents
    
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
