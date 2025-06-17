package com.xyzwps.libs.yaff.node;

import java.util.HashMap;
import java.util.Map;

public class DefaultNodeRegister implements NodeRegister {

    private final Map<String, Node> nodes = new HashMap<>();

    public DefaultNodeRegister() {
        register(new NoopNode());
        register(new ControlNode.IfNode());
        register(new ControlNode.CaseNode());
        register(new ControlNode.WhenNode());
        register(new ControlNode.DefaultNode());
//        register(new SwitchControl());
//        register(new DefaultControl());
//        register(new CaseControl());
    }

    @Override
    public void register(Node node) {
        // TODO: 不能有冲突
        nodes.put(node.getName(), node);
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }
}
