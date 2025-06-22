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
/// 检查规则:
/// - R1: id 规则
///   - 各个节点的 id 必须唯一，不能重复，也不能为 null。
///   - 必须有一个 id 为 start 的节点
/// - R2: next 规则
///   - case 节点至少应当由两个 next，并且
///     - next 节点必须存在
///     - 最多有一个 default 节点
///     - 其他都是 when 节点
///   - all 节点的 next 可以有多个
///   - when 节点的 parent 不必是 case 节点
///   - default 节点的 parent 不必是 case 节点
///   - end 节点没有 next
///   - 其他节点只有一个 next，且 next 必须存在，除非 next 是 end
/// - R3: control 节点规则
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
                throw new YaffException("FlowNode id cannot be null.");
            }
            if (idToNode.containsKey(id)) {
                throw new YaffException("FlowNode id must be unique: " + id);
            }
            idToNode.put(id, flowNode);
        }

        if (!idToNode.containsKey(NodeIds.START)) {
            throw new YaffException("No start node found");
        }

        return idToNode;
    }

    private void r2() {
        for (var node : flowNodes) {
            var next = node.getNext();
            switch (node.getName()) {
                case ControlNode.CASE_NODE_NAME -> {
                    if (next == null || next.size() <= 1) {
                        throw new YaffException("Case node should have at least two next nodes.");
                    }
                    int defaultCount = 0;
                    int whenCount = 0;
                    int restCount = 0;
                    for (String n : next) {
                        var nextNode = getFlowNode(n);
                        if (nextNode == null) {
                            throw new YaffException("FlowNode %s does not exist.".formatted(n));
                        }

                        switch (nextNode.getName()) {
                            case ControlNode.WHEN_NODE_NAME -> whenCount++;
                            case ControlNode.DEFAULT_NODE_NAME -> defaultCount++;
                            default -> restCount++;
                        }
                    }
                    if (defaultCount > 1) {
                        throw new YaffException("Duplicate default nodes of case node " + node.getId());
                    }
                    if (whenCount == 0) {
                        throw new YaffException("No when nodes of case node " + node.getId());
                    }
                    if (restCount > 0) {
                        throw new YaffException("Unexpected next of case node " + node.getId());
                    }
                }
                case ControlNode.END_NODE_NAME -> {
                    if (next != null && !next.isEmpty()) {
                        throw new YaffException("End node cannot have next.");
                    }
                }
                case ControlNode.ALL_NODE_NAME -> {
                    if (next == null || next.isEmpty()) {
                        // do nothing
                    } else {
                        // next node should exist
                        for (var n : next) {
                            if (NodeIds.END.equals(n)) {
                                continue; // ok
                            }
                            if (idToNode.containsKey(n)) {
                                continue; // ok
                            }
                            throw new YaffException("FlowNode %s does not exist.".formatted(n));
                        }
                    }
                }
                default -> {
                    if (next == null || next.isEmpty()) {
                        // do nothing
                    } else if (next.size() > 1) {
                        throw new YaffException("Node %s cannot have more than one next.".formatted(node.getId()));
                    } else {
                        // next node should exist
                        var n = next.getFirst();
                        if (NodeIds.END.equals(n)) {
                            continue; // ok
                        }
                        if (idToNode.containsKey(n)) {
                            continue; // ok
                        }
                        throw new YaffException("FlowNode %s does not exist.".formatted(n));
                    }
                }
            }
        }
    }

    private void r3() {
    }

    private void r4() {
        var isDag = DAGChecker.isDAG(flowNodes, FlowNode::getId, FlowNode::getNext);
        if (!isDag) {
            throw new YaffException("Flow is not a DAG.");
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
                    throw new YaffException("FlowNode %s has no parent.".formatted(node.getId()));
                }
            }
        }

    }

    FlowNode getFlowNode(String id) {
        return idToNode.get(id);
    }
}
