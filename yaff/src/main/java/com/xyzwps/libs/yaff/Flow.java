package com.xyzwps.libs.yaff;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flow {

    @Getter
    private final List<FlowNode> flowNodes;

    @JsonIgnore
    private final Map<String, FlowNode> idToNode = new HashMap<>();

    Flow(List<FlowNode> flowNodes) {
        this.flowNodes = flowNodes;
        for (var flowNode : flowNodes) {
            var id = flowNode.getId();
            if (idToNode.containsKey(id)) {
                throw new IllegalArgumentException("flow node id must be unique: " + id);
            }
            idToNode.put(id, flowNode);
        }
        this.check();
    }

    /// 检查规则:
    ///  - id 规则
    ///    - 必须有一个 id 为 start 的节点
    ///    - 不能有节点 id 叫 ctx
    ///  - 节点的 next 必须真实存在，除非 next 是 end
    ///  - control 节点规则
    ///    - if 节点: 必须有两个 next
    ///    - case 节点: 最多可以有一个 default next，至少有一个 when next，不可以有其他 next
    ///  - 检查 node instance 和 node 本身是否匹配 TODO: 这个暂时检查不了
    ///  - TODO: 确保是有向无环图
    private void check() {
        if (!idToNode.containsKey(NodeIds.START)) {
            throw new IllegalStateException("No start node found");
        }
        if (idToNode.containsKey(NodeIds.CTX)) {
            throw new IllegalStateException("Node id cannot be ctx");
        }


        for (var node : flowNodes) {
            var next = node.getNext();
            if (next == null || next.isEmpty()) {
                continue;
            }

            for (var n : next) {
                if (NodeIds.END.equals(n)) {
                    continue; // ok
                }
                if (idToNode.containsKey(n)) {
                    continue; // ok
                }
                throw new IllegalArgumentException("Invalid next '%s' of node %s".formatted(n, node.getId()));
            }
        }

        for (var node : flowNodes) {
            var name = node.getName();
            switch (name) {
                case ControlNode.IF_NODE_NAME -> {
                    var next = node.getNext();
                    if (next == null || next.size() != 2) {
                        throw new IllegalArgumentException("If node %s should have two next.".formatted(node.getId()));
                    }
                }
                case ControlNode.CASE_NODE_NAME -> {
                    var next = node.getNext();
                    if (next == null || next.isEmpty()) {
                        throw new IllegalArgumentException("Case node %s should have at least one next.".formatted(node.getId()));
                    }
                    int whenCount = 0;
                    int defaultCount = 0;
                    for (var nextId : next) {
                        var nextNode = idToNode.get(nextId);
                        switch (nextNode.getName()) {
                            case ControlNode.WHEN_NODE_NAME -> whenCount++;
                            case ControlNode.DEFAULT_NODE_NAME -> defaultCount++;
                            default -> throw new IllegalStateException("Case node %s has an unexpected next %s"
                                    .formatted(node.getId(), nextId));
                        }
                    }
                    if (whenCount == 0) {
                        throw new IllegalStateException("Case node %s should have at least one WHEN node"
                                .formatted(node.getId()));
                    }
                    if (defaultCount > 1) {
                        throw new IllegalStateException("Case node %s has more than one default node"
                                .formatted(node.getId()));
                    }
                }
            }
        }
    }


    FlowNode getFlowNode(String id) {
        return idToNode.get(id);
    }
}
