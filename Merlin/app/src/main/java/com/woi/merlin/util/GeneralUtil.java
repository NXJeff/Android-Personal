package com.woi.merlin.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;

/**
 * Created by Jeffery on 2/4/2015.
 */
public class GeneralUtil {

    public static String getDateInString(LocalDate date) {
        SimpleDateFormat dt = new SimpleDateFormat("EEEE, MMM d, yyyy");
        return dt.format(date.toDate());
    }

    public static String getTimeInString(LocalTime time) {
        SimpleDateFormat dt = new SimpleDateFormat("h:mm a");
        return dt.format(time.toDateTimeToday().toDate());
    }
}
