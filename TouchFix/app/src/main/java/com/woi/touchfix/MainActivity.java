package com.woi.touchfix;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eu.chainfire.libsuperuser.Shell;


public class MainActivity extends ActionBarActivity {

    LinearLayout view1, view2, view3;
    TextView tv1;
    int statusType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1 = (LinearLayout)findViewById(R.id.view01);
        view2 = (LinearLayout)findViewById(R.id.view02);
        view3 = (LinearLayout)findViewById(R.id.view03);
        tv1 = (TextView) findViewById(R.id.status);
        (new StartUp()).setContext(this).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateView(int view) {

        switch(view) {
            case 2:
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                try {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("/proc/touchpanel/glove_mode_enable"),"UTF-8"));
                    br.mark(1);
                    if (br.read() != 0xFEFF)
                        br.reset();
                    String nextLine;

                    while ((nextLine = br.readLine()) != null) {
                        if(nextLine.trim() == "1") {
                            tv1.setText("Status: Activated");
                        }
                    }
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                break;
            case 3:
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);
                break;
            default:
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
        }
    }


    private class StartUp extends AsyncTask<String, Void, String> {


        private Context context = null;
        boolean suAvailable = false;

        //Created by themakeinfo.com,Promote us !!!
        public StartUp setContext(Context context) {
            this.context = context;
            return this;
        }

        @Override
        protected String doInBackground(String... params) {
            suAvailable = Shell.SU.available();
            if (suAvailable) {
                Shell.SU.run("busybox mount -o remount,rw /system");
                Shell.SU.run("echo 1 > /proc/touchpanel/glove_mode_enable");
                Shell.SU.run("echo $'#!/system/bin/sh\\n#Touch Fix\\n\\necho 1 > /proc/touchpanel/glove_mode_enable' > /system/etc/init.d/T99TouchFix");
                Shell.SU.run("busybox chmod 777 /system/etc/init.d/T99TouchFix");
                Shell.SU.run("busybox mount -o remount,ro /system");
                statusType = 2;
            } else {
                statusType = 3;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            updateView(statusType);
        }
    }
}