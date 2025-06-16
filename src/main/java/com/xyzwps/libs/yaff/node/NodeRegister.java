package com.xyzwps.libs.yaff.node;

public interface NodeRegister {
    void register(Node node);

    Node getNode(String name);
}
