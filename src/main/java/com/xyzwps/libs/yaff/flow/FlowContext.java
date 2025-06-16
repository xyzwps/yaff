package com.xyzwps.libs.yaff.flow;


import java.util.Set;
import java.util.regex.Pattern;

public interface FlowContext {


    Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");

    /**
     * Set Context Variable
     *
     * @param name  Variable Name should match with {@link #NAME_PATTERN}
     * @param value Variable Value TODO: 限制值域
     */
    void set(String name, Object value);

    /**
     * Get Context Variable
     *
     * @param name Variable Name should match with {@link #NAME_PATTERN}
     * @return Variable Value
     */
    Object get(String name);

    Set<String> getNames();

    static FlowContext create() {
        return new SimpleFlowContext();
    }
}
