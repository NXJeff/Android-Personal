package com.woi.merlin.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.IconTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.ThemeSingleton;
import com.woi.merlin.R;
import com.woi.merlin.component.ColorPickerDialog;
import com.woi.merlin.component.DatePickerFragment;
import com.woi.merlin.component.TimePickerFragment;
import com.woi.merlin.enumeration.CustomRepeatMode;
import com.woi.merlin.enumeration.ReminderType;
import com.woi.merlin.enumeration.RepeatType;
import com.woi.merlin.util.GeneralUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

/**
 * Created by Jeffery on 2/2/2015.
 */
public class AddNewReminder extends ActionBarActivity {

    TextView fromDatePicker, toDatePicker, atTimePicker, colorPicker;

    LocalDate fromDate, toDate;
    LocalTime atTime;
    Spinner repeatSpinner, customRepeatModeSpinner, reminderTypeSpinner;
    IconTextView colorIconView;

    RepeatType repeatType = RepeatType.DONOTREPEAT;
    ReminderType reminderType = ReminderType.Normal;
    CustomRepeatMode customRepeatMode = CustomRepeatMode.FOREVER;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_reminder_activity_layout);

        initActionBar();
        initViewComponents();
        initRepeatSpinner();
        initCustomRepeatMode();
        initReminderType();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_reminder_action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("itemId", getResources().getResourceName(item.getItemId()));
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.homeAsUp:
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_save:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
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
        customRepeatModeSpinner = (Spinner) findViewById(R.id.customRepeatMode);
        reminderTypeSpinner = (Spinner) findViewById(R.id.reminderTypeSpinner);
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

        applyDefaultColorToActivity();

    }

    public void showFromDateDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", fromDate.getYear());
        args.putInt("month", fromDate.getMonthOfYear() - 1);
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
        args.putInt("month", toDate.getMonthOfYear() - 1);
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
            fromDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
            fromDatePicker.setText(GeneralUtil.getDateInString(fromDate));
        }
    };

    DatePickerDialog.OnDateSetListener callbackOnToDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            toDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
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

    /**
     * repeatSpinner
     */
    private void initRepeatSpinner() {
//        List<String> list = new ArrayList<String>();
//        list.add(RepeatType.DONOTREPEAT.toString());
//        list.add(RepeatType.EVERYDAY.toString());
//        list.add(RepeatType.EVERYWEEK.toString());
//        list.add(RepeatType.EVERYMONTH.toString());
//        list.add(RepeatType.EVERYYEAR.toString());
//        list.add(RepeatType.CUSTOM.toString());

        ArrayAdapter<RepeatType> dataAdapter = new ArrayAdapter<RepeatType>(this,
                android.R.layout.simple_list_item_1, RepeatType.values());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(dataAdapter);

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                repeatType = (RepeatType) repeatSpinner.getSelectedItem();
                LinearLayout repeatCustomView = (LinearLayout) findViewById(R.id.repeatCustomView);
                LinearLayout dosePerDayOptionLayout = (LinearLayout) findViewById(R.id.dosePerDayOptionLayout);

                if (repeatType.equals(RepeatType.CUSTOM)) {
                    repeatCustomView.setVisibility(View.VISIBLE);
                } else {
                    repeatCustomView.setVisibility(View.GONE);
                }

                if (repeatType.equals(RepeatType.DONOTREPEAT)) {
                    dosePerDayOptionLayout.setVisibility(View.GONE);
                } else {
                    if (reminderType.equals(ReminderType.MedicalReminder))
                        dosePerDayOptionLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //Do nothing
            }
        });
    }

    private void populateRepeatSpinnerValue(RepeatType repeatType) {
        repeatSpinner.setSelection(((ArrayAdapter<RepeatType>) repeatSpinner.getAdapter()).getPosition(repeatType));
    }

    /**
     * customRepeatModeSpinner
     */
    private void initCustomRepeatMode() {
//        List<String> list = new ArrayList<String>();
//        list.add("Forever");
//        list.add("Until a date");
//        list.add("For a number of event");
        ArrayAdapter<CustomRepeatMode> dataAdapter = new ArrayAdapter<CustomRepeatMode>(this,
                android.R.layout.simple_spinner_item, CustomRepeatMode.values());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customRepeatModeSpinner.setAdapter(dataAdapter);

        customRepeatModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                customRepeatMode = (CustomRepeatMode) customRepeatModeSpinner.getSelectedItem();
                LinearLayout dosesInTotalLayout = (LinearLayout) findViewById(R.id.dosesInTotalLayout);
                LinearLayout toDatePickerLayout = (LinearLayout) findViewById(R.id.toDatePickerLayout);

                if (customRepeatMode.equals(CustomRepeatMode.FOREVER)) {
                    toDatePickerLayout.setVisibility(View.GONE);
                    dosesInTotalLayout.setVisibility(View.GONE);

                } else if (customRepeatMode.equals(CustomRepeatMode.UNTIL)) {
                    toDatePickerLayout.setVisibility(View.VISIBLE);
                    dosesInTotalLayout.setVisibility(View.GONE);
                } else if (customRepeatMode.equals(CustomRepeatMode.EVENTS)) {
                    toDatePickerLayout.setVisibility(View.GONE);
                    if (reminderType.equals(ReminderType.MedicalReminder))
                        dosesInTotalLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //Do nothing
            }
        });
    }

    private void initReminderType() {

        ArrayAdapter<ReminderType> dataAdapter = new ArrayAdapter<ReminderType>(this,
                android.R.layout.simple_spinner_item, ReminderType.values());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderTypeSpinner.setAdapter(dataAdapter);
        reminderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                reminderType = (ReminderType) reminderTypeSpinner.getSelectedItem();

                if (reminderType.equals(ReminderType.Normal)) {
                    applyDefaultColorToActivity(13);
                } else if (reminderType.equals(ReminderType.MedicalReminder)) {
                    applyDefaultColorToActivity(5);
                } else if (reminderType.equals(ReminderType.LoveCalendar)) {
                    applyDefaultColorToActivity(20);
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //Do nothing
            }
        });
    }

    private void populateReminderType(ReminderType reminderType) {
        reminderTypeSpinner.setSelection(((ArrayAdapter<ReminderType>) reminderTypeSpinner.getAdapter()).getPosition(reminderType));
    }


    static int selectedColorIndex = -1;

    private void showCustomColorChooser() {
        new ColorPickerDialog().show(this, selectedColorIndex, new ColorPickerDialog.Callback() {
            @Override
            public void onColorSelection(int index, int color, int darker, String colorName) {
                selectedColorIndex = index;
                applyColorToActivity(color, darker, colorName);
            }
        });
    }

    private void applyDefaultColorToActivity() {
        applyDefaultColorToActivity(13);
    }

    private void applyDefaultColorToActivity(int colorPosition) {
        TypedArray ca = getResources().obtainTypedArray(R.array.colors);
        TypedArray cna = getResources().obtainTypedArray(R.array.colorsName);
        int color = ca.getColor(colorPosition, 0);
        int darker = ColorPickerDialog.shiftColor(color);
        String colorName = cna.getString(colorPosition);
        applyColorToActivity(color, darker, colorName);
    }

    private void applyColorToActivity(int color, int darker, String colorName) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        colorIconView.setTextColor(color);
        colorPicker.setText(colorName);
        ThemeSingleton.get().positiveColor = color;
        ThemeSingleton.get().neutralColor = color;
        ThemeSingleton.get().negativeColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(darker);
    }

    @Override
    public void onBackPressed() {

        new MaterialDialog.Builder(this)
                .title("Cancel")
                .content("Are you sure you want to discard this event?")
                .positiveText("Keep Editing")
                .negativeText("Discard")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        finish();
                    }
                })
                .show();
    }
}
