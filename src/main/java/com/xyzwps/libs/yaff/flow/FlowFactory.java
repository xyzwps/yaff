package com.xyzwps.libs.yaff.flow;

import com.xyzwps.libs.yaff.node.DefaultNodeRegister;
import com.xyzwps.libs.yaff.node.Node;
import com.xyzwps.libs.yaff.node.NodeRegister;

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

    public Flow createFlowCanvas(List<NodeInstance> nodes) {
        check(nodes);
        return new Flow(nodes);
    }

    private void check(List<NodeInstance> nodes) {
        // TODO:
    }

    public FlowExecutor createExecutor() {
        return new FlowExecutor(nodeRegister);
    }
}
