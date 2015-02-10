package com.woi.merlin.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeffery on 2/7/2015.
 */
public enum ReminderType {

    Normal("N"),
    MedicalReminder("M"),
    LoveCalendar("L");

    private String value;

    private final static Map<String, ReminderType> map =
            new HashMap<String, ReminderType>(RepeatType.values().length, 1.0f);

    static {
        for (ReminderType c : ReminderType.values()) {
            map.put(c.value, c);
        }
    }

    private ReminderType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (this) {
            case Normal:
                return "Normal";
            case MedicalReminder:
                return "Medical Reminder";
            case LoveCalendar:
                return "Love Calendar";
        }

        return null;
    }

    public static ReminderType of(String name) {
        ReminderType result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("No Such RepeatType Exists");
        }
        return result;
    }

}
