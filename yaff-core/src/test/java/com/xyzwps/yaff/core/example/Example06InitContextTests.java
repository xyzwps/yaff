package com.xyzwps.yaff.core.example;

import com.xyzwps.yaff.core.FlowContext;
import com.xyzwps.yaff.core.FlowNode;
import com.xyzwps.yaff.core.expression.JavaScriptAssignExpression;
import com.xyzwps.yaff.core.NodeIds;
import com.xyzwps.yaff.core.commons.JSON;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xyzwps.yaff.core.TestCommons.*;
import static com.xyzwps.yaff.core.TestCommons.factory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Example06InitContextTests {

    @Test
    void test1() {
        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(TEXT_TO_UPPER_NODE_NAME)
                        .ref("upper")
                        .assignExpressions(new JavaScriptAssignExpression("text", "'Hello World'"))
                        .edges("print"),
                new FlowNode()
                        .id("print")
                        .name(PRINT_TEXT_NODE_NAME)
                        .ref("cmd")
                        .assignExpressions(new JavaScriptAssignExpression("text", "upper + ' -> ' + preset"))
                        .edges(NodeIds.END)
        );

        var flow = factory.createFlow(nodes, List.of(
                new JavaScriptAssignExpression("preset", "123")
        ));
        var executor = factory.getExecutor(LISTENER);

        var context = FlowContext.create();
        assertNull(context.get("upper"));
        assertNull(context.get("cmd"));

        executor.execute(flow, context);
        assertEquals("HELLO WORLD", context.get("upper"));
        assertEquals("print(\"HELLO WORLD -> 123\")", context.get("cmd"));

        System.out.println("flow: " + JSON.stringify(flow));
        System.out.println("meta: " + JSON.stringify(factory.getNodeRegister()));
    }

    @Test
    void testFromJSON() {
        var json = """
                {
                    "flowNodes":[
                        {
                            "id":"start","ref":"upper","name":"example.textToUpper",
                            "edges":[{"to":"print","type":"FALLBACK","expression":null}],
                            "assignExpressions":[{"expression":"'Hello World'","inputName":"text","type":"javascript"}]
                        },{
                            "id":"print","ref":"cmd","name":"example.printText",
                            "edges":[{"to":"end","type":"FALLBACK","expression":null}],
                            "assignExpressions":[{"expression":"upper + ' -> ' + preset","inputName":"text","type":"javascript"}]
                        }
                    ],
                    "flowInputs":[{"expression":"123","inputName":"preset","type":"javascript"}]
                }
                """;

        var flow = factory.fromJSON(json);
        var executor = factory.getExecutor(LISTENER);

        var context = FlowContext.create();
        assertNull(context.get("upper"));
        assertNull(context.get("cmd"));

        executor.execute(flow, context);
        assertEquals("HELLO WORLD", context.get("upper"));
        assertEquals("print(\"HELLO WORLD -> 123\")", context.get("cmd"));
    }
}
