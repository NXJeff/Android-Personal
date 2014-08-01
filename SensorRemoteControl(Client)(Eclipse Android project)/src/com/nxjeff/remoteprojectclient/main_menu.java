package com.nxjeff.remoteprojectclient;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class main_menu extends Activity {
	
	private ListView menuList;
	private ArrayList<String> currentArray = new ArrayList<String>();
	private TextView subTitle;
	private String ip_add,c_name;
	private String port_no;

	

	Intent mouseO,mouseA,presentation,num,multimedia,keyboard,test,help;
	
	ArrayAdapter<String> adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        ip_add="";
        c_name="";
        port_no="";
        try{	
        	Bundle extras = getIntent().getExtras();

        	ip_add = extras.getString("ip_add");
        	port_no = extras.getString("port_no");
        	c_name = extras.getString("c_name");
  
        }catch(Exception e){
        	Log.i(getClass().getSimpleName(), "Bundle exception :"+e);
        }
        
        

     
        subTitle = (TextView)findViewById(R.id.mainmenu_subTitle);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , currentArray);
        mainMenu(); //Initial to main menu
        
        mouseO = new Intent(this,mouse_c.class);
        mouseA = new Intent(this,mouse_a.class);
        presentation = new Intent(this,presentation.class);
        multimedia = new Intent(this,player.class);
        num = new Intent(this,numpad.class);
        keyboard = new Intent(this,Keyboard.class);
        help = new Intent(this,Help.class);
        
        menuList=(ListView)findViewById(R.id.mainmenu_list);
        menuList.setAdapter(adapter);

        
        menuList.setOnItemClickListener(new OnItemClickListener() {
	        //@Override
	        public void onItemClick(AdapterView arg0, View view, int position, long id) {	//listview click function

	        	String option = menuList.getItemAtPosition(position).toString();

	        	
	        	if(option=="Remote Functions"){
	        		remoteMenuInit();
	            }else
	            	if(option == "Back") {
	            		mainMenu();
	            		Log.d("", "BACK PRESSED");
	            	}	
	            
	            	if(option == "Settings") {
	            		startActivity(test);
	            }else
	            	if(option=="About") {
	            		about();
	            		
	            }else
	            	if(option== "Help"){
	            		startActivity(help);
	            	}else
	            	if(option == "Exit") {
	            			onBackPressed();
	            	}
	            	
	            	if(option =="Orientation Mouse"){
	            		startActivity(mouseO);
	            	}
	            	
	            	if(option == "Accelerometer Mouse"){
	            		startActivity(mouseA);
	            		
	            	}
	            	
	            	if(option == "Slide Show"){
	            		startActivity(presentation);
	            	}
	            	if(option == "Multimedia Control"){
	            		startActivity(multimedia);
	            	}
	            	
	            	if(option =="NumPad"){
	            		startActivity(num);
	            	}
	            	
	            	if(option=="Keyboard"){
	            		startActivity(keyboard);
	            	}
	            	
	            	            
	        }});
	}
	
	//About me dialog
	public void about(){
		final Dialog aboutDialog = new Dialog(main_menu.this);
		aboutDialog.setContentView(R.layout.about);
		aboutDialog.setTitle("Sensor Remote Control");
		aboutDialog.setCancelable(true);
		
        TextView text = (TextView) aboutDialog.findViewById(R.id.TextView01);
        text.setText("Sensor Remote Control is an application that turning a smartphone into a virtual controller, to control the PC's mouse and keyboard. " +
        		"User need to execute the Sensor Remote Control (Server) application on the PC and have a same wireless connection connected between both server and client." +
        		"\n\n **** How to use Sensor Remote Control  ****\n\n" +
        		"\n\nUser will need to find out the IP & port number of the server by using the SCAN button or enter them into the text fields manually, then proceed by pressing Connect button." +
        		"\n\nUser will need to enter the required PassKey if the server have \"Use PassKey\" selected."); 
        text.setTextSize(14);
        //set up image view

        ImageView img = (ImageView) aboutDialog.findViewById(R.id.ImageView01);	
        img.setImageResource(R.drawable.icon);



        //set up button

        Button button = (Button) aboutDialog.findViewById(R.id.ButtonAbout);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        	aboutDialog.dismiss();
        		
            }

        });
  
        aboutDialog.show();
	}
	
	public void mainMenu(){
		subTitle.setText("Connected to "+ip_add+":"+port_no+" "+c_name);
		currentArray.clear();
		currentArray.add("Remote Functions");
        currentArray.add("About");
        currentArray.add("Help");
        currentArray.add("Exit");
        try{
            adapter.notifyDataSetChanged();
            }catch(Exception e){}
	}
	
	public void remoteMenuInit(){
		subTitle.setText("Remote Functions");
		currentArray.clear();
		currentArray.add("Accelerometer Mouse");
		currentArray.add("Orientation Mouse");
        currentArray.add("Keyboard");
        currentArray.add("NumPad");
        currentArray.add("Slide Show");
        currentArray.add("Multimedia Control");
        currentArray.add("Back");
        try{
            adapter.notifyDataSetChanged();
            }catch(Exception e){}
	}
	
	
	@Override
    public void onBackPressed() {
		if(currentArray.contains("Back")){
			mainMenu();
		}else{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to disconnect?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   db.closeSession();
                        main_menu.this.finish();
                       
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        AlertDialog alert = builder.create();
        alert.show();

    }
	}
}
