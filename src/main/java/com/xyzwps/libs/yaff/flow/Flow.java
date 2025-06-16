package com.xyzwps.libs.yaff.flow;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flow {

    private final List<NodeInstance> nodeInstances;

    private final Map<String, NodeInstance> idToNode = new HashMap<>();

    Flow(List<NodeInstance> nodeInstances) {
        this.nodeInstances = nodeInstances;

        // TODO: 检查
        // 1. 必须有一个 id 为 start 的节点
        // 2. 不必有 end 节点
        // 3. 每个节点的 next 必须真实存在
        // 4. 检查 node instance 和 node 本身是否匹配

        for (NodeInstance nodeInstance : nodeInstances) {
            idToNode.put(nodeInstance.getId(), nodeInstance);
        }
    }

    NodeInstance getNodeInstance(String id) {
        return idToNode.get(id);
    }
}
