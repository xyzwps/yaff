package com.xyzwps.libs.yaff.flow;


import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flow {

    @Getter
    private final List<FlowNode> flowNodes;

    private final Map<String, FlowNode> idToNode = new HashMap<>();

    Flow(List<FlowNode> flowNodes) {
        this.flowNodes = flowNodes;

        // TODO: 检查
        // 1. 必须有一个 id 为 start 的节点
        //    不能有节点 id 叫 ctx
        // 2. 不必有 end 节点
        // 3. 每个节点的 next 必须真实存在
        // 4. 检查 node instance 和 node 本身是否匹配
        //
        //  if 节点后必须有两个 next

        for (FlowNode flowNode : flowNodes) {
            idToNode.put(flowNode.getId(), flowNode);
        }
    }

    FlowNode getFlowNode(String id) {
        return idToNode.get(id);
    }
}
