package com.xyzwps.libs.yaff.example;

import com.xyzwps.libs.yaff.*;
import com.xyzwps.libs.yaff.NodeIds;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Example02Tests {

    /**
     * if node example
     */
    @Test
    public void test() {
        var factory = new FlowFactory()
                .register(new PrintNode());

        var nodes = List.<FlowNode>of(
                new FlowNode()
                        .id(NodeIds.START)
                        .name(NoopNode.NAME)
                        .next("check"),
                new FlowNode()
                        .id("check")
                        .name(ControlNode.IfNode.IF_NODE_NAME)
                        .assignExpressions(new JavaScriptExpression("condition", "ctx.a1 > ctx.a2"))
                        .next("printA1", "printA2"),
                new FlowNode()
                        .id("printA1")
                        .name(PrintNode.NAME)
                        .assignExpressions(new ConstantExpression("text", "a1 is great"))
                        .next(NodeIds.END),
                new FlowNode()
                        .id("printA2")
                        .name(PrintNode.NAME)
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


    static class PrintNode implements Node {

        public static final String NAME = "print";

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
            return List.of(new Parameter("printContent", ParameterType.STRING));
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
            var textStr = inputs.get("text");
            if (textStr instanceof String text) {
                context.set("printContent", "print('" + text + "')");
            } else {
                throw new RuntimeException("Invalid input value");
            }
        }
    }


}