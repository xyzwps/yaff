package com.xyzwps.yaff.core.example;

import com.xyzwps.yaff.core.*;
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
                        .next("when"),
                new FlowNode()
                        .id("when")
                        .name(ControlNode.WHEN_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "a1 > a2"))
                        .next("printA1"),
                new FlowNode()
                        .id("printA1")
                        .name(PRINT_NODE_NAME)
                        .ref("p1")
                        .assignExpressions(new JavaScriptExpression("text", "'a1 is great'"))
                        .next(NodeIds.END)
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