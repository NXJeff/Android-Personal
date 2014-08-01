package com.nxjeff.remoteprojectclient;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Help extends Activity {
	
    // Declare webview
    WebView browser;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        browser=(WebView)findViewById(R.id.webkit);
        
        // Loads html-string into webview
        browser.loadUrl("file:///android_asset/help.html");
    }
}