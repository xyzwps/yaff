package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;
import com.xyzwps.libs.yaff.commons.JSON;
import com.xyzwps.libs.yaff.NodeIds;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xyzwps.libs.yaff.TestCommons.*;
import static org.junit.jupiter.api.Assertions.*;


class Example01_Simple_Tests {

    /**
     * simple example
     */
    @Test
    void test1() {
        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(TEXT_TO_UPPER_NODE_NAME)
                        .ref("upper")
                        .assignExpressions(new ConstantExpression("text", "Hello World"))
                        .next("print"),
                new FlowNode()
                        .id("print")
                        .name(PRINT_TEXT_NODE_NAME)
                        .ref("cmd")
                        .assignExpressions(new JavaScriptExpression("text", "upper"))
                        .next(NodeIds.END)
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.getExecutor(LISTENER);

        var context = FlowContext.create();
        assertNull(context.get("upper"));
        assertNull(context.get("cmd"));

        executor.execute(flow, context);
        assertEquals("HELLO WORLD", context.get("upper"));
        assertEquals("print(\"HELLO WORLD\")", context.get("cmd"));

        System.out.println("flow: " + JSON.stringify(flow));
        System.out.println("meta: " + JSON.stringify(factory.getNodeRegister()));
    }

    @Test
    void testFlowFromJSON() {
        var flowJSON = """
                {"flowNodes":[
                    {
                        "id":"start","ref":"upper","name":"example.textToUpper","next":["print"],
                        "assignExpressions":[{"value":"Hello World","inputName":"text","type":"constant"}]
                    },
                    {
                        "id":"print","ref":"cmd","name":"example.printText","next":["end"],
                        "assignExpressions":[{"expression":"upper","inputName":"text","type":"javascript"}]
                    }
                ]}
                """;
        var flow = factory.fromJSON(flowJSON);
        var executor = factory.getExecutor();

        var context = FlowContext.create();
        assertNull(context.get("upper"));
        assertNull(context.get("cmd"));

        executor.execute(flow, context);
        assertEquals("HELLO WORLD", context.get("upper"));
        assertEquals("print(\"HELLO WORLD\")", context.get("cmd"));

        System.out.println("flow: " + JSON.stringify(flow));
        System.out.println("meta: " + JSON.stringify(factory.getNodeRegister()));
    }

}