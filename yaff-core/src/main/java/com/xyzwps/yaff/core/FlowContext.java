package com.xyzwps.yaff.core;

import java.util.Map;
import java.util.Set;

public interface FlowContext {

    /**
     * Set Context Variable
     *
     * @param name  Variable Name
     * @param value Variable Value
     */
    void set(String name, Object value);

    /**
     * Get Context Variable
     *
     * @param name Variable Name
     * @return Variable Value
     */
    Object get(String name);

    Set<String> getNames();

    FlowContext clone();

    Map<String, Object> toMap();

    static FlowContext create() {
        return new SimpleFlowContext();
    }

}
