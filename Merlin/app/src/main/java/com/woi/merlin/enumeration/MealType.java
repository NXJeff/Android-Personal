package com.woi.merlin.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YeekFeiTan on 3/6/2015.
 */
public enum MealType {
    BREAKFAST("B"),
    LUNCH("L"),
    DINNER("D"),
    SUPPER("S");

    private String value;
    private final static Map<String, MealType> map =
            new HashMap<String, MealType>(RepeatType.values().length, 1.0f);

    static {
        for (MealType c : MealType.values()) {
            map.put(c.getValue(), c);
        }
    }

    private MealType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        switch (this) {
            case BREAKFAST:
                return "Breakfast";
            case LUNCH:
                return "Lunch";
            case DINNER:
                return "Dinner";
            case SUPPER:
                return "Supper";
        }

        return null;
    }

    public static MealType of(String name) {
        MealType result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("No Such MealType Exists");
        }
        return result;
    }

    public String getValue() {
        return value;
    }
}

