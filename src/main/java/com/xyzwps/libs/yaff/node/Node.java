package com.xyzwps.libs.yaff.node;

import com.xyzwps.libs.yaff.flow.FlowContext;

import java.util.List;
import java.util.Map;

public interface Node {
    String getName();

    // TODO: 应该返回一个 map
    List<Parameter> getInputs();

    List<Parameter> getOutputs();

    default NodeMetaData getMetaData() {
        return new NodeMetaData(getName(), getInputs(), getOutputs());
    }

    void execute(Map<String, Object> inputs, FlowContext context);
}
