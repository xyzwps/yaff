package com.xyzwps.libs.yaff;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class NodeBuilder {
    private String name;
    private String description;
    private List<Parameter> inputs;
    private List<Parameter> outputs;
    private BiConsumer<Map<String, Object>, FlowContext> execute;

    public Node build() {
        // TODO: 限制 name 格式
        if (name == null) {
            throw new IllegalArgumentException("Node name cannot be null");
        }

        if (description == null) {
            description = "";
        }

        // TODO: 检查 input 每个参数
        if (inputs == null) {
            inputs = List.of();
        }

        // TODO: 检查 input 每个参数
        if (outputs == null) {
            outputs = List.of();
        }

        if (execute == null) {
            execute = NodeBuilder::doNothing;
        }

        return new TemplateNode(name, description, inputs, outputs, execute);
    }

    private static void doNothing(Map<String, Object> inputs, FlowContext context) {
    }

    NodeBuilder() {
    }

    public NodeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public NodeBuilder description(String description) {
        this.description = description;
        return this;
    }

    public NodeBuilder inputs(Parameter... inputs) {
        this.inputs = Arrays.asList(inputs);
        return this;
    }

    public NodeBuilder outputs(Parameter... outputs) {
        this.outputs = Arrays.asList(outputs);
        return this;
    }

    public NodeBuilder execute(BiConsumer<Map<String, Object>, FlowContext> execute) {
        this.execute = execute;
        return this;
    }

    @Getter
    private static final class TemplateNode implements Node {
        private final String name;
        private final String description;
        private final List<Parameter> inputs;
        private final List<Parameter> outputs;
        private final BiConsumer<Map<String, Object>, FlowContext> execute;

        private TemplateNode(String name, String description,
                             List<Parameter> inputs, List<Parameter> outputs,
                             BiConsumer<Map<String, Object>, FlowContext> execute) {
            this.name = name;
            this.description = description;
            this.inputs = Collections.unmodifiableList(inputs);
            this.outputs = Collections.unmodifiableList(outputs);
            this.execute = execute;
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
            execute.accept(inputs, context);
        }
    }
}