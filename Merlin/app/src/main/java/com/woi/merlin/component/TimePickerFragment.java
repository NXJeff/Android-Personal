package com.woi.merlin.component;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Jeffery on 2/3/2015.
 */
public class TimePickerFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener ontime;

    public TimePickerFragment() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
    }
    public void setCallBack(TimePickerDialog.OnTimeSetListener ontime) {
        this.ontime = ontime;
    }

    private int hour, minute;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hour = args.getInt("hour");
        minute = args.getInt("minute");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), ontime, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
