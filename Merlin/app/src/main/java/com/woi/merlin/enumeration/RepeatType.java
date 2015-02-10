package com.woi.merlin.enumeration;

import java.util.HashMap;
import java.util.Map;

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
    private final static Map<Integer, RepeatType> map =
            new HashMap<Integer, RepeatType>(RepeatType.values().length, 1.0f);

    static {
        for (RepeatType c : RepeatType.values()) {
            map.put(c.value, c);
        }
    }

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

    public static RepeatType of(int name) {
        RepeatType result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("No Such RepeatType Exists");
        }
        return result;
    }


}
