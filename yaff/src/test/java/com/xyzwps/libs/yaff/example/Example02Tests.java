package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;
import com.xyzwps.libs.yaff.NodeIds;
import com.xyzwps.libs.yaff.ControlNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Example02Tests {

    /**
     * if node example
     */
    @Test
    void testIf() {
        var factory = new FlowFactory()
                .register(printNode);

        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(YaffNode.NOOP_NODE_NAME)
                        .next("dispatch"),
                new FlowNode()
                        .id("dispatch")
                        .name(ControlNode.CASE_NODE_NAME)
                        .next("case", "default"),
                new FlowNode()
                        .id("case")
                        .name(ControlNode.WHEN_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "a1 > a2"))
                        .next("printA1"),
                new FlowNode()
                        .id("default")
                        .name(ControlNode.DEFAULT_NODE_NAME)
                        .next("printA2"),
                new FlowNode()
                        .id("printA1")
                        .name(PRINT_NODE_NAME)
                        .ref("p1")
                        .assignExpressions(new ConstantExpression("text", "a1 is great"))
                        .next(NodeIds.END),
                new FlowNode()
                        .id("printA2")
                        .name(PRINT_NODE_NAME)
                        .ref("p2")
                        .assignExpressions(new ConstantExpression("text", "a2 is great"))
                        .next(NodeIds.END)
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.getExecutor();

        {
            var context = FlowContext.create();
            context.set("a1", 1);
            context.set("a2", 2);

            assertNull(context.get("p1"));
            assertNull(context.get("p2"));

            executor.execute(flow, context);

            assertNull(context.get("p1"));
            assertEquals("print('a2 is great')", context.get("p2"));
        }

        {
            var context = FlowContext.create();
            context.set("a1", 3);
            context.set("a2", 2);

            assertNull(context.get("p1"));
            assertNull(context.get("p2"));

            executor.execute(flow, context);

            assertEquals("print('a1 is great')", context.get("p1"));
            assertNull(context.get("p2"));
        }
    }

    static final String PRINT_NODE_NAME = "print";

    static final Node printNode = Node.builder()
            .name(PRINT_NODE_NAME)
            .description("Print a message")
            .inputs(new NodeInput("text", ParameterType.STRING))
            .output(new NodeOutput(ParameterType.STRING))
            .execute((inputs) -> {
                var textStr = inputs.get("text");
                if (textStr instanceof String text) {
                    return "print('" + text + "')";
                }
                throw new RuntimeException("Invalid inputs value");
            })
            .build();


}