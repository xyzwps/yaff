package com.xyzwps.libs.yaff.commons;

public final class Arguments {

    public static <T> T notNull(T obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument " + name + " cannot be null.");
        }
        return obj;
    }
}
