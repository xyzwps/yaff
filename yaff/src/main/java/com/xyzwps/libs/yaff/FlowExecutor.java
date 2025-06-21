package com.xyzwps.libs.yaff;

import java.util.HashMap;
import java.util.Map;

// TODO: 测试
public class FlowExecutor {

    private final NodeRegister nodeRegister;

    FlowExecutor(NodeRegister nodeRegister) {
        this.nodeRegister = nodeRegister;
    }

    public void execute(Flow flow, FlowContext context) {
        var currentId = NodeIds.START;
        while (!NodeIds.END.equals(currentId)) {
            var flowNode = getFlowNode(currentId, flow);
            var node = getNode(flowNode);

            var inputs = collectInputs(flowNode, node, context);
            var output = node.execute(inputs);
            var ref = flowNode.getRef();
            if (ref != null) {
                context.set(ref, output);
            }

            var next = flowNode.getNext();
            if (next == null || next.isEmpty()) {
                break;
            } else if (next.size() > 1) {
                currentId = executeControl(output, flowNode, node, flow, context);
            } else {
                currentId = next.getFirst();
            }
        }
    }

    private Node getNode(FlowNode flowNode) {
        var node = nodeRegister.getNode(flowNode.getName());
        if (node == null) {
            throw new RuntimeException("Node not registered: name=" + flowNode.getName());
        }
        return node;
    }

    private static FlowNode getFlowNode(String id, Flow flow) {
        var flowNode = flow.getFlowNode(id);
        if (flowNode == null) {
            throw new IllegalArgumentException("Node not found: " + id);
        }
        return flowNode;
    }

    private Map<String, Object> collectInputs(FlowNode flowNode, Node node, FlowContext context) {
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
                    throw new RuntimeException("Invalid inputs name: " + inputName);
                }
                inputs.put(inputName, assignExpression.calculate(context, inputType));
            }
        }
        return inputs;
    }

    /**
     * 执行控制流，返回执行完之后下一个应该执行的节点 id。
     */
    private String executeControl(Object output, FlowNode flowNode, Node node, Flow flow, FlowContext context) {
        return switch (node.getName()) {
            case ControlNode.CASE_NODE_NAME -> executeCaseWhen(flowNode, node, flow, context);
            default -> throw new RuntimeException("Invalid node: " + node.getName());
        };
    }

    private String executeCaseWhen(FlowNode flowNode, Node node, Flow flow, FlowContext context) {
        var nextIds = flowNode.getNext();

        FlowNode defaultFlowNode = null;
        Node defaultNode = null;

        for (var nextId : nextIds) {
            var nextFlowNode = flow.getFlowNode(nextId);
            if (nextFlowNode == null) {
                throw new RuntimeException("Node not found: id=" + nextId);
            }
            var nextNode = nodeRegister.getNode(nextFlowNode.getName());
            if (nextNode == null) {
                throw new RuntimeException("Node not registered: name=" + nextFlowNode.getName());
            }

            switch (nextNode.getName()) {
                case ControlNode.WHEN_NODE_NAME -> {
                    var inputs = collectInputs(nextFlowNode, nextNode, context);
                    var conditionValue = inputs.get(ControlNode.CONDITION);
                    if (conditionValue instanceof Boolean bool && bool) {
                        return nextFlowNode.getNext().getFirst(); // TODO: 验证确实有且仅有一个
                    }
                }
                case ControlNode.DEFAULT_NODE_NAME -> {
                    if (defaultNode == null) {
                        defaultNode = nextNode;
                        defaultFlowNode = nextFlowNode;
                    } else {
                        throw new IllegalStateException("Duplicate default node");
                    }
                }
                default -> throw new RuntimeException("Invalid node: " + nextNode.getName());
            }
        }

        if (defaultFlowNode != null) {
            return defaultFlowNode.getNext().getFirst();  // TODO: 验证确实有且仅有一个
        }
        throw new IllegalStateException("No default node found");
    }
}
