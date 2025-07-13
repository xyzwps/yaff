package com.xyzwps.yaff.core.commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAGCheckerTests {

    @Test
    void testArgs() {
        var e31 = assertThrows(IllegalArgumentException.class, () -> DAGChecker.isDAG(null, null, null));
        assertEquals("Argument data cannot be null.", e31.getMessage());

        var e32 = assertThrows(IllegalArgumentException.class, () -> DAGChecker.isDAG(List.of(), null, null));
        assertEquals("Argument getKey cannot be null.", e32.getMessage());

        var e33 = assertThrows(IllegalArgumentException.class, () -> DAGChecker.isDAG(List.of(), d -> null, null));
        assertEquals("Argument getNext cannot be null.", e33.getMessage());

        var e11 = assertThrows(IllegalArgumentException.class, () -> DAGChecker.isDAG(null));
        assertEquals("Argument nodes cannot be null.", e11.getMessage());
    }

    @Test
    void testEmptyGraph() {
        assertTrue(DAGChecker.isDAG(List.of()));
        assertTrue(DAGChecker.isDAG(List.of(), d -> null, d -> null));
    }

    /// ```
    ///         +---> 3 ----> 4 -->--+
    ///        /                      \
    /// 1 --> 2 ------> 5 ----->-------+---> 6
    ///```
    @Test
    void testTrue() {
        var nodes = List.of(
                new GNode(1, new int[]{2}),
                new GNode(2, new int[]{3, 5}),
                new GNode(3, new int[]{4}),
                new GNode(4, new int[]{6}),
                new GNode(5, new int[]{6}),
                new GNode(6, new int[]{})
        );
        assertTrue(DAGChecker.isDAG(nodes, GNode::id, GNode::nextList));
    }

    /// ```
    ///          +------> 7 ----->----+
    ///         /                      \
    /// 1 ---> 2 -----> 3 ----> 4 ----> 6
    ///         \              /
    ///          +--<-- 5 <---+
    ///```
    @Test
    void testFalse() {
        var nodes = List.of(
                new GNode(1, new int[]{2}),
                new GNode(2, new int[]{3, 7}),
                new GNode(3, new int[]{4}),
                new GNode(4, new int[]{6, 5}),
                new GNode(5, new int[]{2}),
                new GNode(6, new int[]{}),
                new GNode(7, new int[]{6})
        );

        assertFalse(DAGChecker.isDAG(nodes, GNode::id, GNode::nextList));
    }


    record GNode(int id, int[] next) {

        public List<Integer> nextList() {
            var list = new ArrayList<Integer>();
            for (int n : next) {
                list.add(n);
            }
            return list;
        }
    }


}
