package com.xyzwps.yaff.core;


public interface ControlNode {
    String CASE_NODE_NAME = "control.case";
    String WHEN_NODE_NAME = "control.when";
    String DEFAULT_NODE_NAME = "control.default";
    String START_NODE_NAME = "control.start";
    String END_NODE_NAME = "control.end";
    String ALL_NODE_NAME = "control.all";

    String CONDITION = "condition";

    // TODO: 写一个根据注解生成节点元数据的工具类

    Node caseNode = Node.builder()
            .name(CASE_NODE_NAME)
            .description("Case-When 模式的入口节点。由此出发，可以指向多个 When 节点和最多一个 Default 节点。")
            .build();

    Node whenNode = Node.builder()
            .name(WHEN_NODE_NAME)
            .description("可以用于 Case-When 模式，也可以单独使用。单独使用时，如果条件值为 false，则直接结束整个流程。")
            .inputs(new NodeInput(CONDITION, boolean.class))
            .build();

    Node defaultNode = Node.builder()
            .name(DEFAULT_NODE_NAME)
            .description("用于 Case-When 模式的 Fallback 逻辑，也可以单独使用。单独使用时，没有特殊作用。")
            .build();

    Node startNode = Node.builder()
            .name(START_NODE_NAME)
            .description("流程的开始节点。一个流程只能有一个。")
            .build();

    Node endNode = Node.builder()
            .name(END_NODE_NAME)
            .description("流程的结束节点。一个流程最多有一个。")
            .build();

    Node allNode = Node.builder()
            .name(ALL_NODE_NAME)
            .description("由此节点出发，可以指向多个任意节点。其后继节点会被逐个全部执行。")
            .build();

}
