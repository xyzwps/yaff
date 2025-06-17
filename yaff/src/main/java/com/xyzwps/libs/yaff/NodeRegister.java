package com.xyzwps.libs.yaff;

import java.util.List;

public interface NodeRegister {
    void register(Node node);

    Node getNode(String name);

    List<NodeMetaData> getMetaData();
}
