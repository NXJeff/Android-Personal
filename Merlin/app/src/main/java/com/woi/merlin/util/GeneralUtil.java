package com.woi.merlin.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;

/**
 * Created by Jeffery on 2/4/2015.
 */
public class GeneralUtil {

    //Default format
    public static String getDateInString(LocalDate date) {
        return getDateInString(date, "EEEE, MMM d, yyyy");
    }

    //Wed, April 1, 2015
    public static String getDateInStringFormatA(LocalDate date) {
        return getDateInString(date, "E, MMM d, yyyy");
    }

    public static String getDateInString(LocalDate date, String format) {
        SimpleDateFormat dt = new SimpleDateFormat(format);
        return dt.format(date.toDate());
    }

    public static String getTimeInString(LocalTime time) {
        SimpleDateFormat dt = new SimpleDateFormat("h:mm a");
        return dt.format(time.toDateTimeToday().toDate());
    }
}
