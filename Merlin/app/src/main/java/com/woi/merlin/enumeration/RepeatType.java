package com.woi.merlin.enumeration;

/**
 * Created by Jeffery on 2/7/2015.
 */
public enum RepeatType {
    DONOTREPEAT(0),
    EVERYDAY(1),
    EVERYWEEK(2),
    EVERYMONTH(3),
    EVERYYEAR(4),
    CUSTOM(5);

    private int value;

    private RepeatType(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (this) {
            case DONOTREPEAT:
                return "Do Not Repeat";
            case EVERYDAY:
                return "Everyday";
            case EVERYWEEK:
                return "Every Week";
            case EVERYMONTH:
                return "Every Month";
            case EVERYYEAR:
                return "Every Year";
            case CUSTOM:
                return "Custom";
        }

        return null;
    }
}
