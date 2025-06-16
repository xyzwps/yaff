package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.commons.NodeIds;
import com.xyzwps.libs.yaff.node.NodeRegister;
import com.xyzwps.libs.yaff.node.Parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FlowExecutor {

    private final NodeRegister nodeRegister;

    FlowExecutor(NodeRegister nodeRegister) {
        this.nodeRegister = nodeRegister;
    }

    public void execute(Flow flow, FlowContext context) {

        var currentId = NodeIds.START;
        while (!NodeIds.END.equals(currentId)) {
            var flowNode = flow.getNodeInstance(currentId);
            if (flowNode == null) {
                throw new RuntimeException("Node not found: id=" + currentId);
            }

            var node = nodeRegister.getNode(flowNode.getName());
            if (node == null) {
                throw new RuntimeException("Node not registered: name=" + flowNode.getName());
            }

            Map<String, Parameter.Type> inputTypes = new HashMap<>();
            for (var input : node.getInputs()) {
                inputTypes.put(input.name(), input.type());
            }

            Map<String, Object> inputs = new HashMap<>();
            for (var assignExpression : flowNode.getAssignExpressions()) {
                var inputName = assignExpression.getInputName();
                var inputType = inputTypes.get(inputName);
                if (inputType == null) {
                    throw new RuntimeException("Invalid input name: " + inputName);
                }
                inputs.put(inputName, assignExpression.calculate(context, inputType));
            }
            node.execute(inputs, new PrefixedContext(flowNode.getId(), context));
            currentId = flowNode.getNext().get(0); // TODO: decide next node
        }
    }


    private record PrefixedContext(String prefix, FlowContext context) implements FlowContext {

        @Override
        public void set(String name, Object value) {
            context.set(prefix + "." + name, value);
        }

        @Override
        public Object get(String name) {
            return context.get(name);
        }

        @Override
        public Set<String> getNames() {
            return context.getNames();
        }
    }
}
