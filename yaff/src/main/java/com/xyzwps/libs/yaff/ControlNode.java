package com.xyzwps.libs.yaff;

import java.util.List;

import static com.xyzwps.libs.yaff.ParameterType.*;

public interface ControlNode {
    String IF_NODE_NAME = "control.if";
    String CASE_NODE_NAME = "control.case";
    String WHEN_NODE_NAME = "control.when";
    String DEFAULT_NODE_NAME = "control.default";
    String START_NODE_NAME = "control.start";

    String CONDITION = "condition";

    Node ifNode = new NodeTemplate(
            IF_NODE_NAME,
            "If node",
            List.of(new Parameter("condition", BOOL)),
            List.of(new Parameter("result", BOOL)),
            (inputs, context) -> {
                var conditionValue = inputs.get("condition");
                if (conditionValue instanceof Boolean condition) {
                    context.set("result", condition);
                } else {
                    throw new RuntimeException("Invalid input value");
                }
            }
    );

    Node caseNode = new NodeTemplate(
            CASE_NODE_NAME,
            "Case node",
            List.of(),
            List.of());

    Node whenNode = new NodeTemplate(
            WHEN_NODE_NAME,
            "When node",
            List.of(new Parameter(CONDITION, BOOL)),
            List.of()
    );

    Node defaultNode = new NodeTemplate(
            DEFAULT_NODE_NAME,
            "Default node",
            List.of(),
            List.of());

    Node startNode = new NodeTemplate(
            START_NODE_NAME,
            "Start node",
            List.of(),
            List.of());
}
