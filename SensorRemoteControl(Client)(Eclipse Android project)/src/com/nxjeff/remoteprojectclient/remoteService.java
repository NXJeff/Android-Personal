package com.nxjeff.remoteprojectclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class remoteService extends Service{
	private String ip_add;
	private String c_name;
	private String port_no;
	private String ipofdevice;
	private PrintWriter toServer;
	private BufferedReader fromServer;
	Socket socket;
	private final IBinder mBinder = new MyBinder();
	public Runnable runnable;
	int state;
	byte[] send_data = new byte[50];
	DatagramSocket clientSocket;
	DatagramPacket send_packet;
	InetAddress clientAddr;
	Intent mainIntent,auth,toast;
	PrintWriter out = null;
    BufferedReader in = null;
	
	
	public void onCreate() {
		super.onCreate();
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	int ipAddress = wifiInfo.getIpAddress();
    	ipofdevice = intToIp(ipAddress);
		
	}
	
	public String intToIp(int i) {  //USED BY CONVERTING INTO IPV4 ADDRESS

 	   return ( i & 0xFF)+"." +((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) + "." + ((i >> 24 ) & 0xFF );
 	}
	
	public int getState(){
		return state;
	}
	
	
	public void connectServer(final String ip,String port) {	
		final byte[] buffer = new byte[50];
		state=0;
		ip_add=ip;
		port_no=port;
		Log.i(getClass().getSimpleName(), "connectServerStart");
		mainIntent = new Intent(this,main_menu.class);
		auth = new Intent(this,connAuth.class);
		runnable = new Runnable() {
			@Override
			public void run() {
				try{
					
						
	                    Socket TCPSocket = new Socket();
	                    SocketAddress address = new InetSocketAddress(ip_add, 55979);

	                    TCPSocket.connect(address); //set Timeout
	                    
	                    out = new PrintWriter(TCPSocket.getOutputStream(), true);
	                    in = new BufferedReader(new InputStreamReader(
	                            TCPSocket.getInputStream()));
	                    
	
					clientSocket = new DatagramSocket();
					clientAddr = InetAddress.getByName(ip_add);
					String msg="REQUEST "+ipofdevice;
					Log.d(getClass().getSimpleName(),"msg: "+ipofdevice);
					send_data = msg.getBytes();
					send_packet = new DatagramPacket(send_data, send_data.length, clientAddr, Integer.parseInt(port_no));
					clientSocket.send(send_packet);
					DatagramSocket serverSocket = new DatagramSocket((Integer.parseInt(port_no))-1); 
					while(true){
						try{
							//Receive response from server
							DatagramPacket packet = new DatagramPacket(buffer, buffer.length );
							serverSocket.receive(packet);
							String command = new String(buffer);
							Log.d("","Command: "+command);
							if(command.contains("PASS")){
								auth();
							}else if(command.contains("OK")){
								send("PRINT A client is connected.");
								mainMenu();		
							}else if(command.contains("WRONG")){
								wrongPassword();
							}
							
						}catch(Exception e){
							
						}
					}
					
					
				}catch(IOException ie) {
					Log.e("",""+ie);
					
				}
				
			}
			
		};
		try  
        {  
            Thread t=new Thread(null,runnable);  
            t.start();  
            	
        }catch(Exception e)  
        {  
        	
        }
	}
	
	public void mainMenu(){
		mainIntent.putExtra("ip_add",ip_add );
		mainIntent.putExtra("port_no",port_no);
		mainIntent.putExtra("c_name",db.com_name);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		db.openSession();
		startActivity(mainIntent);
		
	}
	
	public void auth(){
		auth.putExtra("conn_result", "1");
		auth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(auth);
	}
	
	public void wrongPassword(){
		auth.putExtra("conn_result", "2");
		auth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(auth);
	}
	
	public void send(String msg){
		send_data = msg.getBytes();
		send_packet = new DatagramPacket(send_data, send_data.length, clientAddr, Integer.parseInt(port_no));
		
		runnable = new Runnable() {
			@Override
			public void run() {
				try {
					clientSocket.send(send_packet);
				} catch (IOException e) {
					Log.i(getClass().getSimpleName(), "sending exception: "+e);
				}
			}
		};
		try  
        {  
            Thread t=new Thread(null,runnable);  
            t.start();  
        }catch(Exception e)  
        {}
	}
	
	public void sendTCP(final String msg){
		
		runnable = new Runnable() {
			@Override
			public void run() {
				try {
					out.println(msg);
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "sending exception: "+e);
				}
			}
		};
		try  
        {  
            Thread t=new Thread(null,runnable);  
            t.start();  
        }catch(Exception e)  
        {}
	}
	
	public void disconnect(){
		//unused
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy(); 
		if(socket!=null){
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i(getClass().getSimpleName(), "Excpetion on Socket close - remoteService.java");
			}
		}
		Log.i(getClass().getSimpleName(), "Socket close on destroy");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public class MyBinder extends Binder {
		remoteService getService(){
			return remoteService.this;
		}
	}
	
	public static void main(String args[]) throws Exception{
		
		
	}


}
