package com.woi.merlin.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.IconTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.ThemeSingleton;
import com.woi.merlin.R;
import com.woi.merlin.component.ColorPickerDialog;
import com.woi.merlin.component.DatePickerFragment;
import com.woi.merlin.component.TimePickerFragment;
import com.woi.merlin.util.GeneralUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jeffery on 2/2/2015.
 */
public class AddNewReminder extends ActionBarActivity {

    TextView fromDatePicker, toDatePicker, atTimePicker, colorPicker;

    LocalDate fromDate, toDate;
    LocalTime atTime;
    Spinner repeatSpinner;
    IconTextView colorIconView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_reminder_activity_layout);

        initActionBar();
        initViewComponents();
        initRepeatSpinner();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_reminder_action_bar_menu, menu);
        return true;
    }

    private void initActionBar() {
        // enable ActionBar app icon to behave as action to toggle nav drawer
        // use action bar here
//        getSupportActionBar().setIcon(new IconDrawable(this, Iconify.IconValue.fa_bell).colorRes(R.color.grey_white_1000).actionBarSize());
        getSupportActionBar().setTitle("New Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initViewComponents() {
        fromDatePicker = (TextView) findViewById(R.id.fromDatePickerET);
        toDatePicker = (TextView) findViewById(R.id.toDatePickerET);
        atTimePicker = (TextView) findViewById(R.id.atTimePickerET);
        repeatSpinner = (Spinner) findViewById(R.id.repeatSpinner);
        colorPicker = (TextView) findViewById(R.id.colorPickerET);
        colorIconView = (IconTextView) findViewById(R.id.colorIconView);
        fromDate = new LocalDate();
        toDate = new LocalDate();
        atTime = new LocalTime();

        fromDatePicker.setText(GeneralUtil.getDateInString(fromDate));
        toDatePicker.setText(GeneralUtil.getDateInString(toDate));
        atTimePicker.setText(GeneralUtil.getTimeInString(atTime));

        fromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFromDateDatePicker();
            }
        });

        toDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToDateDatePicker();
            }
        });

        atTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAtTimePicker();
            }
        });

        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomColorChooser();
            }
        });
    }

    public void showFromDateDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", fromDate.getYear());
        args.putInt("month", fromDate.getMonthOfYear());
        args.putInt("day", fromDate.getDayOfMonth());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(callbackOnFromDate);
        date.show(this.getFragmentManager(), "From Date");
    }

    public void showToDateDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", toDate.getYear());
        args.putInt("month", toDate.getMonthOfYear());
        args.putInt("day", toDate.getDayOfMonth());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(callbackOnToDate);
        date.show(this.getFragmentManager(), "To Date");
    }

    public void showAtTimePicker() {
        TimePickerFragment date = new TimePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", atTime.getHourOfDay());
        args.putInt("minutes", atTime.getMinuteOfHour());
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(callbackOnAtTime);
        date.show(this.getFragmentManager(), "At Time");
    }

    //Callbacks
    DatePickerDialog.OnDateSetListener callbackOnFromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            fromDate = new LocalDate(year, monthOfYear, dayOfMonth);
            fromDatePicker.setText(GeneralUtil.getDateInString(fromDate));
        }
    };

    DatePickerDialog.OnDateSetListener callbackOnToDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            toDate = new LocalDate(year, monthOfYear, dayOfMonth);
            toDatePicker.setText(GeneralUtil.getDateInString(toDate));
        }
    };

    TimePickerDialog.OnTimeSetListener callbackOnAtTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            atTime = new LocalTime(hourOfDay, minute);
            atTimePicker.setText(GeneralUtil.getTimeInString(atTime));
        }
    };

    private void initRepeatSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("Do not repeat");
        list.add("Every Day");
        list.add("Every Week");
        list.add("Every Month");
        list.add("Every Year");
        list.add("Custom...");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(dataAdapter);
    }

    static int selectedColorIndex = -1;

    private void showCustomColorChooser() {
        new ColorPickerDialog().show(this, selectedColorIndex, new ColorPickerDialog.Callback() {
            @Override
            public void onColorSelection(int index, int color, int darker, String colorName) {
                selectedColorIndex = index;
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                colorIconView.setTextColor(color);
                colorPicker.setText(colorName);
                ThemeSingleton.get().positiveColor = color;
                ThemeSingleton.get().neutralColor = color;
                ThemeSingleton.get().negativeColor = color;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    getWindow().setStatusBarColor(darker);
            }
        });
    }
}
