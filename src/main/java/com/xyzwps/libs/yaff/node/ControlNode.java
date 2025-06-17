package com.xyzwps.libs.yaff.node;

import com.xyzwps.libs.yaff.flow.FlowContext;

import java.util.List;
import java.util.Map;

public sealed interface ControlNode extends Node {

    String IF_NODE_NAME = "control.if";
    String CASE_NODE_NAME = "control.case";
    String WHEN_NODE_NAME = "control.when";
    String DEFAULT_NODE_NAME = "control.default";

    final class DefaultNode implements ControlNode {
        @Override
        public String getName() {
            return DEFAULT_NODE_NAME;
        }

        @Override
        public String getDescription() {
            return "Fallback in control flow";
        }

        @Override
        public List<Parameter> getInputs() {
            return List.of();
        }

        @Override
        public List<Parameter> getOutputs() {
            return List.of();
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
        }
    }

    final class WhenNode implements ControlNode {
        @Override
        public String getName() {
            return WHEN_NODE_NAME;
        }

        @Override
        public String getDescription() {
            return "Check conditions after case node";
        }

        public static final String CONDITION = "condition";

        @Override
        public List<Parameter> getInputs() {
            return List.of(new Parameter(CONDITION, ParameterType.BOOL));
        }

        @Override
        public List<Parameter> getOutputs() {
            return List.of();
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
        }
    }

    final class CaseNode implements ControlNode {
        @Override
        public String getName() {
            return CASE_NODE_NAME;
        }

        @Override
        public String getDescription() {
            return "For case-when pattern";
        }

        @Override
        public List<Parameter> getInputs() {
            return List.of();
        }

        @Override
        public List<Parameter> getOutputs() {
            return List.of();
        }

        @Override
        public void execute(Map<String, Object> inputs, FlowContext context) {
        }
    }

    final class IfNode implements ControlNode {
        @Override
        public String getName() {
            return IF_NODE_NAME;
        }

        @Override
        public String getDescription() {
            return "For if else pattern";
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
