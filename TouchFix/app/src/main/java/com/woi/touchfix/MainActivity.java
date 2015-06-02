package com.woi.touchfix;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import eu.chainfire.libsuperuser.Shell;


public class MainActivity extends ActionBarActivity {

    LinearLayout view1, view2, view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1 = (LinearLayout)findViewById(R.id.view01);
        view2 = (LinearLayout)findViewById(R.id.view02);
        view3 = (LinearLayout)findViewById(R.id.view03);
        (new StartUp()).setContext(this).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private class StartUp extends AsyncTask<String, Void, Void> {


        private Context context = null;
        boolean suAvailable = false;

        //Created by themakeinfo.com,Promote us !!!
        public StartUp setContext(Context context) {
            this.context = context;
            return this;
        }

        @Override
        protected Void doInBackground(String... params) {
            suAvailable = Shell.SU.available();
            if (suAvailable) {
                Shell.SU.run("busybox mount -o remount,rw /system");
                Shell.SU.run("echo 1 > /proc/touchpanel/glove_mode_enable");
                Shell.SU.run("echo $'#!/system/bin/sh\\n#Touch Fix\\n\\necho 1 > /proc/touchpanel/glove_mode_enable' > /system/etc/init.d/T99TouchFix");
                Shell.SU.run("busybox chmod 777 /system/etc/init.d/T99TouchFix");
                Shell.SU.run("busybox mount -o remount,ro /system");
                updateView(2);
            } else {
                Toast.makeText(getApplicationContext(), "Phone not Rooted", Toast.LENGTH_SHORT).show();
                updateView(3);
            }

            return null;
        }
    }
}