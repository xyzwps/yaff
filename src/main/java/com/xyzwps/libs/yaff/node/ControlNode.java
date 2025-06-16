package com.xyzwps.libs.yaff.node;

import com.xyzwps.libs.yaff.flow.FlowContext;

import java.util.List;
import java.util.Map;

public sealed interface ControlNode extends Node {

    String IF_NODE_NAME = "control.if";

    final class IfNode implements ControlNode {
        @Override
        public String getName() {
            return IF_NODE_NAME;
        }

        @Override
        public List<Parameter> getInputs() {
            return List.of(new Parameter("condition", ParameterType.BOOL));
        }

        @Override
        public List<Parameter> getOutputs() {
            return List.of(new Parameter("result", ParameterType.BOOL));
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
            var conditionValue = inputs.get("condition");
            if (conditionValue instanceof Boolean condition) {
                context.set("result", condition);
            } else {
                throw new RuntimeException("Invalid input value");
            }
        }
    }
}
