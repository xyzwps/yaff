package com.xyzwps.libs.yaff;

import com.xyzwps.libs.yaff.commons.Utils;

import java.util.HashMap;
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
        map.put(validName(name), ParameterType.valid(value));
    }

    @Override
    public Object get(String name) {
        return map.get(validName(name));
    }

    @Override
    public Set<String> getNames() {
        return map.keySet();
    }
}
