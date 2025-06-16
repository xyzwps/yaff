package com.xyzwps.libs.yaff.flow;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flow {

    private final List<NodeInstance> nodeInstances;

    private final Map<String, NodeInstance> idToNode = new HashMap<>();

    Flow(List<NodeInstance> nodeInstances) {
        this.nodeInstances = nodeInstances;
        for (NodeInstance nodeInstance : nodeInstances) {
            idToNode.put(nodeInstance.getId(), nodeInstance);
        }
    }

    NodeInstance getNodeInstance(String id) {
        return idToNode.get(id);
    }
}
