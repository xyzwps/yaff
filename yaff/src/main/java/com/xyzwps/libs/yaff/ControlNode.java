package com.xyzwps.libs.yaff;

import static com.xyzwps.libs.yaff.ParameterType.*;

public interface ControlNode {
    String IF_NODE_NAME = "control.if";
    String CASE_NODE_NAME = "control.case";
    String WHEN_NODE_NAME = "control.when";
    String DEFAULT_NODE_NAME = "control.default";
    String START_NODE_NAME = "control.start";
    String END_NODE_NAME = "control.end";

    String CONDITION = "condition";

    Node ifNode = Node.builder()
            .name(IF_NODE_NAME)
            .description("If 节点")
            .inputs(new NodeInput("condition", BOOL))
            .output(new NodeOutput(BOOL))
            .execute((inputs) -> {
                var conditionValue = inputs.get("condition");
                if (conditionValue instanceof Boolean condition) {
                    return condition;
                }
                throw new RuntimeException("Invalid inputs value");
            })
            .build();

    Node caseNode = Node.builder()
            .name(CASE_NODE_NAME)
            .description("Case 节点")
            .build();

    Node whenNode = Node.builder()
            .name(WHEN_NODE_NAME)
            .description("条件节点")
            .inputs(new NodeInput(CONDITION, BOOL))
            .build();

    Node defaultNode = Node.builder()
            .name(DEFAULT_NODE_NAME)
            .description("默认分支")
            .build();

    Node startNode = Node.builder()
            .name(START_NODE_NAME)
            .description("开始节点")
            .build();

    Node endNode = Node.builder()
            .name(END_NODE_NAME)
            .description("结束节点")
            .build();
}
