package com.xyzwps.yaff.core;

import com.xyzwps.yaff.core.commons.Arguments;
import com.xyzwps.yaff.core.commons.JSON;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class FlowFactory {

    private final NodeRegister nodeRegister;

    public FlowFactory(NodeRegister nodeRegister) {
        this.nodeRegister = Arguments.notNull(nodeRegister, "nodeRegister");
    }

    public FlowFactory() {
        this(new DefaultNodeRegister());
    }

    public FlowFactory register(Node node) {
        nodeRegister.register(node);
        return this;
    }

    public Flow createFlow(List<FlowNode> nodes) {
        return createFlow(nodes, null);
    }

    public Flow createFlow(List<FlowNode> nodes, List<AssignExpression> init) {
        check(nodes);
        return new Flow(nodes, init);
    }

    public Flow fromJSON(String json) {
        var flow = JSON.parse(json, FlowData.class);
        check(flow.getFlowNodes());
        return new Flow(flow.getFlowNodes(), flow.getFlowInputs());
    }

    @Data
    static class FlowData {
        private List<FlowNode> flowNodes;
        private List<AssignExpression> flowInputs;
    }

    /// 这里 check {@link Flow} check 不了的东西。
    private void check(List<FlowNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Argument nodes cannot be null or empty.");
        }

        for (FlowNode node : nodes) {
            if (nodeRegister.getNode(node.getName()) == null) {
                throw new YaffException("Node not registered: name=" + node.getName());
            }
        }
    }

    public FlowExecutor getExecutor() {
        return new FlowExecutor(nodeRegister, null);
    }

    public FlowExecutor getExecutor(FlowExecutorListener listener) {
        return new FlowExecutor(nodeRegister, listener);
    }
}
