package com.nxjeff.remoteprojectclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class scanlist extends Activity implements Runnable{
	public static final ArrayList<String> servers = new ArrayList<String>();
	public static final ArrayList<String> ports = new ArrayList<String>();
	public static final ArrayList<String>  comNames= new ArrayList<String>();
	
	public static TextView subTitle,subError;
	String ip;
	ArrayAdapter<String> adapter;
	ListView list;
	String dyn; 
	Thread t;
	private remoteService s;


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanlist);
        subTitle=(TextView)findViewById(R.id.scanlist_subTitle);
        subError=(TextView)findViewById(R.id.scanlist_error);
        subError.setVisibility(View.GONE);
        
        list=(ListView)findViewById(R.id.scan_list);	
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, servers);
        list.setAdapter(adapter);
        doBindService();
        
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	int ipAddress = wifiInfo.getIpAddress();
    	ip = intToIp(ipAddress);
    	
    	if((ipAddress & 0xFF)!=0){
    		 
                t=new Thread(this);  
                t.start();        
            
    	}else{
    		subError.setTextColor(Color.RED);
    		subError.setText("Wireless Network is not connected. To perform a network scan, device is required to connect to a Wi-Fi network. Please turn on Wi-Fi and connect to your Wireless Network. ");
    		subError.setVisibility(View.VISIBLE);
    	}
    	
    	
    	list.setOnItemClickListener(new OnItemClickListener() {
            //@Override
            public void onItemClick(AdapterView arg0, View view, int position, long id) {	//listview click function

            	//debug//Toast.makeText(RemoteProjectClientActivity.this, "You have pressed "+db.savedHosts.get(position), Toast.LENGTH_SHORT).show();
            	Log.d(getClass().getSimpleName(), ""+servers.get(position)+ports.get(position)+comNames.get(position));
            	try {
					db.setIp(servers.get(position),ports.get(position),comNames.get(position));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				s.connectServer(servers.get(position),ports.get(position));
				onDestroy();
				

            }

        });
    	
    }
    
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	String text = (String)msg.obj;
        	subTitle.setText(text);
        	}
    };
    

    	
        public void run() { 
    		String iIPv4 = "";
            String test = "";
            
            servers.clear();
            ports.clear();
            comNames.clear();
            boolean found=false;


            iIPv4=ip;
            // IP stuff.
            String IPv4Start = "", IPv4End = "";
            iIPv4 = iIPv4.substring(0, iIPv4.lastIndexOf("."));
            iIPv4 += ".";
            IPv4Start = iIPv4 + "1";
            IPv4End = iIPv4 + "254";


            PrintWriter out = null;
            BufferedReader in = null;
            
            

            // Loop to scan each address on the local subnet
            for (int i = 1; i < 255; i++) {

                try {
                    dyn=iIPv4+i;
                    Socket mySocket = new Socket();
                    SocketAddress address = new InetSocketAddress(iIPv4 + i, 55979);

                    mySocket.connect(address, 50); //set Timeout
                    
                    out = new PrintWriter(mySocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(
                            mySocket.getInputStream()));
                    out.println("SCAN ");
                    //Send progress
                    
                    
                    String fromServer;
                    String[] fromServerInfo;
                    while ((fromServer = in.readLine()) != null) {
                    	fromServerInfo=fromServer.split(" ");
                        Log.d("RESULT", fromServerInfo[0]+" "+fromServerInfo[1]+" "+fromServerInfo[2]+" "+fromServerInfo[3]);
                        if (fromServerInfo[0].equals("SensorRemote")) {
                        	//Found=true;
                            servers.add(iIPv4 + i);
                            ports.add(fromServerInfo[3]);
                            comNames.add(fromServerInfo[2]);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                	adapter.notifyDataSetChanged();

                               }
                           });
                            
                            mySocket.close();
                            
                            break;
                            
                        }
                    }
                    mySocket.close();
                    out.close();
                    in.close();

                } catch (UnknownHostException e) {
                } catch (IOException e) {
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                subTitle.setText("Scanning.. ("+dyn+")");
                    }
                });
            }
            
            if(servers.isEmpty()){
            runOnUiThread(new Runnable() {
                public void run() {
                	
                    String text = "No hosts found on the network. Make sure Sensor Remote Server application's services are running on the PC and connected to the same network.";
                    subError.setTextColor(Color.RED);
            		subError.setText(text);
            		subError.setVisibility(View.VISIBLE);
            		subTitle.setText("Scan completed.. ("+dyn+")");
            		
               }
           });
            }

    	}
    
    	
    	  
        
    
    public String intToIp(int i) {

    	   return ( i & 0xFF)+"." +((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) + "." + ((i >> 24 ) & 0xFF );
    	}
    
    @Override
    public void onDestroy(){
    	//t.interrupt();
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
	
	public void doUnBindService(){
		unbindService(mConnection);
	}
	
}
