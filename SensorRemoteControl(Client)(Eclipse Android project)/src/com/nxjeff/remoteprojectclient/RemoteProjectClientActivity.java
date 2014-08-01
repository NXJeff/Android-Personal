package com.nxjeff.remoteprojectclient;




import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class RemoteProjectClientActivity extends Activity {
	
	String port_no,new_port;
	static final ArrayList<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
	SimpleAdapter adapter;
	Intent connecting;
	Intent scanning;
	EditText ip_add;
	EditText c_name;
	EditText input_port;
	TextView subError;
	int state;
	private remoteService s;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button btnConnect =(Button)findViewById(R.id.btnConnect);
        final Button btnScan =(Button)findViewById(R.id.btnScan);
        ip_add=(EditText)findViewById(R.id.input_ip_add);
        c_name=(EditText)findViewById(R.id.input_com_name);
        input_port=(EditText)findViewById(R.id.input_port);
        subError=(TextView)findViewById(R.id.main_error);
        subError.setVisibility(View.GONE);
        
        final ListView hostlist = (ListView)findViewById(R.id.recent_list);
        
        db.init(this);
        adapter = new SimpleAdapter(this, list,R.layout.list_row,
        		new String[] {"comName","ipaddress"},
        		new int[] {R.id.comName,R.id.ipaddress}
        		);

        		populateList();
        		hostlist.setAdapter(adapter);
        		
        
        		doBindService();
        scanning = new Intent(this,scanlist.class);
        port_no="55979";  
        
        //check SharedPreferences for the IPs
        
        /**
         * This will help the application to get back the most recent connected IP address, 
         * computer name and its port on the TextField of inputs 
         */
        db.init(this.getApplicationContext());
        if(db.ip!=null) {
        	ip_add.setText(db.ip);
        }
        try{
        input_port.setText(db.port);
        }catch(Exception e){}
        try{
        c_name.setText(db.com_name);
        }catch(Exception e){}
        
        
        hostlist.setOnItemClickListener(new OnItemClickListener() {
	        //@Override
	        public void onItemClick(AdapterView arg0, View view, int position, long id) {	//listview click function

	        	//debug//Toast.makeText(RemoteProjectClientActivity.this, "You have pressed "+db.savedHosts.get(position), Toast.LENGTH_SHORT).show();

	            try{
	            	ip_add.setText(""+db.savedHosts.get(position));
	        	}catch(Exception e){
	        	
	        	}
	            
	            try{
	            input_port.setText(""+db.portList.get(position));
	            }catch(Exception e){}
	            try{
	            c_name.setText(db.comName.get(position));
	            }catch(Exception e){}
	            onConnectButton();
	            
	        }

	    });
        
    	/*
    	 * onCreateContextMenu enable a context menu appear when the user long-pressed the list Item.
    	 * use it to select whether connect or delete.
    	 */
        hostlist.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {	
    		@Override
    	public void onCreateContextMenu(ContextMenu menu, View v,
        	    ContextMenuInfo menuInfo) {
        	  
        	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        	    menu.setHeaderTitle(""+db.savedHosts.get(info.position));
        	    String[] menuItems = {"Connect","Delete"};
        	    for (int i = 0; i<menuItems.length; i++) {
        	      menu.add(Menu.NONE, i, i, menuItems[i]);
        	    }
        	  
    		}
    	});
        
        
        


        
       
        
        //Button Operations
        btnConnect.setOnClickListener(new View.OnClickListener() { //Button 1
            public void onClick(View v) {
            	onConnectButton();
            }}
            	);
        
        btnScan.setOnClickListener(new View.OnClickListener() { //Button 2
            public void onClick(View v) {
            	
            	startActivity(scanning);

                // Perform action on click
            }
        });
        
        if(db.firstuse.equals("YES")){
        	firstUse();
        }
        
        
        //Hide KEyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
    }
    
    private void populateList() {
    	list.clear();
    	String comName;
    	String ipaddress,port;
    	if(!db.savedHosts.isEmpty()){
    	if(db.savedHosts.size()>0){
    	for(int i=0;i<db.savedHosts.size();i++){
    	try{
    		comName=db.comName.get(i);
    	}catch(Exception e){
    		comName="";
    	}
    	try{
    		ipaddress=db.savedHosts.get(i);
    		port=db.portList.get(i);
    	}catch(Exception e){
    		ipaddress="";
    		port="";
    	}
    		HashMap<String,String>  temp = new HashMap<String,String>();
    		
    		temp.put("comName",""+comName);
    		temp.put("ipaddress",ipaddress+":"+port);
        	list.add(temp);
        	}
    	}
    }
    	
    }
    
    public void onConnectButton(){
    	subError.setVisibility(View.GONE);
    	
    	if(validatePort(input_port.getText().toString())==false){
    		subError.setTextColor(Color.RED);
        	subError.setText("The Port Number that entered is not valid. Please enter valid port number valid from 1~65535");
    		subError.setVisibility(View.VISIBLE);
    	}else{
    		//change if port changed.
    	try{
        if(input_port.getText().length()>0)
        {
        	port_no=input_port.getText().toString();
        }}catch(NumberFormatException e){
        	port_no="55979";
        }
        
        try{
        	String ip=ip_add.getText().toString();
        	String com_name=c_name.getText().toString();
        	
    		db.setIp(ip,port_no,com_name);
    		populateList();
    		adapter.notifyDataSetChanged();
        }catch(Exception e){
        	Log.e("SensorRemote","Ip cannot be inserted to the list. ");
        }
        s.connectServer(ip_add.getText().toString(),port_no);
        
        
        	subError.setTextColor(Color.RED);
        	subError.setText("Unable to connect to the Server ("+ip_add.getText().toString()+":"+port_no+"), it can be the Wireless Network is not connected or the Server is not available.");
    		subError.setVisibility(View.VISIBLE);
        
        
    
    	}
    }
    
    public void hideErrorMsg(){
    	subError.setVisibility(View.GONE);
    }
    
  //OnResume then update the GUI again
    @Override
    protected void onResume() {
    	try{
    		dbUpdate();
    		populateList();
    		hideErrorMsg();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
            super.onResume();
    }
    
  //Update data from DB
    public void dbUpdate(){
    	if(db.ip!=null) {
        	ip_add.setText(db.ip);
        }
        try{
        input_port.setText(db.port);
        }catch(Exception e){}
        try{
        c_name.setText(db.com_name);
        }catch(Exception e){}
    }
    
    //Service Components
    
    public ServiceConnection mConnection = new ServiceConnection(){
		public void onServiceConnected(ComponentName className, IBinder binder) {
			s=((remoteService.MyBinder)binder).getService();
			checkLastSession();
			
		}
		public void onServiceDisconnected(ComponentName className) {
			s=null;
			
		}
	};
	
	public void doBindService(){
		bindService(new Intent(this, com.nxjeff.remoteprojectclient.remoteService.class),mConnection, Context.BIND_AUTO_CREATE);
		
	}
	
	/*
	 * onContextItemSelected response to the onCreateContextMenu which operate the selections whether
	 * connect to it or delete the history.
	 */
	public boolean onContextItemSelected(MenuItem aItem) { 
		/* Get the selected item out of the Adapter by its position. */ 
    	AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem 
    	.getMenuInfo(); 
    	switch (aItem.getItemId()) { 
    	case 0:   /* The information will be paste into TextViews (ip_add, input_port,c_name) after user select connect. */ 
    		try{
            	ip_add.setText(""+db.savedHosts.get(menuInfo.position));
        	}catch(Exception e){
        		
        	}
            try{
            input_port.setText(""+db.portList.get(menuInfo.position));
            }catch(Exception e){}
            try{
            c_name.setText(db.comName.get(menuInfo.position));
            }catch(Exception e){}
            onConnectButton();
            break;
    	case 1: 
    		try {
    			/**
            	 * Display an error message when there is a problem on connecting to the server.
            	 */
            	subError.setTextColor(Color.RED);
            	subError.setText(db.comName.get(menuInfo.position)+""+db.savedHosts.get(menuInfo.position)+":"+db.portList.get(menuInfo.position)+" have been removed from the history.");
        		subError.setVisibility(View.VISIBLE);
        		/* Delete from the db and then update the UI */
				db.removeSavedHost(""+db.savedHosts.get(menuInfo.position), ""+db.comName.get(menuInfo.position));
				populateList();
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				
				Log.i(getClass().getSimpleName(), "onContextItemSelected exception: "+e);
			}
    	
    	return true; /* true means: "we handled the event". */ 
    	} 
    	return false; 
    	}
	
	//Validate Port number
    private boolean validatePort(String input) {
        boolean valid = true;
        int temp = 0;

        try {
            temp = Integer.parseInt(input);
            if (temp < 0 || temp > 65535) {
                valid = false;
            }
        } catch (NumberFormatException nfe) {
            valid = false;
        }

        return valid;
    }
	
	public void checkLastSession(){
		//LAST SESSION WILL CONTINUE
        if(db.session.contains("YES")){
        	Log.d(getClass().getSimpleName(),"Session YES "+db.ip+db.port);
        	try{
        	s.connectServer(db.ip.toString(),db.port.toString());
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }else if(db.session.contains("NO")){
        	Log.d(getClass().getSimpleName(),"Session NO");
        }
	}
	
	//First Use Dialog
	public void firstUse(){
		final Dialog aboutDialog = new Dialog(RemoteProjectClientActivity.this);
		aboutDialog.setContentView(R.layout.about);
		aboutDialog.setTitle("First Use of Sensor Remote Control");
		aboutDialog.setCancelable(true);

        TextView text = (TextView) aboutDialog.findViewById(R.id.TextView01);
        text.setText("Thank you for choosing Sensor Remote Control.\n\n First Install the latest Sensor Remote Control Server on your computer. "); 
        text.setTextSize(18);
        //set up image view

        ImageView img = (ImageView) aboutDialog.findViewById(R.id.ImageView01);	
        img.setImageResource(R.drawable.icon);
        //set up button

        Button button = (Button) aboutDialog.findViewById(R.id.ButtonAbout);
        button.setText("OK");
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        	db.closeFirstUse();
        	aboutDialog.dismiss();	
            }

        });
  
        aboutDialog.show();
	}
	
	
	 //Menu Buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.contextmenu, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
        switch (item.getItemId()) {
            case R.id.help:     
            	Intent intent = new Intent(this,Help.class);
            	startActivity(intent);
                                break;
        }
        return true;
    }
	
    	
	
	

}
    
    
    
