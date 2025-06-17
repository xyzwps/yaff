package com.xyzwps.libs.yaff.example;


import com.xyzwps.libs.yaff.commons.JSON;
import com.xyzwps.libs.yaff.commons.NodeIds;
import com.xyzwps.libs.yaff.flow.*;
import com.xyzwps.libs.yaff.node.Node;
import com.xyzwps.libs.yaff.node.Parameter;
import com.xyzwps.libs.yaff.node.ParameterType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class Example01Tests {

    /**
     * simple example
     */
    @Test
    public void test() {
        var factory = new FlowFactory()
                .register(new ToTextNode())
                .register(new PrintNode());

        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(ToTextNode.NAME)
                        .assignExpressions(new ConstantExpression("value", "Hello World"))
                        .next("print"),
                new FlowNode()
                        .id("print")
                        .name(PrintNode.NAME)
                        .assignExpressions(new JavaScriptExpression("text", "start.text"))
                        .next(NodeIds.END)
        );

        var flow = factory.createFlow(nodes);
        var executor = factory.createExecutor();

        var context = FlowContext.create();
        assertNull(context.get("start.text"));

        executor.execute(flow, context);
        assertEquals("Hello World", context.get("start.text"));

        System.out.println("flow: " + JSON.stringify(flow));
        System.out.println("meta: " + JSON.stringify(factory.getNodeRegister()));
    }

    private static class ToTextNode implements Node {

        public static final String NAME = "example.toText";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public String getDescription() {
            return NAME;
        }

        @Override
        public List<Parameter> getInputs() {
            return List.of(new Parameter("value", ParameterType.STRING));
        }

        @Override
        public List<Parameter> getOutputs() {
            return List.of(new Parameter("text", ParameterType.STRING));
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
            var textValue = inputs.get("value");
            if (textValue instanceof String text) {
                context.set("text", text);
            } else {
                throw new RuntimeException("Invalid input value");
            }
        }
    }

    private static class PrintNode implements Node {

        public static final String NAME = "example.print";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public String getDescription() {
            return NAME;
        }

        @Override
        public List<Parameter> getInputs() {
            return List.of(new Parameter("text", ParameterType.STRING));
        }

        @Override
        public List<Parameter> getOutputs() {
            return List.of();
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
            var textValue = inputs.get("text");
            if (textValue instanceof String text) {
                System.out.println(text);
            } else {
                throw new RuntimeException("Invalid input value");
            }
        }
    }
}