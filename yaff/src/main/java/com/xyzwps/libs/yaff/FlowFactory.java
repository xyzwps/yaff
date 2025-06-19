package com.xyzwps.libs.yaff;

import com.xyzwps.libs.yaff.commons.JSON;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class FlowFactory {

    private final NodeRegister nodeRegister;

    public FlowFactory(NodeRegister nodeRegister) {
        this.nodeRegister = Objects.requireNonNull(nodeRegister, "NodeRegister cannot be null");
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
        if (nodes == null) {
            throw new IllegalArgumentException("nodes cannot be null");
        }
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("nodes cannot be empty");
        }

        for (FlowNode node : nodes) {
            if (nodeRegister.getNode(node.getName()) == null) {
                throw new IllegalArgumentException("Node not registered: name=" + node.getName());
            }
        }
    }

    public FlowExecutor getExecutor() {
        return new FlowExecutor(nodeRegister);
    }
}
