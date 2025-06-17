package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.commons.NodeIds;
import com.xyzwps.libs.yaff.node.ControlNode;
import com.xyzwps.libs.yaff.node.Node;
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
            var flowNode = flow.getFlowNode(currentId);
            if (flowNode == null) {
                throw new RuntimeException("Node not found: id=" + currentId);
            }

            var node = nodeRegister.getNode(flowNode.getName());
            if (node == null) {
                throw new RuntimeException("Node not registered: name=" + flowNode.getName());
            }

            System.out.println("=> Step(" + step + ") " + node.getName() + " | " + flowNode.getId());
            var inputs = collectInputs(flowNode, node, context);
            node.execute(inputs, new PrefixedContext(flowNode.getId(), context));

            var next = flowNode.getNext();
            if (next == null || next.isEmpty()) {
                break;
            } else if (next.size() > 1) {
                currentId = executeControl(flowNode, node, flow, context);
            } else {
                currentId = next.getFirst();
            }
        }
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
                    throw new RuntimeException("Invalid input name: " + inputName);
                }
                inputs.put(inputName, assignExpression.calculate(context, inputType));
            }
        }
        return inputs;
    }

    /**
     * 执行控制流，返回执行完之后下一个应该执行的节点 id。
     */
    private String executeControl(FlowNode flowNode, Node node, Flow flow, FlowContext context) {
        return switch (node) {
            case ControlNode.IfNode ifNode -> executeIf(flowNode, context);
            case ControlNode.CaseNode caseNode -> executeCaseWhen(flowNode, caseNode, flow, context);
            default -> throw new RuntimeException("Invalid node: " + node.getName());
        };
    }

    private String executeIf(FlowNode flowNode, FlowContext context) {
        // TODO: 直接从 input 里就可以判断，还要不要污染 context？
        var result = context.get(flowNode.getId() + ".result");
        if (result instanceof Boolean bool && bool) {
            return flowNode.getNext().getFirst();
        } else {
            return flowNode.getNext().get(1);
        }
    }

    private String executeCaseWhen(FlowNode flowNode, ControlNode.CaseNode node, Flow flow, FlowContext context) {
        var nextIds = flowNode.getNext();

        FlowNode defaultFlowNode = null;
        ControlNode.DefaultNode defaultNode = null;

        for (var nextId : nextIds) {
            var nextFlowNode = flow.getFlowNode(nextId);
            if (nextFlowNode == null) {
                throw new RuntimeException("Node not found: id=" + nextId);
            }
            var nextNode = nodeRegister.getNode(nextFlowNode.getName());
            if (nextNode == null) {
                throw new RuntimeException("Node not registered: name=" + nextFlowNode.getName());
            }

            switch (nextNode) {
                case ControlNode.WhenNode whenNode -> {
                    var inputs = collectInputs(nextFlowNode, whenNode, context);
                    var conditionValue = inputs.get(ControlNode.WhenNode.CONDITION);
                    if (conditionValue instanceof Boolean bool && bool) {
                        return nextFlowNode.getNext().getFirst(); // TODO: 验证确实有且仅有一个
                    }
                }
                case ControlNode.DefaultNode it -> {
                    if (defaultNode == null) {
                        defaultNode = it;
                        defaultFlowNode = nextFlowNode;
                    } else {
                        throw new IllegalStateException("Duplicate default node"); // TODO: 检查确实只有一个 default node
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
