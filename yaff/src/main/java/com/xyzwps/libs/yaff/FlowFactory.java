package com.xyzwps.libs.yaff;

import com.xyzwps.libs.yaff.commons.Arguments;
import com.xyzwps.libs.yaff.commons.JSON;
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
        check(nodes);
        return new Flow(nodes);
    }

    public Flow fromJSON(String json) {
        var flow = JSON.parse(json, FlowData.class);
        check(flow.getFlowNodes());
        return new Flow(flow.getFlowNodes());
    }

    @Data
    static class FlowData {
        private List<FlowNode> flowNodes;
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
