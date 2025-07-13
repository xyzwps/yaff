package com.xyzwps.yaff.core;

import java.util.HashMap;
import java.util.List;
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

    enum EndType {
        END,
        BRANCH_END
    }

    public void execute(Flow flow, FlowContext context) {
        execute(flow, context, NodeIds.START, EndType.END);
    }

    private void execute(Flow flow, FlowContext context, String start, EndType endType) {
        var currentId = start;
        while (!NodeIds.END.equals(currentId)) {
            var flowNode = getFlowNode(currentId, flow);
            var node = getNode(flowNode);

            listener.onFootPrint(new FootPrint.BeforeNode(currentId, node.getName()));

            var nextId = switch (node.getName()) {
                case ControlNode.CASE_NODE_NAME -> executeCase(flowNode, node, flow, context);
                case ControlNode.WHEN_NODE_NAME -> executeWhen(flowNode, node, context);
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
        var next = flowNode.getNext();
        if (next == null || next.isEmpty()) {
            listener.onFootPrint(FootPrint.END);
        } else {
            for (var nextId : next) {
                execute(flow, context, nextId, EndType.BRANCH_END);
            }
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
                    throw new YaffException("Invalid inputs name: " + inputName);
                }
                inputs.put(inputName, assignExpression.calculate(context, inputType));
            }
        }
        return inputs;
    }

    private String executeCommonNode(FlowNode flowNode, Node node, FlowContext context) {
        var currentId = flowNode.getId();
        var inputs = collectInputs(flowNode, node, context);
        listener.onFootPrint(new FootPrint.InputsCalculated(currentId, inputs));

        var output = node.execute(inputs);
        listener.onFootPrint(new FootPrint.OutputExecuted(currentId, output));

        var ref = flowNode.getRef();
        if (ref != null) {
            context.set(ref, output);
            listener.onFootPrint(new FootPrint.PutRefIntoContext(currentId, ref, output));
        }

        return first(flowNode.getNext());
    }


    private static <T> T first(List<T> list) {
        return list == null ? null : list.isEmpty() ? null : list.getFirst();
    }

    private String executeWhen(FlowNode whenFlowNode, Node whenNode, FlowContext context) {
        var whenId = whenFlowNode.getId();
        listener.onFootPrint(new FootPrint.CheckWhenNode(whenId));

        var inputs = collectInputs(whenFlowNode, whenNode, context);
        listener.onFootPrint(new FootPrint.InputsCalculated(whenId, inputs));

        var conditionValue = inputs.get(ControlNode.CONDITION);
        if (conditionValue instanceof Boolean bool && bool) {
            var afterId = first(whenFlowNode.getNext());
            if (afterId != null) {
                listener.onFootPrint(new FootPrint.ToNext(whenId, afterId));
            }
            return afterId;
        }
        return null; // end
    }

    private String executeCase(FlowNode flowNode, Node node, Flow flow, FlowContext context) {
        var caseId = flowNode.getId();
        var whenIds = flowNode.getNext();

        FlowNode defaultFlowNode = null;
        Node defaultNode = null;

        for (var whenId : whenIds) {
            var whenFlowNode = getFlowNode(whenId, flow);
            var whenNode = getNode(whenFlowNode);

            switch (whenNode.getName()) {
                case ControlNode.WHEN_NODE_NAME -> {
                    listener.onFootPrint(new FootPrint.CheckWhenNode(whenId));

                    var inputs = collectInputs(whenFlowNode, whenNode, context);
                    listener.onFootPrint(new FootPrint.InputsCalculated(whenId, inputs));

                    var conditionValue = inputs.get(ControlNode.CONDITION);
                    if (conditionValue instanceof Boolean bool && bool) {
                        var afterId = first(whenFlowNode.getNext());
                        if (afterId != null) {
                            listener.onFootPrint(new FootPrint.ToNext(whenId, afterId));
                        }
                        return afterId;
                    }
                }
                case ControlNode.DEFAULT_NODE_NAME -> {
                    if (defaultNode == null) {
                        defaultNode = whenNode;
                        defaultFlowNode = whenFlowNode;
                    } else {
                        throw new YaffException("Duplicate default node");
                    }
                }
                default -> throw new YaffException("Invalid node: " + whenNode.getName());
            }
        }

        if (defaultFlowNode != null) {
            listener.onFootPrint(new FootPrint.ToNext(caseId, defaultFlowNode.getId()));

            var afterId = first(defaultFlowNode.getNext());
            if (afterId != null) {
                listener.onFootPrint(new FootPrint.ToNext(defaultFlowNode.getId(), afterId));
            }
            return afterId;
        }
        throw new YaffException("No default node found");
    }
}
