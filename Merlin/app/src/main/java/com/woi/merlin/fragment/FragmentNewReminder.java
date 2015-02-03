package com.woi.merlin.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.woi.merlin.R;
import com.woi.merlin.component.DatePickerFragment;
import com.woi.merlin.component.TimePickerFragment;
import com.woi.merlin.util.GeneralUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jeffery on 2/2/2015.
 */
public class FragmentNewReminder extends Fragment {

    TextView fromDatePicker, toDatePicker, atTimePicker;

    LocalDate fromDate, toDate;
    LocalTime atTime;
    Spinner repeatSpinner;


    public FragmentNewReminder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_reminder_activity_layout, container,
                false);

        fromDatePicker = (TextView) view.findViewById(R.id.fromDatePickerET);
        toDatePicker = (TextView) view.findViewById(R.id.toDatePickerET);
        atTimePicker = (TextView) view.findViewById(R.id.atTimePickerET);
        repeatSpinner = (Spinner) view.findViewById(R.id.repeatSpinner);

        initRepeatSpinner();

        //set default time
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

        return view;
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(dataAdapter);
    }


}
