package com.xyzwps.libs.yaff;

import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Getter
public final class NodeTemplate implements Node {
    private final String name;
    private final String description;
    private final List<Parameter> inputs;
    private final List<Parameter> outputs;
    private final BiConsumer<Map<String, Object>, FlowContext> execute;

    public NodeTemplate(String name, String description,
                        List<Parameter> inputs, List<Parameter> outputs,
                        BiConsumer<Map<String, Object>, FlowContext> execute) {
        // TODO: 检查参数
        this.name = name;
        this.description = description;
        this.inputs = Collections.unmodifiableList(inputs);
        this.outputs = Collections.unmodifiableList(outputs);
        this.execute = execute;
    }

    private static void doNothing(Map<String, Object> inputs, FlowContext context) {
    }

    public NodeTemplate(String name, String description,
                        List<Parameter> inputs, List<Parameter> outputs) {
        this(name, description, inputs, outputs, NodeTemplate::doNothing);
    }

    @Override
    public void execute(Map<String, Object> inputs, FlowContext context) {
        execute.accept(inputs, context);
    }
}
