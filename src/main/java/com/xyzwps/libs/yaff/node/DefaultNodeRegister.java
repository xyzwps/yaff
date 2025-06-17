package com.xyzwps.libs.yaff.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultNodeRegister implements NodeRegister {

    private final Map<String, Node> nodes = new HashMap<>();

    public DefaultNodeRegister() {
        register(new NoopNode());
        register(new ControlNode.IfNode());
        register(new ControlNode.CaseNode());
        register(new ControlNode.WhenNode());
        register(new ControlNode.DefaultNode());
    }

    @Override
    public void register(Node node) {
        // TODO: 不能有冲突
        nodes.put(node.getName(), node);
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    @Override
    public List<NodeMetaData> getMetaData() {
        return nodes.values().stream().map(Node::getMetaData).toList();
    }
}
