package com.nxjeff.remoteprojectclient;

import java.util.LinkedList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyCharacterMap;

public class db {
	public static String ip;
	public static String com_name;
	public static String port;
	public static String session;
	public static String firstuse;
	public static LinkedList<String> savedHosts,comName;
	public static LinkedList<String> portList;
	public static final int MAX_SAVED_HOSTS=10;
	private static SharedPreferences prefs;
	private static final String PREFS_FILENAME = "SensorRemote";
	private static final String PREFS_RECENT_IP_PREFIX = "recenthost";
	private static final String PREFS_RECENT_PORT_PREFIX = "recentport";
	private static final String PREFS_RECENT_NAME_PREFIX = "recenthostname";
	private static final String PREFS_IPKEY = "remoteip";
	private static final String PREFS_PORTKEY = "remoteport";
	private static final String PREFS_COMNAMEKEY = "remotehostname";
	private static final String PREFS_SESSION = "NO";
	private static final String PREFS_FIRSTUSE = "YES";
	
	public static KeyCharacterMap charmap;
	
	public static void init(Context con) {
		if(prefs==null) {
			prefs=con.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
			savedHosts=new LinkedList<String>();
			comName = new LinkedList<String>();
			portList = new LinkedList<String>();
			populateRecentIPs();
			//get all preferences
			ip=prefs.getString(PREFS_IPKEY, "127.0.0.1");
			com_name=prefs.getString(PREFS_COMNAMEKEY,"");
			port=prefs.getString(PREFS_PORTKEY,"4444");
			session=prefs.getString(PREFS_SESSION, "NO");
			firstuse=prefs.getString(PREFS_FIRSTUSE, "YES");
			charmap = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
			
			
			
			
		}
	}
	
	//open a session
	public static void openSession(){
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(PREFS_SESSION, "YES");
		edit.commit();
		session = "YES";
		
	}
	
	//close a session
	public static void closeSession(){
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(PREFS_SESSION, "NO");
		edit.commit();
		session = "NO";
	}
	
	//Eliminate first use dialog
	public static void closeFirstUse(){
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(PREFS_FIRSTUSE, "NO");
		edit.commit();
		firstuse = "NO";
	}
	
	
	//add an ip in to the list
	public static void setIp(String ip, String iport, String com) throws Exception {
		SharedPreferences.Editor edit = prefs.edit();
		IPValid(ip);
		edit.putString(db.PREFS_IPKEY, ip);
		edit.putString(db.PREFS_PORTKEY, iport);
		edit.putString(db.PREFS_COMNAMEKEY, com);
		edit.commit();
		db.ip = ip;
		db.com_name=com;
		db.port=iport;
		
		if(!savedHosts.contains(ip)) {
			if(savedHosts.size()<MAX_SAVED_HOSTS) {
				savedHosts.addFirst(ip);
				comName.addFirst(com);
				portList.addFirst(iport);
			}else {
				savedHosts.removeLast();
				comName.removeLast();
				portList.removeLast();
				savedHosts.addFirst(ip);
				comName.addFirst(com);
				portList.addFirst(iport);
				
			}
		}else{
			while(savedHosts.contains(ip)){
				int no = getLocation(ip);
				savedHosts.remove(no);
				comName.remove(no);
				portList.remove(no);
				}
			savedHosts.addFirst(ip);
			comName.addFirst(com);
			portList.addFirst(iport);
		}
		writeRecentIPs();
	}
	
	public static int getLocation(String ip){
		int loc=0;
		for(int i=0;i<savedHosts.size();i++)
		{
			if(savedHosts.get(i).contains(ip)==true){
				loc = i;
			}
		}
		return loc;
	}
	
	// deletes a saved host from the list of saved hosts, by string
	public static void removeSavedHost(CharSequence ip,CharSequence com) throws Exception {
		// remove ip from list
        //if (savedHosts.remove(ip.toString())) {
                // rewrite settings
		if(savedHosts.contains(ip.toString()) && comName.contains(com.toString())){
        	    for(int i=0; i<savedHosts.size();i++)
        	    {
        	    	if(comName.get(i).contains(com)&& savedHosts.get(i).contains(ip))
        	    	{
        	    		savedHosts.remove(i);
        	    		comName.remove(i);
        	    		portList.remove(i);
        	    	
        	    	}
        	    }
                writeRecentIPs();

        } else {
                throw new Exception("did not find " + ip.toString() + " in saved host list");
        }
	}
	
	private static void writeRecentIPs() {
        SharedPreferences.Editor edit = prefs.edit();
        String s;
        String p;
        for (int i = 0; i < MAX_SAVED_HOSTS; ++i) {
                try {
                        s = savedHosts.get(i);
                } catch (IndexOutOfBoundsException e) {
                        s = null;
                }
                edit.putString(PREFS_RECENT_IP_PREFIX + ((Integer) i).toString(), s);
        }
        for (int i = 0; i < MAX_SAVED_HOSTS; ++i) {
            try {
                    s = comName.get(i);
            } catch (IndexOutOfBoundsException e) {
                    s = null;
            }
            edit.putString(PREFS_RECENT_NAME_PREFIX + ((Integer) i).toString(), s);
    }
        for (int i = 0; i < MAX_SAVED_HOSTS; ++i) {
            try {
                    p = portList.get(i);
            } catch (IndexOutOfBoundsException e) {
                    p = "0";
            }
            edit.putString(PREFS_RECENT_PORT_PREFIX + ((Integer) i).toString(), p);
    }
        
        edit.putString(PREFS_SESSION,session);
        edit.putString(PREFS_FIRSTUSE,firstuse);
        edit.commit();
        
}
	
	
	private static void populateRecentIPs() {
        savedHosts.clear();
        comName.clear();
        portList.clear();
        for (int i = 0; i < MAX_SAVED_HOSTS; ++i) {
                String host = prefs.getString(PREFS_RECENT_IP_PREFIX + ((Integer) i).toString(), null);
                if (host != null) {
                        savedHosts.add(host);
                }
        }
        for (int i = 0; i < MAX_SAVED_HOSTS; ++i) {
            String host = prefs.getString(PREFS_RECENT_NAME_PREFIX + ((Integer) i).toString(), null);
            if (host != null) {
                    comName.add(host);
            }
    }
        for (int i = 0; i < MAX_SAVED_HOSTS; ++i) {
            String host = prefs.getString(PREFS_RECENT_PORT_PREFIX + ((Integer) i).toString(), null);
            if (host != null) {
                    portList.add(host);
            }
    }
        session = prefs.getString(PREFS_SESSION,"NO");
        firstuse = prefs.getString(PREFS_FIRSTUSE,"YES");
	}
	
	private static void IPValid(String ip) throws Exception {
        try {
                String[] octets = ip.split("\\.");
                for (String s : octets) {
                        int i = Integer.parseInt(s);
                        if (i > 255 || i < 0) {
                                throw new NumberFormatException();
                        }
                }
        } catch (NumberFormatException e) {
                throw new Exception("Illegal IP address!");
        }

}
	public static String getIPfromlist(int position) {
		String host = prefs.getString(PREFS_RECENT_IP_PREFIX + ((Integer) position).toString(), null);
		return host;
	}
	
	
	

}
