package com.xyzwps.yaff.core.example;

import com.xyzwps.yaff.core.*;
import com.xyzwps.yaff.core.expression.JavaScriptAssignExpression;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.yaff.core.TestCommons.*;

class Example05AllTests {

    @Test
    void test() {
        var nodes = List.<FlowNode>of(
                new FlowNode().id(NodeIds.START)
                        .name(ControlNode.ALL_NODE_NAME)
                        .edges(
                                FlowEdgeTo.fallback("b1"),
                                FlowEdgeTo.fallback("b2"),
                                FlowEdgeTo.fallback("b3")
                        ),
                new FlowNode().id("b1")
                        .name(PRINT_TEXT_NODE_NAME)
                        .assignExpressions(new JavaScriptAssignExpression("text", "'b1'"))
                        .ref("b1"),
                new FlowNode().id("b2")
                        .name(PRINT_TEXT_NODE_NAME)
                        .assignExpressions(new JavaScriptAssignExpression("text", "'b2'"))
                        .ref("b2"),
                new FlowNode().id("b3")
                        .name(PRINT_TEXT_NODE_NAME)
                        .assignExpressions(new JavaScriptAssignExpression("text", "'b3'"))
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
