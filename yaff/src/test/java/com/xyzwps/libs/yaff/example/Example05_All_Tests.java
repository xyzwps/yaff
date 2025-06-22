package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.libs.yaff.TestCommons.*;

class Example05_All_Tests {

    @Test
    void test() {
        var nodes = List.<FlowNode>of(
                new FlowNode().id(NodeIds.START)
                        .name(ControlNode.ALL_NODE_NAME)
                        .next("b1", "b2", "b3"),
                new FlowNode().id("b1")
                        .name(PRINT_TEXT_NODE_NAME)
                        .assignExpressions(new ConstantExpression("text", "b1"))
                        .ref("b1"),
                new FlowNode().id("b2")
                        .name(PRINT_TEXT_NODE_NAME)
                        .assignExpressions(new ConstantExpression("text", "b2"))
                        .ref("b2"),
                new FlowNode().id("b3")
                        .name(PRINT_TEXT_NODE_NAME)
                        .assignExpressions(new ConstantExpression("text", "b3"))
                        .ref("b3")
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.getExecutor();
        var context = FlowContext.create();

        assertNull(context.get("b1"));
        assertNull(context.get("b2"));
        assertNull(context.get("b3"));

        executor.execute(flow, context);

        assertEquals("print(\"b1\")", context.get("b1"));
        assertEquals("print(\"b2\")", context.get("b2"));
        assertEquals("print(\"b3\")", context.get("b3"));
    }
}
