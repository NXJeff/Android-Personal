package com.woi.merlin.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.woi.merlin.R;

/**
 * Created by YeekFeiTan on 2/27/2015.
 */
public class AddNewMeal extends ActionBarActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_meal_activity_layout);

//        initActionBar();
//        initViewComponents();
//        initRepeatSpinner();
//        initCustomRepeatMode();
//        initReminderType();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_reminder_action_bar_menu, menu);
        return true;
    }
}
