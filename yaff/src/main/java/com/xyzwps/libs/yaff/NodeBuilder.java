package com.xyzwps.libs.yaff;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class NodeBuilder {
    private String name;
    private String description;
    private List<NodeInput> inputs;
    private NodeOutput output;
    private Function<Map<String, Object>, Object> execute;

    public Node build() {
        if (name == null) {
            throw new IllegalArgumentException("Node name cannot be null");
        }
        if (!Node.NODE_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Node name is invalid");
        }

        if (description == null) {
            description = "";
        }

        if (inputs == null) {
            inputs = List.of();
        }
        for (var input : inputs) {
            if (input == null) {
                throw new IllegalArgumentException("Input is null");
            }
        }

        if (execute == null) {
            execute = NodeBuilder::doNothing;
        }

        return new TemplateNode(name, description, inputs, output, execute);
    }

    private static Object doNothing(Map<String, Object> inputs) {
        return null;
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

    public NodeBuilder inputs(NodeInput... inputs) {
        this.inputs = Arrays.asList(inputs);
        return this;
    }

    public NodeBuilder output(NodeOutput output) {
        this.output = output;
        return this;
    }

    public NodeBuilder execute(Function<Map<String, Object>, Object> execute) {
        this.execute = execute;
        return this;
    }

    @Getter
    private static final class TemplateNode implements Node {
        private final NodeMetaData metaData;
        private final Function<Map<String, Object>, Object> execute;

        private TemplateNode(String name, String description,
                             List<NodeInput> inputs, NodeOutput output,
                             Function<Map<String, Object>, Object> execute) {
            this.metaData = new NodeMetaData(name, description,
                    Collections.unmodifiableList(inputs), output);
            this.execute = execute;
        }

        @Override
        public Object execute(Map<String, Object> inputs) {
            return execute.apply(inputs);
        }
    }
}