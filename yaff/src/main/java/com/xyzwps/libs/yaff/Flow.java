package com.xyzwps.libs.yaff;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xyzwps.libs.yaff.commons.DAGChecker;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// 表示一个由节点组成的流程。
///
/// 在执行本检查之前，应先确保 {@link FlowNode#getName()} 对应的 {@link Node} 确实存在。
/// 这里没有 {@link NodeRegister} 作为上下文，无法进行此项检查。
///
/// TODO: 测试
///
/// 检查规则:
/// - R1: id 规则
///   - 各个节点的 id 必须唯一，不能重复，也不能为 null。
///   - 必须有一个 id 为 start 的节点
/// - R2: next 规则
///   - 节点的 next 必须真实存在，除非 next 是 end
/// - R3: control 节点规则
///   - if 节点: 必须有两个 next
///   - case 节点: 最多可以有一个 default next，至少有一个 when next，不可以有其他 next
/// - R4: 图规则
///   - 确保是有向无环图
///   - 除了 start 节点外，所有节点都必须至少有一个 parent。
public class Flow {

    @Getter
    private final List<FlowNode> flowNodes;

    @JsonIgnore
    private final Map<String, FlowNode> idToNode;

    Flow(List<FlowNode> flowNodes) {
        this.flowNodes = flowNodes;
        this.idToNode = r1(flowNodes);
        this.r2();
        this.r3();
        this.r4();
    }

    private static Map<String, FlowNode> r1(List<FlowNode> flowNodes) {
        var idToNode = new HashMap<String, FlowNode>();
        for (var flowNode : flowNodes) {
            var id = flowNode.getId();
            if (id == null) {
                throw new IllegalArgumentException("flow node id cannot be null");
            }
            if (idToNode.containsKey(id)) {
                throw new IllegalArgumentException("flow node id must be unique: " + id);
            }
            idToNode.put(id, flowNode);
        }

        if (!idToNode.containsKey(NodeIds.START)) {
            throw new IllegalStateException("No start node found");
        }

        return idToNode;
    }

    private void r2() {
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
    }

    private void r3() {
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

    private void r4() {
        var isDag = DAGChecker.isDAG(flowNodes, FlowNode::getId, FlowNode::getNext);
        if (!isDag) {
            throw new IllegalStateException("Flow is not a DAG");
        }

        {
            var idToParents = new HashMap<String, List<String>>();
            for (var node : flowNodes) {
                var next = node.getNext();
                if (next != null) {
                    for (var nextId : next) {
                        if (!idToParents.containsKey(nextId)) {
                            idToParents.put(nextId, new ArrayList<>());
                        }
                        idToParents.get(nextId).add(node.getId());
                    }
                }
            }

            for (var node : flowNodes) {
                if (NodeIds.START.equals(node.getId())) {
                    continue;
                }

                var parents = idToParents.get(node.getId());
                if (parents == null || parents.isEmpty()) {
                    throw new IllegalStateException("Node %s has no parent".formatted(node.getId()));
                }
            }
        }

    }

    FlowNode getFlowNode(String id) {
        return idToNode.get(id);
    }
}
