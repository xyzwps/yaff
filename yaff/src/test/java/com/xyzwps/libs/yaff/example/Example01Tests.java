package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;
import com.xyzwps.libs.yaff.commons.JSON;
import com.xyzwps.libs.yaff.NodeIds;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xyzwps.libs.yaff.example.Commons.*;
import static org.junit.jupiter.api.Assertions.*;


class Example01Tests {

    /**
     * simple example
     */
    @Test
    void test1() {
        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(TEXT_TO_UPPER_NODE_NAME)
                        .assignExpressions(new ConstantExpression("text", "Hello World"))
                        .next("print"),
                new FlowNode()
                        .id("print")
                        .name(PRINT_TEXT_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("text", "start.text"))
                        .next(NodeIds.END)
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.createExecutor();

        var context = FlowContext.create();
        assertNull(context.get("start.text"));
        assertNull(context.get("print.cmd"));

        executor.execute(flow, context);
        assertEquals("HELLO WORLD", context.get("start.text"));
        assertEquals("print(\"HELLO WORLD\")", context.get("print.cmd"));

        System.out.println("flow: " + JSON.stringify(flow));
        System.out.println("meta: " + JSON.stringify(factory.getNodeRegister()));
    }

    @Test
    void test2() {
        var flowJSON = """
                {"flowNodes":[
                    {
                        "id":"start","name":"example.textToUpper","next":["print"],
                        "assignExpressions":[{
                            "type":"constant",
                            "value":"Hello World",
                            "inputName":"text"
                        }]
                    },
                    {
                        "id":"print","name":"example.printText","next":["end"],
                        "assignExpressions":[{
                            "type":"javascript",
                            "expression":"start.text",
                            "inputName":"text"
                        }]
                    }
                ]}
                """;
        var flow = factory.fromJSON(flowJSON);
        var executor = factory.createExecutor();

        var context = FlowContext.create();
        assertNull(context.get("start.text"));
        assertNull(context.get("print.cmd"));

        executor.execute(flow, context);
        assertEquals("HELLO WORLD", context.get("start.text"));
        assertEquals("print(\"HELLO WORLD\")", context.get("print.cmd"));

        System.out.println("flow: " + JSON.stringify(flow));
        System.out.println("meta: " + JSON.stringify(factory.getNodeRegister()));
    }

}