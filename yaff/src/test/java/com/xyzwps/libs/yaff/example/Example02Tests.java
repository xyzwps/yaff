package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;
import com.xyzwps.libs.yaff.NodeIds;
import com.xyzwps.libs.yaff.ControlNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Example02Tests {

    /**
     * if node example
     */
    @Test
    void test() {
        var factory = new FlowFactory()
                .register(printNode);

        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(YaffNode.NOOP_NODE_NAME)
                        .next("check"),
                new FlowNode()
                        .id("check")
                        .name(ControlNode.IF_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "ctx.a1 > ctx.a2"))
                        .next("printA1", "printA2"),
                new FlowNode()
                        .id("printA1")
                        .name(PRINT_NODE_NAME)
                        .assignExpressions(new ConstantExpression("text", "a1 is great"))
                        .next(NodeIds.END),
                new FlowNode()
                        .id("printA2")
                        .name(PRINT_NODE_NAME)
                        .assignExpressions(new ConstantExpression("text", "a2 is great"))
                        .next(NodeIds.END)
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.createExecutor();

        var context = FlowContext.create();
        context.set("a1", 1);
        context.set("a2", 2);

        assertNull(context.get("printA1.printContent"));
        assertNull(context.get("printA2.printContent"));

        executor.execute(flow, context);

        assertNull(context.get("printA1.printContent"));
        assertEquals("print('a2 is great')", context.get("printA2.printContent"));
    }

    static final String PRINT_NODE_NAME = "print";

    static final Node printNode = Node.builder()
            .name(PRINT_NODE_NAME)
            .description("Print a message")
            .inputs(new Parameter("text", ParameterType.STRING))
            .outputs(new Parameter("printContent", ParameterType.STRING))
            .execute((inputs, context) -> {
                var textStr = inputs.get("text");
                if (textStr instanceof String text) {
                    context.set("printContent", "print('" + text + "')");
                } else {
                    throw new RuntimeException("Invalid input value");
                }
            }).build();


}