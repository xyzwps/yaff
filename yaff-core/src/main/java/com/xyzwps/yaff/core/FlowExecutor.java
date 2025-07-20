package com.xyzwps.yaff.core;

import java.util.HashMap;
import java.util.Map;

// TODO: 测试
// TODO: 中断后继续执行
public class FlowExecutor {

    private final NodeRegister nodeRegister;
    private final FlowExecutorListener listener;

    FlowExecutor(NodeRegister nodeRegister, FlowExecutorListener listener) {
        this.nodeRegister = nodeRegister;
        this.listener = listener == null ? FlowExecutorListener.NOOP : listener;
    }

    /// TODO: 把这个干了
    enum EndType {
        END,
        BRANCH_END
    }

    public void execute(Flow flow, FlowContext context) {
        execute(flow, context, NodeIds.START, EndType.END);
    }

    private void execute(Flow flow, FlowContext context, String start, EndType endType) {
        // init context
        flow.getFlowInputs().forEach(exp -> {
            var name = exp.getInputName();
            var value = exp.calculate(context, Object.class);
            context.set(name, value);
        });

        var currentId = start;
        while (!NodeIds.END.equals(currentId)) {
            var flowNode = getFlowNode(currentId, flow);
            var node = getNode(flowNode);

            listener.onFootPrint(new FootPrint.BeforeNode(currentId, node.getName()));

            var nextId = switch (node.getName()) {
                case ControlNode.ALL_NODE_NAME -> executeAll(flowNode, node, flow, context);
                default -> executeCommonNode(flowNode, node, context);
            };

            if (nextId == null) {
                break;
            }

            listener.onFootPrint(new FootPrint.ToNext(currentId, nextId));
            currentId = nextId;
        }
        switch (endType) {
            case END -> listener.onFootPrint(FootPrint.END);
            case BRANCH_END -> listener.onFootPrint(new FootPrint.BranchEnd(currentId));
        }
    }

    private String executeAll(FlowNode flowNode, Node node, Flow flow, FlowContext context) {
        var edges = flowNode.getEdges();
        if (edges == null) {
            return null;
        }

        // FIXME: context 必须是并发安全的
        for (var edge : edges) {
            execute(flow, context, edge.to(), EndType.BRANCH_END);
        }
        return null;
    }

    private Node getNode(FlowNode flowNode) {
        var node = nodeRegister.getNode(flowNode.getName());
        if (node == null) {
            throw new YaffException("Node not registered: name=" + flowNode.getName());
        }
        return node;
    }

    private static FlowNode getFlowNode(String id, Flow flow) {
        var flowNode = flow.getFlowNode(id);
        if (flowNode == null) {
            throw new YaffException("Node not found: " + id);
        }
        return flowNode;
    }

    private Map<String, Object> collectInputs(FlowNode flowNode, Node node, FlowContext context) {
        Map<String, Class<?>> inputTypes = new HashMap<>();
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
                    throw new YaffException("Invalid inputs name: " + inputName);
                }
                inputs.put(inputName, assignExpression.calculate(context, inputType));
            }
        }
        return inputs;
    }

    private String executeCommonNode(FlowNode flowNode, Node node, FlowContext context) {
        var inputs = collectInputs(flowNode, node, context);
        var output = node.execute(inputs);

        var ref = flowNode.getRef();
        if (ref != null && !ref.isEmpty()) {
            context.set(ref, output);
        }

        var edges = flowNode.getEdges();
        if (edges == null || edges.isEmpty()) {
            return null;
        }

        var fallbackEdge = edges.stream()
                .filter(edge -> edge.type() == FlowEdgeTo.EdgeType.FALLBACK)
                .findFirst()
                .orElseThrow();
        var checkEdges = edges.stream()
                .filter(edge -> edge.type() == FlowEdgeTo.EdgeType.CHECK)
                .toList();

        for (var checkEdge : checkEdges) {
            var checked = checkEdge.expression().calculate(context, boolean.class);
            if (checked) {
                return checkEdge.to();
            }
        }
        return fallbackEdge.to();
    }
}
