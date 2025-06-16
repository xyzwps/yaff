package com.xyzwps.libs.yaff.flow;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class SimpleFlowContext implements FlowContext {
    private final HashMap<String, Object> map = new HashMap<>();

    @Override
    public void set(String name, Object value) {
        Objects.requireNonNull(name, "Context variable name cannot be null");
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid context variable name: " + name);
        }
        map.put(name, value);
    }

    @Override
    public Object get(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Context variable path cannot be null");
        }
        if (!NAME_PATTERN.matcher(path).matches()) {
            throw new IllegalArgumentException("Invalid context variable path: " + path);
        }
        return map.get(path);
    }

    @Override
    public Set<String> getNames() {
        return map.keySet();
    }
}
