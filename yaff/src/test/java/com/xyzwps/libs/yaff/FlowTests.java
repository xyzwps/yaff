package com.xyzwps.libs.yaff;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xyzwps.libs.yaff.TestCommons.*;
import static org.junit.jupiter.api.Assertions.*;


class FlowTests {

    @Nested
    class R1Tests {

        @Test
        void testOK_noEnd() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
            );
            var flow = factory.createFlow(nodes);
            var executor = factory.getExecutor();
            var context = FlowContext.create();
            executor.execute(flow, context);
            assertEquals("HELLO", context.get("upperText"));
        }

        @Test
        void testOK_hasVirtualEnd() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next(NodeIds.END)
            );
            var flow = factory.createFlow(nodes);
            var executor = factory.getExecutor();
            var context = FlowContext.create();
            executor.execute(flow, context);
            assertEquals("HELLO", context.get("upperText"));
        }

        @Test
        void testOK_hasRealEnd() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next(NodeIds.END),
                    new FlowNode()
                            .id(NodeIds.END)
                            .name(ControlNode.END_NODE_NAME)
            );
            var flow = factory.createFlow(nodes);
            var executor = factory.getExecutor();
            var context = FlowContext.create();
            executor.execute(flow, context);
            assertEquals("HELLO", context.get("upperText"));
        }

        @Test
        void noStart() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id("__start__")
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("No start node found", ex.getMessage());
        }

        @Test
        void nullId() {
            var ex = assertThrows(YaffException.class, () -> new FlowNode().id(null));
            assertEquals("FlowNode id cannot be null.", ex.getMessage());
        }

        @Test
        void duplicatedIds() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("FlowNode id must be unique: start", ex.getMessage());
        }

    }

    @Nested
    class R2Tests {

        @Test
        void nextNotFound() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next("xx")
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("FlowNode xx does not exist.", ex.getMessage());
        }

        @Test
        void endCannotHaveNext() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next(NodeIds.END),
                    new FlowNode()
                            .id(NodeIds.END)
                            .name(ControlNode.END_NODE_NAME)
                            .next(NodeIds.END)
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("End node cannot have next.", ex.getMessage());
        }

        @Test
        void normalNodeCannotHaveMoreThanOneNext() {
            var nodes = List.<FlowNode>of(
                    new FlowNode().id(NodeIds.START).name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode().id("toUpper").name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next("case"),
                    new FlowNode().id("case").name(ControlNode.CASE_NODE_NAME)
                            .next("when1", "when2", "default"),
                    new FlowNode().id("when1").name(ControlNode.WHEN_NODE_NAME)
                            .assignExpressions(new ConstantExpression(ControlNode.CONDITION, true))
                            .next("a1"),
                    new FlowNode().id("when2").name(ControlNode.WHEN_NODE_NAME)
                            .assignExpressions(new ConstantExpression(ControlNode.CONDITION, true))
                            .next("a2"),
                    new FlowNode().id("default").name(ControlNode.DEFAULT_NODE_NAME)
                            .next("d"),
                    new FlowNode().id("a1").name(YaffNode.NOOP_NODE_NAME),
                    new FlowNode().id("a2").name(YaffNode.NOOP_NODE_NAME),
                    new FlowNode().id("d").name(YaffNode.NOOP_NODE_NAME).next("a1", "a2")
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("Node d cannot have more than one next.", ex.getMessage());
        }

        @Test
        void caseNodeShouldHavaNext() {
            var nodes = List.<FlowNode>of(
                    new FlowNode().id(NodeIds.START).name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode().id("toUpper").name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next("case"),
                    new FlowNode().id("case").name(ControlNode.CASE_NODE_NAME)
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("Case node should have at least two next nodes.", ex.getMessage());
        }

        @Test
        void caseNodeShouldHavaAtLeastTwoNext() {
            var nodes = List.<FlowNode>of(
                    new FlowNode().id(NodeIds.START).name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode().id("toUpper").name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next("case"),
                    new FlowNode().id("case").name(ControlNode.CASE_NODE_NAME)
                            .next("when1"),
                    new FlowNode().id("when1").name(ControlNode.WHEN_NODE_NAME)
                            .assignExpressions(new ConstantExpression(ControlNode.CONDITION, true))
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("Case node should have at least two next nodes.", ex.getMessage());
        }

        @Test
        void caseNodeDuplicatedDefaults() {
            var nodes = List.<FlowNode>of(
                    new FlowNode().id(NodeIds.START).name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode().id("toUpper").name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next("case"),
                    new FlowNode().id("case").name(ControlNode.CASE_NODE_NAME)
                            .next("d1", "d2"),
                    new FlowNode().id("d1").name(ControlNode.DEFAULT_NODE_NAME),
                    new FlowNode().id("d2").name(ControlNode.DEFAULT_NODE_NAME)
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("Duplicate default nodes of case node case", ex.getMessage());
        }

        @Test
        void caseNodeWithNotCaseOrDefaultNode() {
            var nodes = List.<FlowNode>of(
                    new FlowNode().id(NodeIds.START).name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode().id("toUpper").name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next("case"),
                    new FlowNode().id("case").name(ControlNode.CASE_NODE_NAME)
                            .next("w1", "d2"),
                    new FlowNode().id("w1").name(ControlNode.WHEN_NODE_NAME)
                            .assignExpressions(new ConstantExpression(ControlNode.CONDITION, true)),
                    new FlowNode().id("d2").name(YaffNode.NOOP_NODE_NAME)
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("Unexpected next of case node case", ex.getMessage());
        }

        @Test
        void justWhen() {
            var nodes = List.<FlowNode>of(
                    new FlowNode().id(NodeIds.START).name(YaffNode.NOOP_NODE_NAME)
                            .next("when"),
                    new FlowNode().id("when").name(ControlNode.WHEN_NODE_NAME)
            );
            factory.createFlow(nodes);
        }

        @Test
        void justDefault() {
            var nodes = List.<FlowNode>of(
                    new FlowNode().id(NodeIds.START).name(YaffNode.NOOP_NODE_NAME)
                            .next("def"),
                    new FlowNode().id("def").name(ControlNode.DEFAULT_NODE_NAME)
            );
            factory.createFlow(nodes);
        }
    }

    @Nested
    class R4Tests {

        @Test
        void shouldBeRAG() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText")
                            .next("next"),
                    new FlowNode()
                            .id("next")
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper")
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("Flow is not a DAG.", ex.getMessage());
        }

        @Test
        void parentShouldExist() {
            var nodes = List.<FlowNode>of(
                    new FlowNode()
                            .id(NodeIds.START)
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper"),
                    new FlowNode()
                            .id("toUpper")
                            .name(TEXT_TO_UPPER_NODE_NAME)
                            .assignExpressions(new ConstantExpression("text", "Hello"))
                            .ref("upperText"),
                    new FlowNode()
                            .id("next")
                            .name(YaffNode.NOOP_NODE_NAME)
                            .next("toUpper")
            );
            var ex = assertThrows(YaffException.class, () -> factory.createFlow(nodes));
            assertEquals("FlowNode next has no parent.", ex.getMessage());
        }

    }
}
