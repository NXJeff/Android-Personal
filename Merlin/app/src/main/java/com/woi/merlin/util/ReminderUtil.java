package com.woi.merlin.util;

import com.woi.merlin.enumeration.ReminderType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import merlin.model.raw.Reminder;

/**
 * Created by YeekFeiTan on 3/16/2015.
 */
public class ReminderUtil {

    public static Long getNextReminderTime(Reminder reminder) {

        LocalDate fromDate = new LocalDate(reminder.getFromDate());
        LocalDate toDate = new LocalDate(reminder.getToDate());
        LocalTime atTime = new LocalTime(reminder.getAtTime());

        if(reminder.getReminderType().equals(ReminderType.Normal)) {



        }

    }
}
