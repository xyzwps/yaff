package com.xyzwps.libs.yaff;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public class SimpleFlowContext implements FlowContext {
    private final HashMap<String, Object> map = new HashMap<>();

    static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    @Override
    public void set(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("Context variable name cannot be null");
        }

        var validValue = ParameterType.valid(value);
        if (NAME_PATTERN.matcher(name).matches()) {
            map.put(name, validValue);
        } else {
            throw new IllegalArgumentException("Invalid context variable name: " + name);
        }
    }

    @Override
    public Object get(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Context variable path cannot be null");
        }
        return map.get(path);
    }

    @Override
    public Set<String> getNames() {
        return map.keySet();
    }
}
