package com.xyzwps.libs.yaff;

// TODO: 支持更多类型
public enum ParameterType {
    STRING, INT, FLOAT, BOOL;

    public static Object valid(Object value) {
        if (value == null) {
            return null;
        }

        return switch (value) {
            case String s -> s;
            case Integer i -> i.longValue();
            case Long l -> l;
            case Float f -> f.doubleValue();
            case Double d -> d;
            case Boolean b -> b;
            default -> throw new IllegalArgumentException("Invalid value type: " + value.getClass().getName());
        };
    }
}
