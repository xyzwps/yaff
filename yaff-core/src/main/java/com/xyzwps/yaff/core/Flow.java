package com.xyzwps.yaff.core;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xyzwps.yaff.core.commons.DAGChecker;
import com.xyzwps.yaff.core.commons.PM;
import com.xyzwps.yaff.core.expression.AssignExpression;
import lombok.Getter;

import java.util.*;

/// 表示一个由节点组成的流程。
///
/// 在执行本检查之前，应先确保 {@link FlowNode#getName()} 对应的 {@link Node} 确实存在。
/// 这里没有 {@link NodeRegister} 作为上下文，无法进行此项检查。
///
/// 检查规则:
/// - R1: id 规则
///   - 各个节点的 id 必须唯一，不能重复，也不能为 null。
///   - 必须有一个 id 为 start 的节点
/// - R2: edge 规则
///   - R2.0: 所有边必须是合法的边
///   - R2.1: 特殊节点
///     - R2.1.1: 没有从 end 节点出发的边。
///     - R2.1.2: 从 all 节点出发的边必须都是 fallback 边
///   - R2.2: 从一个节点出发可以存在多个边。也可以没有边（比如 end 节点）
///   - R2.3: 从一个节点出发，如果右边，那么必须有且仅有一个 fallback 边。
///   - R2.4: 从一个节点出发，每个边的到达节点必须不同。
/// - R3: 图规则
///   - 确保是有向无环图
///   - 除了 start 节点外，所有节点都必须至少有一个 parent。
public class Flow {

    @Getter
    private final List<FlowNode> flowNodes;

    @Getter
    private final List<AssignExpression> flowInputs;

    @JsonIgnore
    private final Map<String, FlowNode> idToNode;

    Flow(List<FlowNode> flowNodes, List<AssignExpression> flowInputs) {
        this.flowNodes = flowNodes == null ? List.of() : List.copyOf(flowNodes);
        this.flowInputs = flowInputs == null ? List.of() : List.copyOf(flowInputs);
        this.idToNode = r1(this.flowNodes);
        this.r2();
        this.r3();
        this.r3();
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
            var edges = node.getEdges();
            PM.run(
                    PM.RBranch.of(() -> NodeIds.END.equals(node.getId()), () -> {
                        R_2_1_1_noEdgesFromEndNode(edges);
                    }),
                    PM.RBranch.of(() -> ControlNode.ALL_NODE_NAME.equals(node.getName()), () -> {
                        if (edges == null || edges.isEmpty()) {
                            return;
                        }
                        R_2_0_validateEdges(edges);
                        R_2_1_2_everyEdgesFromAllNodeShouldBeFallbackEdge(edges);
                    }),
                    PM.RBranch.fallback(() -> {
                        if (edges == null || edges.isEmpty()) {
                            return;
                        }
                        R_2_0_validateEdges(edges);
                        R_2_3_onlyOneFallbackEdgeAllowed(edges);
                        R_2_4_noDuplicatedTos(edges);
                    })
            );
        }
    }

    private static void R_2_1_1_noEdgesFromEndNode(List<FlowEdgeTo> edges) {
        if (edges != null && !edges.isEmpty()) {
            throw new YaffException("No edges cannot start from end node.");
        }
    }

    private static void R_2_1_2_everyEdgesFromAllNodeShouldBeFallbackEdge(List<FlowEdgeTo> edges) {
        for (var edge : edges) {
            if (edge.type() != FlowEdgeTo.EdgeType.FALLBACK) {
                throw new YaffException("Edges from all node must be fallback edges.");
            }
        }
    }

    private static void R_2_0_validateEdges(List<FlowEdgeTo> edges) {
        for (var edge : edges) {
            if (edge == null) {
                throw new YaffException("FlowNode edge cannot be null.");
            }
            if (!edge.validated()) {
                throw new YaffException("FlowNode edge is invalid.");
            }
        }
    }

    private static void R_2_4_noDuplicatedTos(List<FlowEdgeTo> edges) {
        var toSet = new HashSet<String>();
        for (var edge : edges) {
            toSet.add(edge.to());
        }
        if (toSet.size() != edges.size()) {
            throw new YaffException("FlowNode has duplicate next edge.");
        }
    }

    private static void R_2_3_onlyOneFallbackEdgeAllowed(List<FlowEdgeTo> edges) {
        var fallbackEdgeCount = 0;
        for (var edge : edges) {
            if (edge.type() == FlowEdgeTo.EdgeType.FALLBACK) {
                fallbackEdgeCount++;
            }
        }
        if (fallbackEdgeCount <= 0) {
            throw new YaffException("FlowNode must have at least one fallback edge.");
        }
        if (fallbackEdgeCount > 1) {
            throw new YaffException("FlowNode can only have one fallback edge.");
        }
    }

    private void r3() {
        var isDag = DAGChecker.isDAG(flowNodes, FlowNode::getId, n -> n.getEdges() == null ? List.of() : n.getEdges().stream().map(FlowEdgeTo::to).toList());
        if (!isDag) {
            throw new YaffException("Flow is not a DAG.");
        }

        {
            var idToParents = new HashMap<String, List<String>>();
            for (var node : flowNodes) {
                var edges = node.getEdges();
                if (edges != null) {
                    for (var edge : edges) {
                        var nextId = edge.to();
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
