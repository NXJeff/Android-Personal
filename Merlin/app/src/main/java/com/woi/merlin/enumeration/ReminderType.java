package com.woi.merlin.enumeration;

/**
 * Created by Jeffery on 2/7/2015.
 */
public enum ReminderType {

    Normal("N"),
    MedicalReminder("M"),
    LoveCalendar("L");

    private String value;

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


}
