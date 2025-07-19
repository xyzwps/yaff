package com.xyzwps.yaff.core;


public interface ControlNode {
    String START_NODE_NAME = "control.start";
    String END_NODE_NAME = "control.end";
    String ALL_NODE_NAME = "control.all";

    // TODO: 写一个根据注解生成节点元数据的工具类

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
