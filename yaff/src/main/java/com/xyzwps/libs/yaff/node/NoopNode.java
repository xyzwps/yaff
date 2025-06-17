package com.xyzwps.libs.yaff.node;

import com.xyzwps.libs.yaff.flow.FlowContext;

import java.util.List;
import java.util.Map;

public class NoopNode implements Node {

    public static final String NAME = "yaff.noop";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return NAME;
    }

    @Override
    public List<Parameter> getInputs() {
        return List.of();
    }

    @Override
    public List<Parameter> getOutputs() {
        return List.of();
    }

    @Override
    public void execute(Map<String, Object> inputs, FlowContext context) {
    }
}
