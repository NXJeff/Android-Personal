package com.woi.merlin.enumeration;

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
}
