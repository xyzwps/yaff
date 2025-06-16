package com.xyzwps.libs.yaff.flow;


import java.util.Set;

public interface FlowContext {

    /**
     * Set Context Variable
     *
     * @param name  Variable Name
     * @param value Variable Value TODO: 限制值域
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

    static FlowContext create() {
        return new SimpleFlowContext();
    }
}
