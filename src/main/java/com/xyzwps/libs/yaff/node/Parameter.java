package com.xyzwps.libs.yaff.node;

public record Parameter(String name, Type type) {
    public enum Type {
        STRING, INT, FLOAT, BOOL
    }
}
