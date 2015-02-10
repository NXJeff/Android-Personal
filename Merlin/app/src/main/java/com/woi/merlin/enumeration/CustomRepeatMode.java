package com.woi.merlin.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeffery on 2/8/2015.
 */
public enum CustomRepeatMode {

    FOREVER(0),
    UNTIL(1),
    EVENTS(2);

    private int value;

    private CustomRepeatMode(int value) {
        this.value = value;
    }

    private final static Map<Integer, CustomRepeatMode> map =
            new HashMap<Integer, CustomRepeatMode>(CustomRepeatMode.values().length, 1.0f);

    static {
        for (CustomRepeatMode c : CustomRepeatMode.values()) {
            map.put(c.value, c);
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case FOREVER:
                return "Forever";
            case UNTIL:
                return "Until a date";
            case EVENTS:
                return "For a number of events";
        }

        return null;
    }

    public static CustomRepeatMode of(int name) {
        CustomRepeatMode result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("No Such RepeatType Exists");
        }
        return result;
    }
}
