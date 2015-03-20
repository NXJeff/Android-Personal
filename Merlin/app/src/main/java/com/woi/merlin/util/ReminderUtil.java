package com.woi.merlin.util;

import com.woi.merlin.enumeration.ReminderType;
import com.woi.merlin.enumeration.RepeatType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import merlin.model.raw.Reminder;

/**
 * Created by YeekFeiTan on 3/16/2015.
 */
public class ReminderUtil {

    public static LocalDate getNextReminderTime(Reminder reminder) {

        LocalDate nextDate = new LocalDate(reminder.getFromDate());
        LocalDate currentDateTime = new LocalDate();

        do {
            switch (RepeatType.of(reminder.getRepeatType())) {
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

            }
        } while (nextDate.isAfter(currentDateTime));

        return nextDate;

    }

    public static LocalDate getNextOccuredDateBasedOnRepeatType(Reminder reminder, RepeatType repeatType) {

        LocalDate nextDate = new LocalDate(reminder.getFromDate());
        LocalDate currentDateTime = new LocalDate();

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
            }
        } while (nextDate.isAfter(currentDateTime));

        return nextDate;
    }
}
