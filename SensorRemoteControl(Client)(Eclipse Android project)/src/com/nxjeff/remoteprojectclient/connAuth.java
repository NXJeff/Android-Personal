package com.nxjeff.remoteprojectclient;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class connAuth extends Activity{
	private remoteService s;
	private String conn_result;
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.connauth);
		 doBindService();
		 final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		 final EditText input = new EditText(this);
		 conn_result="";
		 try{	
	        	Bundle extras = getIntent().getExtras();

	        	conn_result = extras.getString("conn_result");
	        	
	  
	        }catch(Exception e){
	        	Log.i(getClass().getSimpleName(), "Bundle exception :"+e);
	        }
		 
		 if(conn_result.equals("1")){
			 
		 input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		 alert.setView(input);    //edit text added to alert
		 alert.setTitle("Password Required");   //title setted
		 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
			   String value = input.getText().toString();
			   
			   try{
			   		s.send("PASSKEY "+createHash(value));
			   		onDestroy();
			   }catch(Exception e){
				   
			   }
			   }
			 });

			 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int whichButton) {
			     // Canceled.
			   }
			 });
		 alert.show();
		 }else if(conn_result.equals("2")){
			 Toast.makeText(connAuth.this, "Wrong Passkey. Please try again.",Toast.LENGTH_LONG).show();
			 
			 
			 alert.setMessage("Wrong PassKey. Please try again.");   
			 alert.setTitle("Access Denied");   //title setted
			 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
					 onDestroy();
				 }
			 });
			 alert.show();
			 
		 }
	}
	
	//MD5 HASHING FOR AUTHENTICATION PURPOSE
	static public String createHash(String word) {
        String created_hash = null;
        try {
           MessageDigest md5 = MessageDigest.getInstance("MD5");
           md5.update(word.getBytes());
           BigInteger hash = new BigInteger(1, md5.digest());
           created_hash = hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
           System.out.println("Exception : " + e.getMessage());
        }
        return created_hash;
     }
	
	@Override
    public void onDestroy(){
    	this.finish();
    	super.onDestroy();
    	
    }
	
	
	public ServiceConnection mConnection = new ServiceConnection(){
		public void onServiceConnected(ComponentName className, IBinder binder) {
			s=((remoteService.MyBinder)binder).getService();
			Log.d("","Connected. Service Connection.");
		}
		public void onServiceDisconnected(ComponentName className) {
			s=null;
		
		}
	};
	
	public void doBindService(){
		bindService(new Intent(this, com.nxjeff.remoteprojectclient.remoteService.class),mConnection, Context.BIND_AUTO_CREATE);
		
	}
	

}
