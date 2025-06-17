package com.xyzwps.libs.yaff;

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
        var name = node.getName();
        if (name == null) {
            throw new RuntimeException("Node name cannot be null");
        }

        if (nodes.containsKey(name)) {
            throw new RuntimeException("Node already registered: name=" + name);
        }

        nodes.put(name, node);
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    @Override
    public List<NodeMetaData> getMetaData() {
        return nodes.values().stream().map(Node::getMetaData).toList();
    }
}
