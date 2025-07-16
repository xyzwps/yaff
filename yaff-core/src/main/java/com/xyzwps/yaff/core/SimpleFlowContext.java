package com.xyzwps.yaff.core;

import com.xyzwps.yaff.core.commons.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleFlowContext implements FlowContext {

    private final HashMap<String, Object> map = new HashMap<>();

    private static String validName(String name) {
        if (name == null) {
            throw new YaffException("Name cannot be null");
        }
        if (!Utils.isIdentifier(name)) {
            throw new YaffException("Invalid context variable name: " + name);
        }
        return name;
    }

    @Override
    public void set(String name, Object value) {
        map.put(validName(name), value);
    }

    @Override
    public Object get(String name) {
        return map.get(validName(name));
    }

    @Override
    public Set<String> getNames() {
        return map.keySet();
    }

    @Override
    public FlowContext clone() {
        var newContext = new SimpleFlowContext();
        newContext.map.putAll(this.map);
        return newContext;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.copyOf(map);
    }
}
