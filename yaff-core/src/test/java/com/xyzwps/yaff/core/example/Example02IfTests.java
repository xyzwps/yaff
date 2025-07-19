package com.xyzwps.yaff.core.example;

import com.xyzwps.yaff.core.*;
import com.xyzwps.yaff.core.expression.JavaScriptAssignExpression;
import com.xyzwps.yaff.core.expression.JavaScriptExpression;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Example02IfTests {

    /// `If` example.
    ///
    /// We use when node to check.
    @Test
    void testIf() {
        var factory = new FlowFactory()
                .register(printNode);

        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(YaffNode.NOOP_NODE_NAME)
                        .edges(
                                FlowEdgeTo.fallback(NodeIds.END),
                                FlowEdgeTo.check("printA1", new JavaScriptExpression("a1 > a2"))
                        ),
                new FlowNode()
                        .id("printA1")
                        .name(PRINT_NODE_NAME)
                        .ref("p1")
                        .assignExpressions(new JavaScriptAssignExpression("text", "'a1 is great'"))
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.getExecutor();

        {
            var context = FlowContext.create();
            context.set("a1", 1);
            context.set("a2", 2);

            assertNull(context.get("p1"));
            executor.execute(flow, context);
            assertNull(context.get("p1"));
        }

        {
            var context = FlowContext.create();
            context.set("a1", 3);
            context.set("a2", 2);

            assertNull(context.get("p1"));

            executor.execute(flow, context);

            assertEquals("print('a1 is great')", context.get("p1"));
        }
    }

    static final String PRINT_NODE_NAME = "print";

    static final Node printNode = Node.builder()
            .name(PRINT_NODE_NAME)
            .description("Print a message")
            .inputs(new NodeInput("text", String.class))
            .output(new NodeOutput(String.class))
            .execute((inputs) -> {
                var textStr = inputs.get("text");
                if (textStr instanceof String text) {
                    return "print('" + text + "')";
                }
                throw new RuntimeException("Invalid inputs value");
            })
            .build();


}