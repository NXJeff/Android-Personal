package com.woi.merlin.util;

import com.woi.merlin.enumeration.RepeatType;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import merlin.model.raw.Reminder;

/**
 * Created by YeekFeiTan on 3/16/2015.
 */
public class ReminderUtil {

    public static DateTime getNextReminderTime(Reminder reminder) {

        DateTime fromDate = new DateTime(reminder.getFromDate());
        DateTime nextDate = new DateTime(reminder.getFromDate());
        DateTime currentDateTime = new DateTime();
        DateTime atTime = new DateTime(reminder.getAtTime());
        RepeatType repeatType = RepeatType.of(reminder.getRepeatType());

        //set time
        nextDate = nextDate.withTime(atTime.getHourOfDay(), atTime.getMinuteOfHour(), atTime.getSecondOfMinute(), 0);

        //If not after then get the next date
        if (nextDate.isBefore(currentDateTime)) {

            if (!repeatType.equals(RepeatType.DONOTREPEAT)) {
                if (currentDateTime.compareTo(fromDate) != 0) {

                    do {
                        switch (repeatType) {
                            case EVERYDAY:
                                nextDate = nextDate.plusDays(1);
                                break;
                            case EVERYWEEK:
                                nextDate = nextDate.plusWeeks(1);
                                break;
                            case EVERYMONTH:
                                nextDate = nextDate.plusMonths(1);
                                break;
                            case EVERYYEAR:
                                nextDate = nextDate.plusYears(1);
                                break;
                            case CUSTOM:
                                break;
                        }

                    } while (!nextDate.isAfter(currentDateTime));
                }
            }
        }

        if (nextDate.isBefore(currentDateTime)) {
            nextDate = null;
        } else {
            nextDate = nextDate.withTime(atTime.getHourOfDay(), atTime.getMinuteOfHour(), atTime.getSecondOfMinute(), 0);
        }

        return nextDate;
    }

    public static String getReadableRemainingDate(Reminder reminder) {
        return getReadableRemainingDate(getNextReminderTime(reminder));
    }

    public static String getReadableRemainingDate(DateTime future) {

        DateTime now = new DateTime();
        Period period = new Period(now, future);

        //Suffixes
        String yearSuffix = " years ";
        String monthSuffix = " months ";
        String weekSuffix = " weeks ";
        String daySuffix = " days ";
        String hourSuffix = " hr ";
        String minuteSuffix = " min ";
//        String secondSuffix = " second ";

        int year = period.getYears();
        int month = period.getMonths();
        int week = period.getWeeks();
        int day = period.getDays();
        int hour = period.getHours();
        int minute = period.getMinutes();
        int second = period.getSeconds();

        if (year == 1)
            yearSuffix = " year ";

        if (month == 1)
            monthSuffix = " month ";

        if (week == 1)
            weekSuffix = " week ";

        if (day == 1)
            daySuffix = " day ";

//        if (hour == 1)
//            hourSuffix = " hour ";
//
//        if (minute == 1)
//            minuteSuffix = " minute ";

//        if (second == 1)
//            secondSuffix = " second ";

        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();
        if (year > 0 || month > 0) {
            periodFormatterBuilder.appendYears().appendSuffix(yearSuffix);
            if (month > 0) {
                periodFormatterBuilder.appendMonths().appendSuffix(monthSuffix);
            } else {
                periodFormatterBuilder.appendWeeks().appendSuffix(weekSuffix);
            }
            periodFormatterBuilder.appendDays().appendSuffix(daySuffix);
        }


        periodFormatterBuilder.appendHours().appendSuffix(hourSuffix);
        if (year == 0 && month == 0) {
            periodFormatterBuilder.appendMinutes().appendSuffix(minuteSuffix);
//            periodFormatterBuilder.appendSeconds().appendSuffix(secondSuffix);
        }

        periodFormatterBuilder.printZeroNever();

        PeriodFormatter formatter = periodFormatterBuilder.toFormatter();

        String remaining = formatter.print(period);

        return remaining;
    }

}
