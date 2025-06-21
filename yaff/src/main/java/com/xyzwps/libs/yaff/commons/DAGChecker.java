package com.xyzwps.libs.yaff.commons;


import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public final class DAGChecker {

    public static <T, K> boolean isDAG(List<T> data, Function<T, K> getKey, Function<T, List<K>> getNext) {
        Arguments.notNull(data, "data");
        Arguments.notNull(getKey, "getKey");
        Arguments.notNull(getNext, "getNext");

        var nodes = data.stream().map(d -> new GraphNode<>(d, getKey.apply(d), getNext.apply(d))).toList();
        return isDAG(nodes);
    }

    public static <T, K> boolean isDAG(List<GraphNode<T, K>> nodes) {
        Arguments.notNull(nodes, "nodes");

        var keyToNode = new HashMap<K, GraphNode<T, K>>();
        for (var node : nodes) {
            keyToNode.put(node.key, node);
        }

        for (var node : nodes) {
            if (node.next == null || node.next.isEmpty()) {
                continue;
            }
            for (var next : node.next) {
                var nextNode = keyToNode.get(next);
                if (nextNode != null) {
                    nextNode.inDegree++;
                }
            }
        }

        while (!keyToNode.isEmpty()) {
            var zeroInDegreeNodes = keyToNode.values().stream()
                    .filter(node -> node.inDegree == 0)
                    .toList();
            if (zeroInDegreeNodes.isEmpty()) {
                return false;
            }

            for (var ready : zeroInDegreeNodes) {
                keyToNode.remove(ready.key);
                for (var next : ready.next) {
                    var nextNode = keyToNode.get(next);
                    if (nextNode != null) {
                        nextNode.inDegree--;
                    }
                }
            }
        }

        return true;
    }


    public static class GraphNode<T, K> {
        final T value;
        final K key;
        final List<K> next;
        int inDegree = 0;

        public GraphNode(T value, K key, List<K> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
