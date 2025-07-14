package com.xyzwps.yaff.core.example;

import com.xyzwps.yaff.core.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Example03CaseWhenTests {

    @Test
    void testCaseWhen() {
        var factory = new FlowFactory()
                .register(Example02IfTests.printNode);

        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(YaffNode.NOOP_NODE_NAME)
                        .next("case"),
                new FlowNode()
                        .id("case")
                        .name(ControlNode.CASE_NODE_NAME)
                        .next("when1", "when2", "default"),

                new FlowNode()
                        .id("when1")
                        .name(ControlNode.WHEN_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "a1 > a2"))
                        .next("print1"),
                new FlowNode()
                        .id("print1")
                        .name(Example02IfTests.PRINT_NODE_NAME)
                        .ref("p1")
                        .assignExpressions(new JavaScriptExpression("text", "'a1 is great'"))
                        .next(NodeIds.END),

                new FlowNode()
                        .id("when2")
                        .name(ControlNode.WHEN_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "a1 < a2"))
                        .next("print2"),
                new FlowNode()
                        .id("print2")
                        .ref("p2")
                        .name(Example02IfTests.PRINT_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("text", "'a2 is great'"))
                        .next(NodeIds.END),

                new FlowNode()
                        .id("default")
                        .name(ControlNode.DEFAULT_NODE_NAME)
                        .next("print3"),
                new FlowNode()
                        .id("print3")
                        .ref("p3")
                        .name(Example02IfTests.PRINT_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("text", "'a1 is equal to a2'"))
                        .next(NodeIds.END)
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.getExecutor();

        // a1 > a2
        {
            var context = FlowContext.create();
            context.set("a1", 3);
            context.set("a2", 2);

            assertNull(context.get("p1"));
            assertNull(context.get("p2"));
            assertNull(context.get("p3"));

            executor.execute(flow, context);

            assertEquals("print('a1 is great')", context.get("p1"));
            assertNull(context.get("p2"));
            assertNull(context.get("p3"));
        }

        // a1 < a2
        {
            var context = FlowContext.create();
            context.set("a1", 1);
            context.set("a2", 2);

            assertNull(context.get("p1"));
            assertNull(context.get("p2"));
            assertNull(context.get("p3"));

            executor.execute(flow, context);

            assertNull(context.get("p1"));
            assertEquals("print('a2 is great')", context.get("p2"));
            assertNull(context.get("p3"));
        }

        // a1 == a2
        {
            var context = FlowContext.create();
            context.set("a1", 2);
            context.set("a2", 2);

            assertNull(context.get("p1"));
            assertNull(context.get("p2"));
            assertNull(context.get("p3"));

            executor.execute(flow, context);

            assertNull(context.get("p1"));
            assertNull(context.get("p2"));
            assertEquals("print('a1 is equal to a2')", context.get("p3"));
        }
    }
}
