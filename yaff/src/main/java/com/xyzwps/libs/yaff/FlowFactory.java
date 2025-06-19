package com.xyzwps.libs.yaff;

import com.xyzwps.libs.yaff.commons.JSON;
import lombok.Data;

import java.util.List;
import java.util.Objects;

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

    public NodeRegister getNodeRegister() {
        return nodeRegister;
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


    private void check(List<FlowNode> nodes) {
        // TODO:
    }

    public FlowExecutor createExecutor() {
        return new FlowExecutor(nodeRegister);
    }
}
