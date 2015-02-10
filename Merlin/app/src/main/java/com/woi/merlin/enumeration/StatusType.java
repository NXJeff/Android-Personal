package com.woi.merlin.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YeekFeiTan on 2/10/2015.
 */
public enum StatusType {

    Active("A"),
    Deleted("D");

    private String value;

    private final static Map<String, StatusType> map =
            new HashMap<String, StatusType>(StatusType.values().length, 1.0f);

    static {
        for (StatusType c : StatusType.values()) {
            map.put(c.value, c);
        }
    }

    private StatusType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (this) {
            case Active:
                return "A";
            case Deleted:
                return "D";
        }

        return null;
    }

    public static StatusType of(String name) {
        StatusType result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("No Such StatusType Exists");
        }
        return result;
    }
}
