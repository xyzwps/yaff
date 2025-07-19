package com.xyzwps.yaff.core.example;

import com.xyzwps.yaff.core.*;
import com.xyzwps.yaff.core.expression.JavaScriptAssignExpression;
import com.xyzwps.yaff.core.expression.JavaScriptExpression;
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
                        .edges(
                                FlowEdgeTo.check("print1", new JavaScriptExpression("a1 > a2")),
                                FlowEdgeTo.check("print2", new JavaScriptExpression("a1 < a2")),
                                FlowEdgeTo.fallback("print3")
                        ),
                new FlowNode()
                        .id("print1")
                        .name(Example02IfTests.PRINT_NODE_NAME)
                        .ref("p1")
                        .assignExpressions(new JavaScriptAssignExpression("text", "'a1 is great'"))
                        .edges(NodeIds.END),
                new FlowNode()
                        .id("print2")
                        .ref("p2")
                        .name(Example02IfTests.PRINT_NODE_NAME)
                        .assignExpressions(new JavaScriptAssignExpression("text", "'a2 is great'"))
                        .edges(NodeIds.END),
                new FlowNode()
                        .id("print3")
                        .ref("p3")
                        .name(Example02IfTests.PRINT_NODE_NAME)
                        .assignExpressions(new JavaScriptAssignExpression("text", "'a1 is equal to a2'"))
                        .edges(NodeIds.END)
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
