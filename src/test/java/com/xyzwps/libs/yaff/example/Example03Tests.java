package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.commons.NodeIds;
import com.xyzwps.libs.yaff.flow.*;
import com.xyzwps.libs.yaff.node.ControlNode;
import com.xyzwps.libs.yaff.node.NoopNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Example03Tests {

    @Test
    void test() {
        var factory = new FlowFactory()
                .register(new Example02Tests.PrintNode());

        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(NoopNode.NAME)
                        .next("case"),
                new FlowNode()
                        .id("case")
                        .name(ControlNode.CASE_NODE_NAME)
                        .next("when1", "when2", "default"),

                new FlowNode()
                        .id("when1")
                        .name(ControlNode.WHEN_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "ctx.a1 > ctx.a2"))
                        .next("print1"),
                new FlowNode()
                        .id("print1")
                        .name(Example02Tests.PrintNode.NAME)
                        .assignExpressions(new ConstExpression("text", "a1 is great"))
                        .next(NodeIds.END),

                new FlowNode()
                        .id("when2")
                        .name(ControlNode.WHEN_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "ctx.a1 < ctx.a2"))
                        .next("print2"),
                new FlowNode()
                        .id("print2")
                        .name(Example02Tests.PrintNode.NAME)
                        .assignExpressions(new ConstExpression("text", "a2 is great"))
                        .next(NodeIds.END),

                new FlowNode()
                        .id("default")
                        .name(ControlNode.DEFAULT_NODE_NAME)
                        .next("print3"),
                new FlowNode()
                        .id("print3")
                        .name(Example02Tests.PrintNode.NAME)
                        .assignExpressions(new ConstExpression("text", "a1 is equal to a2"))
                        .next(NodeIds.END)
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.createExecutor();

        // a1 > a2
        {
            var context = FlowContext.create();
            context.set("a1", 3);
            context.set("a2", 2);

            assertNull(context.get("print1.printContent"));
            assertNull(context.get("print2.printContent"));
            assertNull(context.get("print3.printContent"));

            executor.execute(flow, context);

            assertEquals("print('a1 is great')", context.get("print1.printContent"));
            assertNull(context.get("print2.printContent"));
            assertNull(context.get("print3.printContent"));
        }

        // a1 < a2
        {
            var context = FlowContext.create();
            context.set("a1", 1);
            context.set("a2", 2);

            assertNull(context.get("print1.printContent"));
            assertNull(context.get("print2.printContent"));
            assertNull(context.get("print3.printContent"));

            executor.execute(flow, context);

            assertNull(context.get("print1.printContent"));
            assertEquals("print('a2 is great')", context.get("print2.printContent"));
            assertNull(context.get("print3.printContent"));
        }

        // a1 == a2
        {
            var context = FlowContext.create();
            context.set("a1", 2);
            context.set("a2", 2);

            assertNull(context.get("print1.printContent"));
            assertNull(context.get("print2.printContent"));
            assertNull(context.get("print3.printContent"));

            executor.execute(flow, context);

            assertNull(context.get("print1.printContent"));
            assertNull(context.get("print2.printContent"));
            assertEquals("print('a1 is equal to a2')", context.get("print3.printContent"));
        }
    }
}
