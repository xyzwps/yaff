package com.xyzwps.libs.yaff;

import java.util.List;
import java.util.Map;

public interface Node {
    String getName();

    // TODO: 应该返回一个 map
    List<Parameter> getInputs();

    List<Parameter> getOutputs();

    String getDescription();

    default NodeMetaData getMetaData() {
        return new NodeMetaData(getName(), getInputs(), getOutputs(), getDescription());
    }

    /**
     * TODO: 返回一个新上下文，避免修改原上下文是不是好一点？
     */
    void execute(Map<String, Object> inputs, FlowContext context);
}
