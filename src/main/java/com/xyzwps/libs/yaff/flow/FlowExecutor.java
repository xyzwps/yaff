package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.commons.NodeIds;
import com.xyzwps.libs.yaff.node.ControlNode;
import com.xyzwps.libs.yaff.node.NodeRegister;
import com.xyzwps.libs.yaff.node.ParameterType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FlowExecutor {

    private final NodeRegister nodeRegister;

    FlowExecutor(NodeRegister nodeRegister) {
        this.nodeRegister = nodeRegister;
    }

    public void execute(Flow flow, FlowContext context) {

        var currentId = NodeIds.START;
        var step = 0;
        while (!NodeIds.END.equals(currentId)) {
            step++;
            // TODO: 避免无限循环
            var flowNode = flow.getNodeInstance(currentId);
            if (flowNode == null) {
                throw new RuntimeException("Node not found: id=" + currentId);
            }

            var node = nodeRegister.getNode(flowNode.getName());
            if (node == null) {
                throw new RuntimeException("Node not registered: name=" + flowNode.getName());
            }

            System.out.println("=> Step(" + step + ") " + node.getName() + " | " + flowNode.getId());
            Map<String, ParameterType> inputTypes = new HashMap<>();
            for (var input : node.getInputs()) {
                inputTypes.put(input.name(), input.type());
            }

            Map<String, Object> inputs = new HashMap<>();
            var assignExpressions = flowNode.getAssignExpressions();
            if (assignExpressions != null && !assignExpressions.isEmpty()) {
                for (var assignExpression : assignExpressions) {
                    var inputName = assignExpression.getInputName();
                    var inputType = inputTypes.get(inputName);
                    if (inputType == null) {
                        throw new RuntimeException("Invalid input name: " + inputName);
                    }
                    inputs.put(inputName, assignExpression.calculate(context, inputType));
                }
            }
            node.execute(inputs, new PrefixedContext(flowNode.getId(), context));

            var next = flowNode.getNext();
            if (next == null || next.isEmpty()) {
                break;
            } else if (next.size() > 1) {
                switch (node) {
                    case ControlNode.IfNode ifNode -> {
                        var result = context.get(flowNode.getId() + ".result");
                        if (result instanceof Boolean bool && bool) {
                            currentId = flowNode.getNext().getFirst();
                        } else {
                            currentId = flowNode.getNext().get(1);
                        }
                    }
                    default -> throw new RuntimeException("Invalid node: " + node.getName());
                }
            } else {
                currentId = next.getFirst();
            }
        }
    }

    private record PrefixedContext(String prefix, FlowContext context) implements FlowContext {

        @Override
        public void set(String name, Object value) {
            // TODO: 检查 name 是否是合法的输出名称
            if (name == null) {
                throw new IllegalArgumentException("Context variable name cannot be null");
            }

            if (SimpleFlowContext.NAME_PATTERN.matcher(name).matches()) {
                context.set(prefix + "." + name, value);
            } else {
                throw new IllegalArgumentException("Invalid context variable name: " + name);
            }
        }

        @Override
        public Object get(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Context variable name cannot be null");
            }
            return context.get(name);
        }

        @Override
        public Set<String> getNames() {
            return context.getNames();
        }
    }
}
